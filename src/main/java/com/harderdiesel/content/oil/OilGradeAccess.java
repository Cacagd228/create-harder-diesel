package com.harderdiesel.content.oil;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.ServerLevel;

import com.jesz.createdieselgenerators.world.OilChunksSavedData;

/**
 * Хелпер для получения сорта нефти в чанке. Детерминирован от seed+chunkPos.
 * Лёгкая нефть в выборе не участвует.
 */
public final class OilGradeAccess {
    private OilGradeAccess() {}

    public static CrudeGrade getForChunk(ServerLevel level, ChunkPos pos) {
        // Используем тот же seed что и CDG для консистентности
        long seed = level.getSeed();
        RandomSource random = RandomSource.create(seed ^ (long) pos.x * 341873128712L ^ (long) pos.z * 132897987541L ^ 0x5DEECE66DL);
        // Биомы берём как в OilChunksSavedData.getBiomesInChunk
        var biomes = OilChunksSavedData.getBiomesInChunk(level, pos);
        return OilBiomeWeights.pickForChunkBiomes(random, biomes);
    }

    public static CrudeGrade getForChunkSeeded(long seed, ChunkPos pos, java.util.List<net.minecraft.core.Holder<net.minecraft.world.level.biome.Biome>> biomes) {
        RandomSource random = RandomSource.create(seed ^ (long) pos.x * 341873128712L ^ (long) pos.z * 132897987541L ^ 0x5DEECE66DL);
        return OilBiomeWeights.pickForChunkBiomes(random, biomes);
    }
}
