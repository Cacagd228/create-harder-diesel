package com.harderdiesel.content.oil;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Веса 45/30/15/10 как база (стандарт). Группа А — бедные (50/30/15/5), группа В — богатые (35/30/20/15).
 * Порядок тиров всегда 1>2>3>4. Лёгкая нефть в генерации не используется.
 */
public final class OilBiomeWeights {
    private OilBiomeWeights() {}

    public record Weights(int heavySour, int heavySweet, int mediumSour, int mediumSweet) {
        int total() { return heavySour + heavySweet + mediumSour + mediumSweet; }
    }

    private static final Weights STANDARD = new Weights(45, 30, 15, 10);
    private static final Weights POOR = new Weights(50, 30, 15, 5);
    private static final Weights RICH = new Weights(35, 30, 20, 15);

    private static final Set<ResourceKey<Biome>> POOR_BIOMES = Set.of(
            Biomes.SWAMP, Biomes.MANGROVE_SWAMP,
            Biomes.OCEAN, Biomes.DEEP_OCEAN, Biomes.WARM_OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN,
            Biomes.RIVER, Biomes.FROZEN_RIVER
    );

    private static final Set<ResourceKey<Biome>> RICH_BIOMES = Set.of(
            Biomes.DESERT,
            Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS
    );

    public static Weights forBiomeHolder(List<net.minecraft.core.Holder<Biome>> holders) {
        boolean hasRich = false;
        boolean hasPoor = false;
        for (var h : holders) {
            var keyOpt = h.unwrapKey();
            if (keyOpt.isEmpty()) continue;
            var key = keyOpt.get();
            if (RICH_BIOMES.contains(key)) hasRich = true;
            if (POOR_BIOMES.contains(key)) hasPoor = true;
        }
        if (hasRich) return RICH;
        if (hasPoor) return POOR;
        return STANDARD;
    }

    public static Weights forBiomeKey(ResourceKey<Biome> key) {
        if (RICH_BIOMES.contains(key)) return RICH;
        if (POOR_BIOMES.contains(key)) return POOR;
        return STANDARD;
    }

    public static CrudeGrade pick(RandomSource random, Weights w) {
        int r = random.nextInt(w.total());
        if (r < w.heavySour) return CrudeGrade.HEAVY_SOUR;
        r -= w.heavySour;
        if (r < w.heavySweet) return CrudeGrade.HEAVY_SWEET;
        r -= w.heavySweet;
        if (r < w.mediumSour) return CrudeGrade.MEDIUM_SOUR;
        return CrudeGrade.MEDIUM_SWEET;
    }

    public static CrudeGrade pickForChunkBiomes(RandomSource random, List<net.minecraft.core.Holder<Biome>> holders) {
        return pick(random, forBiomeHolder(holders));
    }
}
