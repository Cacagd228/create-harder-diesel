package com.harderdiesel.content.oil;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

/**
 * Глобальный тумблер нефти и детерминированная редкость месторождений.
 * Состояние хранится в серверном конфиге (OilEnabled) — переживает рестарт
 * и синхронизируется на клиент. Существующие месторождения (уже записанные
 * в OilChunksSavedData CDG) не затрагиваются ни тумблером, ни редкостью.
 */
public final class OilToggle {
    private OilToggle() {}

    public static boolean enabled() {
        try {
            return com.harderdiesel.ModConfig.OIL_ENABLED.get();
        } catch (Throwable t) {
            // конфиг ещё не загружен (ранняя фаза) — считаем включённой
            return true;
        }
    }

    /**
     * Детерминированная проверка «есть ли в чанке нефть» по доле из конфига.
     * Хэш от seed+координат — результат стабилен между перезапусками,
     * поэтому месторождения не «мигают».
     */
    public static boolean chunkHasOil(ServerLevel level, ChunkPos pos) {
        double chance;
        try {
            chance = com.harderdiesel.ModConfig.OIL_CHUNK_CHANCE.get();
        } catch (Throwable t) {
            return true;
        }
        if (chance >= 1.0)
            return true;
        if (chance <= 0.0)
            return false;
        long h = level.getSeed()
                ^ pos.x * 0x9E3779B97F4A7C15L
                ^ pos.z * 0xC2B2AE3D27D4EB4FL;
        h ^= h >>> 33;
        h *= 0xFF51AFD7ED558CCDL;
        h ^= h >>> 33;
        double frac = (h & 0xFFFFFFFFL) / (double) (1L << 32);
        return frac < chance;
    }
}
