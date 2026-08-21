package com.harderdiesel.content.pollution;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Серверный менеджер загрязнения. Хранение в PollutionSavedData.
 * Диффузия + распад выполняются раз в POLLUTION_TICK_INTERVAL тиков.
 */
public class PollutionManager {
    public static final int POLLUTION_TICK_INTERVAL = 20; // раз в секунду
    public static float DIFFUSION_RATE = 0.0018F; // консервативный flux = diffusion*(val-neighbor)/4, caps diff*0.45 val*0.24; B: 0.0018 → завод 1.08/с выходит на ~500 (T2-T3) за ~9-10 мин (читается из конфига)
    public static float DECAY_RATE = 0.0004F; // B: 0.0004 нелинейный 1000→0 ~80мин, 2× медленнее набора (читается из конфига)
    public static final float SYNC_THRESHOLD = 0.1F; // план: 0.1 для видимости при 0.02/с (синк ~5с)
    public static double globalMultiplier = 1.0; // множитель из команды / конфига

    private static int tickCounter = 0;
    // кэш последних синкнутых значений для throttling
    private static final Map<String, Map<Long, Float>> lastSynced = new HashMap<>();

    public static String dimId(ServerLevel level) {
        return level.dimension().location().toString();
    }

    public static float getPollution(ServerLevel level, ChunkPos pos) {
        return PollutionSavedData.get(level).get(pos);
    }

    public static float getPollution(ServerLevel level, int chunkX, int chunkZ) {
        return getPollution(level, new ChunkPos(chunkX, chunkZ));
    }

    public static void setPollution(ServerLevel level, ChunkPos pos, float value, boolean sync) {
        PollutionSavedData.get(level).set(pos, value);
        if (sync) syncChunk(level, pos, value);
    }

    public static void addPollution(ServerLevel level, ChunkPos pos, float delta) {
        if (delta <= 0) return;
        if (level == null) return;
        PollutionSavedData data = PollutionSavedData.get(level);
        float before = data.get(pos);
        float after = Math.min(1000F, before + delta);
        data.set(pos, after);
        // не спамим пакетами каждый тик — синк будет в батче на diffusion tick либо по threshold
        if (Math.abs(after - getLastSynced(level, pos.toLong())) >= SYNC_THRESHOLD) {
            syncChunk(level, pos, after);
        }
    }

    public static void addPollution(ServerLevel level, int chunkX, int chunkZ, float delta) {
        addPollution(level, new ChunkPos(chunkX, chunkZ), delta);
    }

    private static float getLastSynced(ServerLevel level, long posLong) {
        Map<Long, Float> map = lastSynced.get(dimId(level));
        if (map == null) return -9999F;
        return map.getOrDefault(posLong, -9999F);
    }

    private static void markSynced(ServerLevel level, ChunkPos pos, float val) {
        lastSynced.computeIfAbsent(dimId(level), k -> new HashMap<>()).put(pos.toLong(), val);
    }

    /** Отправить SyncPollutionChunkPacket если XaerosZones загружен, иначе no-op */
    public static void syncChunk(ServerLevel level, ChunkPos pos, float value) {
        markSynced(level, pos, value);
        try {
            Class<?> cls = Class.forName("com.slavav.xaeroszones.network.packet.s2c.SyncPollutionChunkPacket");
            String dim = dimId(level);
            Object packet = cls.getConstructor(String.class, int.class, int.class, float.class)
                    .newInstance(dim, pos.x, pos.z, value);
            PacketDistributor.sendToPlayersTrackingChunk(level, pos, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
            // fallback для дебага: ещё и всем в измерении чтобы 100% видно на карте
            // PacketDistributor.sendToPlayersInDimension(level, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
        } catch (Throwable t) {
            // XaerosZones не установлен — игнор
            // System.err.println("[HarderDiesel] syncChunk failed: " + t);
        }
    }

    public static void syncAreaToPlayer(ServerPlayer player, ServerLevel level, int centerChunkX, int centerChunkZ, int radius) {
        PollutionSavedData data = PollutionSavedData.get(level);
        Map<ChunkPos, Float> area = new HashMap<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                ChunkPos cp = new ChunkPos(centerChunkX + dx, centerChunkZ + dz);
                float v = data.get(cp);
                if (v > 0.01F) area.put(cp, v);
            }
        }
        if (area.isEmpty()) return;
        try {
            Class<?> cls = Class.forName("com.slavav.xaeroszones.network.packet.s2c.SyncPollutionAreaPacket");
            String dim = dimId(level);
            Object packet = cls.getConstructor(String.class, Map.class).newInstance(dim, area);
            PacketDistributor.sendToPlayer(player, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
        } catch (Throwable ignored) {}
    }

    public static void clearDimensionForPlayer(ServerPlayer player, ServerLevel level) {
        try {
            Class<?> cls = Class.forName("com.slavav.xaeroszones.network.packet.s2c.ClearPollutionPacket");
            String dim = dimId(level);
            Object packet = cls.getConstructor(String.class).newInstance(dim);
            PacketDistributor.sendToPlayer(player, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
        } catch (Throwable ignored) {}
    }

    /** Серверный тик: диффузия + распад + периодический синк. Вызывается из ServerTickEvent. */
    public static void serverTick(List<ServerLevel> levels) {
        tickCounter++;
        if (tickCounter % POLLUTION_TICK_INTERVAL != 0) return;
        for (ServerLevel level : levels) {
            tickLevel(level);
        }
    }

    private static void tickLevel(ServerLevel level) {
        float cfgDecay = com.harderdiesel.ModConfig.POLLUTION_DECAY_RATE.get().floatValue();
        float cfgDiff = com.harderdiesel.ModConfig.POLLUTION_DIFFUSION_RATE.get().floatValue();
        DECAY_RATE = cfgDecay;
        DIFFUSION_RATE = Math.max(0F, Math.min(1F, cfgDiff));
        try { globalMultiplier = com.harderdiesel.ModConfig.POLLUTION_GLOBAL_MULTIPLIER.get(); } catch (Throwable ignored2) {}
        PollutionSavedData data = PollutionSavedData.get(level);
        Map<Long, Float> snap = data.snapshot();
        if (snap.isEmpty()) return;

        // B + cons: нелинейный распад 0.5+val/1000*0.5 + консервативный flux = D*diff/4 caps diff*0.45 val*0.24 (масса сохраняется, 200/50→119/81 за тик при 0.25)
        Map<Long, Float> decayed = new HashMap<>();
        for (Map.Entry<Long, Float> e : snap.entrySet()) {
            float v = e.getValue();
            if (v <= 0.01F) continue;
            float effDecay = DECAY_RATE * (0.5F + v / 1000F * 0.5F);
            decayed.put(e.getKey(), v * (1F - effDecay));
        }
        if (decayed.isEmpty()) return;

        Map<Long, Float> next = new HashMap<>(decayed);
        java.util.List<Long> sortedKeys = new java.util.ArrayList<>(decayed.keySet());
        java.util.Collections.sort(sortedKeys);

        for (Long key : sortedKeys) {
            float val = decayed.get(key);
            ChunkPos cp = new ChunkPos(key);
            long[] neighborKeys = new long[]{ new ChunkPos(cp.x + 1, cp.z).toLong(), new ChunkPos(cp.x - 1, cp.z).toLong(), new ChunkPos(cp.x, cp.z + 1).toLong(), new ChunkPos(cp.x, cp.z - 1).toLong() };
            for (long nk : neighborKeys) {
                // читаем из decayed чтобы D*(a-b) было антисимметрично; merge в next с caps сохраняет массу
                float nVal = decayed.getOrDefault(nk, 0F);
                float diff = val - nVal;
                if (diff <= 0.01F) continue;
                float flux = DIFFUSION_RATE * diff / 4F;
                // 0.45*diff = 45% градиента за тик, 0.24*val = при соседях 0 отток ≤24% массы — нет бифуркации 0.08/0.081
                flux = Math.min(flux, diff * 0.45F);
                flux = Math.min(flux, val * 0.24F);
                if (flux < 0.0005F) continue;
                next.merge(key, -flux, Float::sum);
                next.merge(nk, flux, (a, b) -> Math.min(1000F, a + b));
            }
        }
        // clamp источника после вычитания
        for (Long key : sortedKeys) {
            Float v = next.get(key);
            if (v != null && v < 0) next.put(key, 0F);
        }

        List<ChunkPos> changedChunks = new ArrayList<>();
        for (Map.Entry<Long, Float> e : next.entrySet()) {
            long key = e.getKey();
            float nv = e.getValue();
            float ov = snap.getOrDefault(key, 0F);
            if (Math.abs(nv - ov) < 0.005F && nv <= 0.01F) continue;
            ChunkPos cp = new ChunkPos(key);
            data.set(cp, nv);
            if (Math.abs(nv - getLastSynced(level, key)) >= SYNC_THRESHOLD || (ov <= 0.01F && nv > 0.01F)) {
                changedChunks.add(cp);
            }
        }
        for (ChunkPos cp : changedChunks) {
            syncChunk(level, cp, data.get(cp));
        }
    }

    /** Получить pollution для игрока (по его чанку) */
    public static float getPollutionForPlayer(ServerPlayer player) {
        if (player.level() instanceof ServerLevel sl) {
            ChunkPos cp = new ChunkPos(player.blockPosition());
            return getPollution(sl, cp);
        }
        return 0F;
    }

    private static final java.util.Set<Long> loggedFirst = new java.util.HashSet<>();

    public static void setGlobalMultiplier(double mult) {
        globalMultiplier = Math.max(0, Math.min(100, mult));
        System.out.println("[HarderDiesel][Pollution] global multiplier set to " + globalMultiplier);
    }

    public static double getGlobalMultiplier() { return globalMultiplier; }

    /** Эмиссия helper: добавить delta*globalMultiplier к чанку блока */
    public static void emit(Level level, net.minecraft.core.BlockPos pos, float amount) {
        if (!(level instanceof ServerLevel sl)) return;
        // читаем множитель также из конфига если команда не использовалась
        try { globalMultiplier = com.harderdiesel.ModConfig.POLLUTION_GLOBAL_MULTIPLIER.get(); } catch (Throwable ignored) {}
        float effective = (float)(amount * globalMultiplier);
        if (effective <= 0) return;
        ChunkPos cp = new ChunkPos(pos);
        float before = getPollution(sl, cp);
        addPollution(sl, cp, effective);
        long key = cp.toLong();
        if (before < 0.01F && effective > 0 && loggedFirst.add(key)) {
            System.out.println("[HarderDiesel][Pollution] first emit at " + pos + " chunk=" + cp + " base=" + amount + " mult=" + globalMultiplier + " effective=" + effective + " dim=" + dimId(sl) + " now=" + getPollution(sl, cp));
        }
    }
}
