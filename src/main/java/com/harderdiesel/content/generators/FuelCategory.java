package com.harderdiesel.content.generators;

import com.harderdiesel.HarderDiesel;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

/**
 * Семейство топлива, под которое делается генератор. Каждый генератор принимает
 * только жидкости из своего тега {@code harderdiesel:generator_fuels/<id>} —
 * в газовый нельзя залить бензин и т.д.
 */
public enum FuelCategory {
    GASOLINE("gasoline", "gasoline", MapColor.COLOR_ORANGE),
    DIESEL("diesel", "diesel", MapColor.COLOR_BLUE),
    GAS("gas", "gas", MapColor.COLOR_CYAN),
    NITRO("nitro", "nitro", MapColor.COLOR_MAGENTA);

    /** Суффикс для регистрации: {@code gasoline_generator}, {@code large_gasoline_generator}... */
    public final String id;
    public final TagKey<Fluid> fuelTag;
    public final MapColor mapColor;

    FuelCategory(String id, String tagPath, MapColor mapColor) {
        this.id = id;
        this.fuelTag = TagKey.create(Registries.FLUID,
                ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "generator_fuels/" + tagPath));
        this.mapColor = mapColor;
    }

    /** Можно ли залить эту жидкость в генератор данного семейства. */
    public boolean accepts(Level level, Fluid fluid) {
        if (fluid == null || fluid == Fluids.EMPTY)
            return false;
        Registry<Fluid> registry = level.registryAccess().registryOrThrow(Registries.FLUID);
        return registry.getTag(fuelTag)
                .map(tag -> tag.contains(fluid.builtInRegistryHolder()))
                .orElse(false);
    }

    public static boolean accepts(FuelCategory category, Level level, Fluid fluid) {
        return category != null && category.accepts(level, fluid);
    }
}
