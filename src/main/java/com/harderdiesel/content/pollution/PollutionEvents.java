package com.harderdiesel.content.pollution;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Серверные события для PollutionManager: тик диффузии/распада и синк при входе/смене измерения.
 * Диффузия и распад выполняются по всей SavedData независимо от загрузки чанков;
 * накопление (эмиссия машин) возможно только в загруженных чанках.
 */
public class PollutionEvents {

    /** Период синка области вокруг движущегося игрока (в тиках). */
    private static final int AREA_SYNC_INTERVAL = 40;
    private static final Map<UUID, Integer> areaSyncCounters = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        try {
            java.util.ArrayList<ServerLevel> list = new java.util.ArrayList<>();
            for (ServerLevel sl : event.getServer().getAllLevels()) list.add(sl);
            PollutionManager.serverTick(list);
        } catch (Throwable ignored) {}
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        if (sp.tickCount % AREA_SYNC_INTERVAL != 0) return;

        UUID id = sp.getUUID();
        int n = areaSyncCounters.getOrDefault(id, 0) + 1;
        // каждый второй интервал (раз в ~4с) — достаточно для карты
        if (n % 2 == 0) {
            ChunkPos cp = new ChunkPos(sp.blockPosition());
            PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 6);
        }
        areaSyncCounters.put(id, n);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        // Иначе статические кэши протекут в следующий мир/сервер
        PollutionManager.clearCaches();
        areaSyncCounters.clear();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        areaSyncCounters.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        // Старое измерение чистим
        try {
            Class<?> cls = Class.forName("com.slavav.xaeroszones.network.packet.s2c.ClearPollutionPacket");
            Object packet = cls.getConstructor(String.class).newInstance("*");
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(sp, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
        } catch (Throwable ignored) {}
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // при респавне тоже синкануть область
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }
}
