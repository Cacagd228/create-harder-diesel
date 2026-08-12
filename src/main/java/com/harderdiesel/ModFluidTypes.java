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

    public static final DeferredHolder<FluidType, FluidType> LOW_OCTANE_GASOLINE =
            FLUID_TYPES.register("low_octane_gasoline", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.low_octane_gasoline")
                    .density(740)
                    .viscosity(900)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> HIGH_OCTANE_GASOLINE =
            FLUID_TYPES.register("high_octane_gasoline", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.high_octane_gasoline")
                    .density(760)
                    .viscosity(800)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> ARTISAN_HIGH_OCTANE_GASOLINE =
            FLUID_TYPES.register("artisan_high_octane_gasoline", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.artisan_high_octane_gasoline")
                    .density(780)
                    .viscosity(750)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> NITROMETHANE =
            FLUID_TYPES.register("nitromethane", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.nitromethane")
                    .density(1120)
                    .viscosity(700)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> PROPANE =
            FLUID_TYPES.register("propane", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.propane")
                    .density(500)
                    .viscosity(350)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MIXED_NITROALKANES =
            FLUID_TYPES.register("mixed_nitroalkanes", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.mixed_nitroalkanes")
                    .density(1050)
                    .viscosity(650)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> NITROETHANE =
            FLUID_TYPES.register("nitroethane", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.nitroethane")
                    .density(1050)
                    .viscosity(680)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> NITROPROPANE =
            FLUID_TYPES.register("nitropropane", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.nitropropane")
                    .density(990)
                    .viscosity(620)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LOW_CETANE_DIESEL =
            FLUID_TYPES.register("low_cetane_diesel", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.low_cetane_diesel")
                    .density(830)
                    .viscosity(2400)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MEDIUM_CETANE_DIESEL =
            FLUID_TYPES.register("medium_cetane_diesel", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.medium_cetane_diesel")
                    .density(850)
                    .viscosity(2600)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> HIGH_CETANE_DIESEL =
            FLUID_TYPES.register("high_cetane_diesel", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.high_cetane_diesel")
                    .density(870)
                    .viscosity(2800)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MAZUT =
            FLUID_TYPES.register("mazut", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.mazut")
                    .density(950)
                    .viscosity(5000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
    }
}
