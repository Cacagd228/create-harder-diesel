package com.harderdiesel.content.oil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сорт нефти в чанке. Детерминирован от seed+chunkPos+биомы — как количество нефти в CDG,
 * поэтому нигде не хранится и одинаков на клиенте и сервере.
 * Лёгкая нефть в выборе не участвует.
 */
public final class OilGradeAccess {
    private OilGradeAccess() {}

    // кэш: dimension -> chunkPos -> grade (расчёт дорогой: ~12k сэмплов биомов на чанк).
    // ConcurrentHashMap: пишется серверным потоком, читается клиентским (гогглы/туман).
    private static final Map<String, Map<Long, CrudeGrade>> CACHE = new ConcurrentHashMap<>();
    private static final int CACHE_CAP = 65536;

    /** LRU-мапа: вытесняет самый старый элемент вместо полной очистки всего кэша. */
    private static Map<Long, CrudeGrade> newLruMap() {
        return java.util.Collections.synchronizedMap(new LinkedHashMap<>(256, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, CrudeGrade> eldest) {
                return size() > CACHE_CAP;
            }
        });
    }

    /** Любая сторона, с кэшем. Используется миксинами помпы, сканера, гогглов и дисплеев.
     *  Возвращает null если seed мира недоступен (клиент на удалённом сервере).
     *  На сервере сначала проверяет OilGradeOverrideSavedData (команда oil add). */
    public static CrudeGrade getForChunk(Level level, ChunkPos pos) {
        if (level == null) return null;
        // серверный оверрайд от команды
        if (level instanceof ServerLevel sl) {
            try {
                CrudeGrade overridden = OilGradeOverrideSavedData.get(sl).get(pos);
                if (overridden != null) return overridden;
            } catch (Throwable ignored) {}
        }
        long seed = seedOf(level);
        if (seed == SEED_UNKNOWN) return null;
        String dim = level.dimension().location().toString();
        Map<Long, CrudeGrade> map = CACHE.computeIfAbsent(dim, k -> newLruMap());
        Long key = pos.toLong();
        CrudeGrade cached = map.get(key);
        if (cached != null) return cached;
        RandomSource random = RandomSource.create(seed
                ^ (long) pos.x * 341873128712L
                ^ (long) pos.z * 132897987541L
                ^ 0x5DEECE66DL);
        CrudeGrade grade = OilBiomeWeights.pickForChunkBiomes(random, sampleBiomes(level, pos));
        map.put(key, grade);
        return grade;
    }

    /** Совместимость со старым вызовом (сервер). */
    public static CrudeGrade getForChunk(ServerLevel level, ChunkPos pos) {
        return getForChunk((Level) level, pos);
    }

    public static final long SEED_UNKNOWN = Long.MIN_VALUE;

    /** Seed мира: сервер — напрямую; клиент — только через интегрированный сервер (одиночка/LAN-хост). */
    public static long seedOf(Level level) {
        if (level instanceof ServerLevel sl) return sl.getSeed();
        if (!net.neoforged.fml.loading.FMLLoader.getDist().isClient()) return SEED_UNKNOWN;
        // Не ссылаемся на ClientSeedHolder напрямую — иначе NoClassDefFoundError на dedicated server
        try {
            Class<?> holder = Class.forName("com.harderdiesel.content.oil.ClientSeedHolder");
            java.lang.reflect.Method m = holder.getMethod("get", Level.class);
            Object res = m.invoke(null, level);
            return res instanceof Long l ? l : SEED_UNKNOWN;
        } catch (Throwable t) {
            return SEED_UNKNOWN;
        }
    }

    public static void clearCache() {
        CACHE.clear();
    }

    public static void invalidate(ChunkPos pos, String dim) {
        Map<Long, CrudeGrade> map = CACHE.get(dim);
        if (map != null) map.remove(pos.toLong());
    }

    public static CrudeGrade getForChunkSeeded(long seed, ChunkPos pos, List<Holder<Biome>> biomes) {
        RandomSource random = RandomSource.create(seed
                ^ (long) pos.x * 341873128712L
                ^ (long) pos.z * 132897987541L
                ^ 0x5DEECE66DL);
        return OilBiomeWeights.pickForChunkBiomes(random, biomes);
    }

    /** Сэмплирование биомов чанка — та же схема, что OilChunksSavedData.getBiomesInChunk у CDG (y 60..110). */
    private static List<Holder<Biome>> sampleBiomes(Level level, ChunkPos pos) {
        java.util.Set<Holder<Biome>> set = new java.util.HashSet<>();
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        for (int x = pos.getMinBlockX(); x <= pos.getMaxBlockX(); x++) {
            for (int z = pos.getMinBlockZ(); z <= pos.getMaxBlockZ(); z++) {
                for (int y = 60; y < 110; y++) {
                    mpos.set(x, y, z);
                    set.add(level.getBiome(mpos));
                }
            }
        }
        return new ArrayList<>(set);
    }
}
