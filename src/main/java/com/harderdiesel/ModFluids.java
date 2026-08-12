package com.harderdiesel;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, HarderDiesel.MODID);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> NAPHTHA =
            FLUIDS.register("naphtha", () -> new BaseFlowingFluid.Source(ModFluids.NAPHTHA_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_NAPHTHA =
            FLUIDS.register("flowing_naphtha", () -> new BaseFlowingFluid.Flowing(ModFluids.NAPHTHA_PROPERTIES));

    public static final BaseFlowingFluid.Properties NAPHTHA_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.NAPHTHA, NAPHTHA, FLOWING_NAPHTHA)
                    .block(ModBlocks.NAPHTHA)
                    .bucket(ModItems.NAPHTHA_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> KEROSENE =
            FLUIDS.register("kerosene", () -> new BaseFlowingFluid.Source(ModFluids.KEROSENE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_KEROSENE =
            FLUIDS.register("flowing_kerosene", () -> new BaseFlowingFluid.Flowing(ModFluids.KEROSENE_PROPERTIES));

    public static final BaseFlowingFluid.Properties KEROSENE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.KEROSENE, KEROSENE, FLOWING_KEROSENE)
                    .block(ModBlocks.KEROSENE)
                    .bucket(ModItems.KEROSENE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HEAVY_OIL =
            FLUIDS.register("heavy_oil", () -> new BaseFlowingFluid.Source(ModFluids.HEAVY_OIL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HEAVY_OIL =
            FLUIDS.register("flowing_heavy_oil", () -> new BaseFlowingFluid.Flowing(ModFluids.HEAVY_OIL_PROPERTIES));

    public static final BaseFlowingFluid.Properties HEAVY_OIL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HEAVY_OIL, HEAVY_OIL, FLOWING_HEAVY_OIL)
                    .block(ModBlocks.HEAVY_OIL)
                    .bucket(ModItems.HEAVY_OIL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> TAR =
            FLUIDS.register("tar", () -> new TarSourceFluid(ModFluids.TAR_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_TAR =
            FLUIDS.register("flowing_tar", () -> new TarFlowingFluid(ModFluids.TAR_PROPERTIES));

    public static final BaseFlowingFluid.Properties TAR_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.TAR, TAR, FLOWING_TAR)
                    .block(ModBlocks.TAR)
                    .bucket(ModItems.TAR_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LOW_OCTANE_GASOLINE =
            FLUIDS.register("low_octane_gasoline", () -> new BaseFlowingFluid.Source(ModFluids.LOW_OCTANE_GASOLINE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LOW_OCTANE_GASOLINE =
            FLUIDS.register("flowing_low_octane_gasoline", () -> new BaseFlowingFluid.Flowing(ModFluids.LOW_OCTANE_GASOLINE_PROPERTIES));

    public static final BaseFlowingFluid.Properties LOW_OCTANE_GASOLINE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LOW_OCTANE_GASOLINE, LOW_OCTANE_GASOLINE, FLOWING_LOW_OCTANE_GASOLINE)
                    .block(ModBlocks.LOW_OCTANE_GASOLINE)
                    .bucket(ModItems.LOW_OCTANE_GASOLINE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HIGH_OCTANE_GASOLINE =
            FLUIDS.register("high_octane_gasoline", () -> new BaseFlowingFluid.Source(ModFluids.HIGH_OCTANE_GASOLINE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HIGH_OCTANE_GASOLINE =
            FLUIDS.register("flowing_high_octane_gasoline", () -> new BaseFlowingFluid.Flowing(ModFluids.HIGH_OCTANE_GASOLINE_PROPERTIES));

    public static final BaseFlowingFluid.Properties HIGH_OCTANE_GASOLINE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HIGH_OCTANE_GASOLINE, HIGH_OCTANE_GASOLINE, FLOWING_HIGH_OCTANE_GASOLINE)
                    .block(ModBlocks.HIGH_OCTANE_GASOLINE)
                    .bucket(ModItems.HIGH_OCTANE_GASOLINE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> ARTISAN_HIGH_OCTANE_GASOLINE =
            FLUIDS.register("artisan_high_octane_gasoline", () -> new BaseFlowingFluid.Source(ModFluids.ARTISAN_HIGH_OCTANE_GASOLINE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_ARTISAN_HIGH_OCTANE_GASOLINE =
            FLUIDS.register("flowing_artisan_high_octane_gasoline", () -> new BaseFlowingFluid.Flowing(ModFluids.ARTISAN_HIGH_OCTANE_GASOLINE_PROPERTIES));

    public static final BaseFlowingFluid.Properties ARTISAN_HIGH_OCTANE_GASOLINE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.ARTISAN_HIGH_OCTANE_GASOLINE, ARTISAN_HIGH_OCTANE_GASOLINE, FLOWING_ARTISAN_HIGH_OCTANE_GASOLINE)
                    .block(ModBlocks.ARTISAN_HIGH_OCTANE_GASOLINE)
                    .bucket(ModItems.ARTISAN_HIGH_OCTANE_GASOLINE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> NITROMETHANE =
            FLUIDS.register("nitromethane", () -> new BaseFlowingFluid.Source(ModFluids.NITROMETHANE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_NITROMETHANE =
            FLUIDS.register("flowing_nitromethane", () -> new BaseFlowingFluid.Flowing(ModFluids.NITROMETHANE_PROPERTIES));

    public static final BaseFlowingFluid.Properties NITROMETHANE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.NITROMETHANE, NITROMETHANE, FLOWING_NITROMETHANE)
                    .block(ModBlocks.NITROMETHANE)
                    .bucket(ModItems.NITROMETHANE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> PROPANE =
            FLUIDS.register("propane", () -> new BaseFlowingFluid.Source(ModFluids.PROPANE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_PROPANE =
            FLUIDS.register("flowing_propane", () -> new BaseFlowingFluid.Flowing(ModFluids.PROPANE_PROPERTIES));

    public static final BaseFlowingFluid.Properties PROPANE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.PROPANE, PROPANE, FLOWING_PROPANE)
                    .block(ModBlocks.PROPANE)
                    .bucket(ModItems.PROPANE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MIXED_NITROALKANES =
            FLUIDS.register("mixed_nitroalkanes", () -> new BaseFlowingFluid.Source(ModFluids.MIXED_NITROALKANES_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MIXED_NITROALKANES =
            FLUIDS.register("flowing_mixed_nitroalkanes", () -> new BaseFlowingFluid.Flowing(ModFluids.MIXED_NITROALKANES_PROPERTIES));

    public static final BaseFlowingFluid.Properties MIXED_NITROALKANES_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MIXED_NITROALKANES, MIXED_NITROALKANES, FLOWING_MIXED_NITROALKANES)
                    .block(ModBlocks.MIXED_NITROALKANES)
                    .bucket(ModItems.MIXED_NITROALKANES_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> NITROETHANE =
            FLUIDS.register("nitroethane", () -> new BaseFlowingFluid.Source(ModFluids.NITROETHANE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_NITROETHANE =
            FLUIDS.register("flowing_nitroethane", () -> new BaseFlowingFluid.Flowing(ModFluids.NITROETHANE_PROPERTIES));

    public static final BaseFlowingFluid.Properties NITROETHANE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.NITROETHANE, NITROETHANE, FLOWING_NITROETHANE)
                    .block(ModBlocks.NITROETHANE)
                    .bucket(ModItems.NITROETHANE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> NITROPROPANE =
            FLUIDS.register("nitropropane", () -> new BaseFlowingFluid.Source(ModFluids.NITROPROPANE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_NITROPROPANE =
            FLUIDS.register("flowing_nitropropane", () -> new BaseFlowingFluid.Flowing(ModFluids.NITROPROPANE_PROPERTIES));

    public static final BaseFlowingFluid.Properties NITROPROPANE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.NITROPROPANE, NITROPROPANE, FLOWING_NITROPROPANE)
                    .block(ModBlocks.NITROPROPANE)
                    .bucket(ModItems.NITROPROPANE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LOW_CETANE_DIESEL =
            FLUIDS.register("low_cetane_diesel", () -> new BaseFlowingFluid.Source(ModFluids.LOW_CETANE_DIESEL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LOW_CETANE_DIESEL =
            FLUIDS.register("flowing_low_cetane_diesel", () -> new BaseFlowingFluid.Flowing(ModFluids.LOW_CETANE_DIESEL_PROPERTIES));

    public static final BaseFlowingFluid.Properties LOW_CETANE_DIESEL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LOW_CETANE_DIESEL, LOW_CETANE_DIESEL, FLOWING_LOW_CETANE_DIESEL)
                    .block(ModBlocks.LOW_CETANE_DIESEL)
                    .bucket(ModItems.LOW_CETANE_DIESEL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MEDIUM_CETANE_DIESEL =
            FLUIDS.register("medium_cetane_diesel", () -> new BaseFlowingFluid.Source(ModFluids.MEDIUM_CETANE_DIESEL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MEDIUM_CETANE_DIESEL =
            FLUIDS.register("flowing_medium_cetane_diesel", () -> new BaseFlowingFluid.Flowing(ModFluids.MEDIUM_CETANE_DIESEL_PROPERTIES));

    public static final BaseFlowingFluid.Properties MEDIUM_CETANE_DIESEL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MEDIUM_CETANE_DIESEL, MEDIUM_CETANE_DIESEL, FLOWING_MEDIUM_CETANE_DIESEL)
                    .block(ModBlocks.MEDIUM_CETANE_DIESEL)
                    .bucket(ModItems.MEDIUM_CETANE_DIESEL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HIGH_CETANE_DIESEL =
            FLUIDS.register("high_cetane_diesel", () -> new BaseFlowingFluid.Source(ModFluids.HIGH_CETANE_DIESEL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HIGH_CETANE_DIESEL =
            FLUIDS.register("flowing_high_cetane_diesel", () -> new BaseFlowingFluid.Flowing(ModFluids.HIGH_CETANE_DIESEL_PROPERTIES));

    public static final BaseFlowingFluid.Properties HIGH_CETANE_DIESEL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HIGH_CETANE_DIESEL, HIGH_CETANE_DIESEL, FLOWING_HIGH_CETANE_DIESEL)
                    .block(ModBlocks.HIGH_CETANE_DIESEL)
                    .bucket(ModItems.HIGH_CETANE_DIESEL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MAZUT =
            FLUIDS.register("mazut", () -> new BaseFlowingFluid.Source(ModFluids.MAZUT_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MAZUT =
            FLUIDS.register("flowing_mazut", () -> new BaseFlowingFluid.Flowing(ModFluids.MAZUT_PROPERTIES));

    public static final BaseFlowingFluid.Properties MAZUT_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MAZUT, MAZUT, FLOWING_MAZUT)
                    .block(ModBlocks.MAZUT)
                    .bucket(ModItems.MAZUT_BUCKET);

    public static void register(IEventBus modEventBus) {
        FLUIDS.register(modEventBus);
    }
}
