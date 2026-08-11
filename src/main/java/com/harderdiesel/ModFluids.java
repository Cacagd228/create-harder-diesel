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
            FLUIDS.register("tar", () -> new BaseFlowingFluid.Source(ModFluids.TAR_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_TAR =
            FLUIDS.register("flowing_tar", () -> new BaseFlowingFluid.Flowing(ModFluids.TAR_PROPERTIES));

    public static final BaseFlowingFluid.Properties TAR_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.TAR, TAR, FLOWING_TAR)
                    .block(ModBlocks.TAR)
                    .bucket(ModItems.TAR_BUCKET);

    public static void register(IEventBus modEventBus) {
        FLUIDS.register(modEventBus);
    }
}
