package com.harderdiesel;

import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, HarderDiesel.MODID);

    public static final DeferredHolder<FluidType, FluidType> NAPHTHA =
            FLUID_TYPES.register("naphtha", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.naphtha")
                    .density(650)
                    .viscosity(600)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> KEROSENE =
            FLUID_TYPES.register("kerosene", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.kerosene")
                    .density(800)
                    .viscosity(1200)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> HEAVY_OIL =
            FLUID_TYPES.register("heavy_oil", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.heavy_oil")
                    .density(1400)
                    .viscosity(4000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> TAR =
            FLUID_TYPES.register("tar", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.tar")
                    .density(1100)
                    .viscosity(8000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
    }
}
