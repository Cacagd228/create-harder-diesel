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

    public static final DeferredHolder<FluidType, FluidType> BUTANE_GAS =
            FLUID_TYPES.register("butane_gas", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.butane_gas")
                    .density(560)
                    .viscosity(250)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> BUTANE_LIQUID =
            FLUID_TYPES.register("butane_liquid", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.butane_liquid")
                    .density(600)
                    .viscosity(300)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LPG_GAS =
            FLUID_TYPES.register("lpg_gas", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.lpg_gas")
                    .density(550)
                    .viscosity(240)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LPG_LIQUID =
            FLUID_TYPES.register("lpg_liquid", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.lpg_liquid")
                    .density(620)
                    .viscosity(320)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> WET_GAS =
            FLUID_TYPES.register("wet_gas", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.wet_gas")
                    .density(580)
                    .viscosity(280)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> PROPANE_LIQUID =
            FLUID_TYPES.register("propane_liquid", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.propane_liquid")
                    .density(510)
                    .viscosity(400)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> VACUUM_GAS_OIL =
            FLUID_TYPES.register("vacuum_gas_oil", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.vacuum_gas_oil")
                    .density(960)
                    .viscosity(3500)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> VACUUM_RESIDUE =
            FLUID_TYPES.register("vacuum_residue", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.vacuum_residue")
                    .density(1020)
                    .viscosity(9000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> CRACKED_NAPHTHA =
            FLUID_TYPES.register("cracked_naphtha", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.cracked_naphtha")
                    .density(730)
                    .viscosity(700)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LIGHT_CYCLE_OIL =
            FLUID_TYPES.register("light_cycle_oil", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.light_cycle_oil")
                    .density(880)
                    .viscosity(2000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> FCC_GAS =
            FLUID_TYPES.register("fcc_gas", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.fcc_gas")
                    .density(550)
                    .viscosity(260)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> ALKYLATE =
            FLUID_TYPES.register("alkylate", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.alkylate")
                    .density(700)
                    .viscosity(600)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> GLYCEROL =
            FLUID_TYPES.register("glycerol", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.glycerol")
                    .density(1260)
                    .viscosity(9000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> NITROGLYCERIN =
            FLUID_TYPES.register("nitroglycerin", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.nitroglycerin")
                    .density(1590)
                    .viscosity(1300)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> REFORMATE =
            FLUID_TYPES.register("reformate", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.reformate")
                    .density(770)
                    .viscosity(700)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> BENZENE =
            FLUID_TYPES.register("benzene", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.benzene")
                    .density(880)
                    .viscosity(650)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> TOLUENE =
            FLUID_TYPES.register("toluene", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.toluene")
                    .density(870)
                    .viscosity(590)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> XYLENE =
            FLUID_TYPES.register("xylene", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.xylene")
                    .density(860)
                    .viscosity(650)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LIGHT_SWEET_CRUDE =
            FLUID_TYPES.register("light_sweet_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.light_sweet_crude")
                    .density(700)
                    .viscosity(800)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> LIGHT_SOUR_CRUDE =
            FLUID_TYPES.register("light_sour_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.light_sour_crude")
                    .density(710)
                    .viscosity(850)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MEDIUM_SWEET_CRUDE =
            FLUID_TYPES.register("medium_sweet_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.medium_sweet_crude")
                    .density(830)
                    .viscosity(1500)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MEDIUM_SOUR_CRUDE =
            FLUID_TYPES.register("medium_sour_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.medium_sour_crude")
                    .density(840)
                    .viscosity(1600)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> HEAVY_SWEET_CRUDE =
            FLUID_TYPES.register("heavy_sweet_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.heavy_sweet_crude")
                    .density(950)
                    .viscosity(4000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> HEAVY_SOUR_CRUDE =
            FLUID_TYPES.register("heavy_sour_crude", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.heavy_sour_crude")
                    .density(960)
                    .viscosity(4200)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static final DeferredHolder<FluidType, FluidType> MOLTEN_SULFUR =
            FLUID_TYPES.register("molten_sulfur", () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.harderdiesel.molten_sulfur")
                    .density(1800)
                    .viscosity(6000)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

    public static void register(IEventBus modEventBus) {
        FLUID_TYPES.register(modEventBus);
    }
}
