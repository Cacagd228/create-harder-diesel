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

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HEAVY_GAS_OIL =
            FLUIDS.register("heavy_gas_oil", () -> new BaseFlowingFluid.Source(ModFluids.HEAVY_GAS_OIL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HEAVY_GAS_OIL =
            FLUIDS.register("flowing_heavy_gas_oil", () -> new BaseFlowingFluid.Flowing(ModFluids.HEAVY_GAS_OIL_PROPERTIES));

    public static final BaseFlowingFluid.Properties HEAVY_GAS_OIL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HEAVY_GAS_OIL, HEAVY_GAS_OIL, FLOWING_HEAVY_GAS_OIL)
                    .block(ModBlocks.HEAVY_GAS_OIL)
                    .bucket(ModItems.HEAVY_GAS_OIL_BUCKET);

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

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> BUTANE_GAS =
            FLUIDS.register("butane_gas", () -> new BaseFlowingFluid.Source(ModFluids.BUTANE_GAS_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_BUTANE_GAS =
            FLUIDS.register("flowing_butane_gas", () -> new BaseFlowingFluid.Flowing(ModFluids.BUTANE_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties BUTANE_GAS_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.BUTANE_GAS, BUTANE_GAS, FLOWING_BUTANE_GAS)
                    .block(ModBlocks.BUTANE_GAS)
                    .bucket(ModItems.BUTANE_GAS_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> BUTANE_LIQUID =
            FLUIDS.register("butane_liquid", () -> new BaseFlowingFluid.Source(ModFluids.BUTANE_LIQUID_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_BUTANE_LIQUID =
            FLUIDS.register("flowing_butane_liquid", () -> new BaseFlowingFluid.Flowing(ModFluids.BUTANE_LIQUID_PROPERTIES));

    public static final BaseFlowingFluid.Properties BUTANE_LIQUID_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.BUTANE_LIQUID, BUTANE_LIQUID, FLOWING_BUTANE_LIQUID)
                    .block(ModBlocks.BUTANE_LIQUID)
                    .bucket(ModItems.BUTANE_LIQUID_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LPG_GAS =
            FLUIDS.register("lpg_gas", () -> new BaseFlowingFluid.Source(ModFluids.LPG_GAS_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LPG_GAS =
            FLUIDS.register("flowing_lpg_gas", () -> new BaseFlowingFluid.Flowing(ModFluids.LPG_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties LPG_GAS_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LPG_GAS, LPG_GAS, FLOWING_LPG_GAS)
                    .block(ModBlocks.LPG_GAS)
                    .bucket(ModItems.LPG_GAS_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LPG_LIQUID =
            FLUIDS.register("lpg_liquid", () -> new BaseFlowingFluid.Source(ModFluids.LPG_LIQUID_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LPG_LIQUID =
            FLUIDS.register("flowing_lpg_liquid", () -> new BaseFlowingFluid.Flowing(ModFluids.LPG_LIQUID_PROPERTIES));

    public static final BaseFlowingFluid.Properties LPG_LIQUID_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LPG_LIQUID, LPG_LIQUID, FLOWING_LPG_LIQUID)
                    .block(ModBlocks.LPG_LIQUID)
                    .bucket(ModItems.LPG_LIQUID_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> WET_GAS =
            FLUIDS.register("wet_gas", () -> new BaseFlowingFluid.Source(ModFluids.WET_GAS_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_WET_GAS =
            FLUIDS.register("flowing_wet_gas", () -> new BaseFlowingFluid.Flowing(ModFluids.WET_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties WET_GAS_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.WET_GAS, WET_GAS, FLOWING_WET_GAS)
                    .block(ModBlocks.WET_GAS)
                    .bucket(ModItems.WET_GAS_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> PROPANE_LIQUID =
            FLUIDS.register("propane_liquid", () -> new BaseFlowingFluid.Source(ModFluids.PROPANE_LIQUID_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_PROPANE_LIQUID =
            FLUIDS.register("flowing_propane_liquid", () -> new BaseFlowingFluid.Flowing(ModFluids.PROPANE_LIQUID_PROPERTIES));

    public static final BaseFlowingFluid.Properties PROPANE_LIQUID_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.PROPANE_LIQUID, PROPANE_LIQUID, FLOWING_PROPANE_LIQUID)
                    .block(ModBlocks.PROPANE_LIQUID)
                    .bucket(ModItems.PROPANE_LIQUID_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> VACUUM_GAS_OIL =
            FLUIDS.register("vacuum_gas_oil", () -> new BaseFlowingFluid.Source(ModFluids.VACUUM_GAS_OIL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_VACUUM_GAS_OIL =
            FLUIDS.register("flowing_vacuum_gas_oil", () -> new BaseFlowingFluid.Flowing(ModFluids.VACUUM_GAS_OIL_PROPERTIES));

    public static final BaseFlowingFluid.Properties VACUUM_GAS_OIL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.VACUUM_GAS_OIL, VACUUM_GAS_OIL, FLOWING_VACUUM_GAS_OIL)
                    .block(ModBlocks.VACUUM_GAS_OIL)
                    .bucket(ModItems.VACUUM_GAS_OIL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> VACUUM_RESIDUE =
            FLUIDS.register("vacuum_residue", () -> new BaseFlowingFluid.Source(ModFluids.VACUUM_RESIDUE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_VACUUM_RESIDUE =
            FLUIDS.register("flowing_vacuum_residue", () -> new BaseFlowingFluid.Flowing(ModFluids.VACUUM_RESIDUE_PROPERTIES));

    public static final BaseFlowingFluid.Properties VACUUM_RESIDUE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.VACUUM_RESIDUE, VACUUM_RESIDUE, FLOWING_VACUUM_RESIDUE)
                    .block(ModBlocks.VACUUM_RESIDUE)
                    .bucket(ModItems.VACUUM_RESIDUE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> CRACKED_NAPHTHA =
            FLUIDS.register("cracked_naphtha", () -> new BaseFlowingFluid.Source(ModFluids.CRACKED_NAPHTHA_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_CRACKED_NAPHTHA =
            FLUIDS.register("flowing_cracked_naphtha", () -> new BaseFlowingFluid.Flowing(ModFluids.CRACKED_NAPHTHA_PROPERTIES));

    public static final BaseFlowingFluid.Properties CRACKED_NAPHTHA_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.CRACKED_NAPHTHA, CRACKED_NAPHTHA, FLOWING_CRACKED_NAPHTHA)
                    .block(ModBlocks.CRACKED_NAPHTHA)
                    .bucket(ModItems.CRACKED_NAPHTHA_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIGHT_CYCLE_OIL =
            FLUIDS.register("light_cycle_oil", () -> new BaseFlowingFluid.Source(ModFluids.LIGHT_CYCLE_OIL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LIGHT_CYCLE_OIL =
            FLUIDS.register("flowing_light_cycle_oil", () -> new BaseFlowingFluid.Flowing(ModFluids.LIGHT_CYCLE_OIL_PROPERTIES));

    public static final BaseFlowingFluid.Properties LIGHT_CYCLE_OIL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LIGHT_CYCLE_OIL, LIGHT_CYCLE_OIL, FLOWING_LIGHT_CYCLE_OIL)
                    .block(ModBlocks.LIGHT_CYCLE_OIL)
                    .bucket(ModItems.LIGHT_CYCLE_OIL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> FCC_GAS =
            FLUIDS.register("fcc_gas", () -> new BaseFlowingFluid.Source(ModFluids.FCC_GAS_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_FCC_GAS =
            FLUIDS.register("flowing_fcc_gas", () -> new BaseFlowingFluid.Flowing(ModFluids.FCC_GAS_PROPERTIES));

    public static final BaseFlowingFluid.Properties FCC_GAS_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.FCC_GAS, FCC_GAS, FLOWING_FCC_GAS)
                    .block(ModBlocks.FCC_GAS)
                    .bucket(ModItems.FCC_GAS_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> ALKYLATE =
            FLUIDS.register("alkylate", () -> new BaseFlowingFluid.Source(ModFluids.ALKYLATE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_ALKYLATE =
            FLUIDS.register("flowing_alkylate", () -> new BaseFlowingFluid.Flowing(ModFluids.ALKYLATE_PROPERTIES));

    public static final BaseFlowingFluid.Properties ALKYLATE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.ALKYLATE, ALKYLATE, FLOWING_ALKYLATE)
                    .block(ModBlocks.ALKYLATE)
                    .bucket(ModItems.ALKYLATE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> GLYCEROL =
            FLUIDS.register("glycerol", () -> new BaseFlowingFluid.Source(ModFluids.GLYCEROL_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_GLYCEROL =
            FLUIDS.register("flowing_glycerol", () -> new BaseFlowingFluid.Flowing(ModFluids.GLYCEROL_PROPERTIES));

    public static final BaseFlowingFluid.Properties GLYCEROL_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.GLYCEROL, GLYCEROL, FLOWING_GLYCEROL)
                    .block(ModBlocks.GLYCEROL)
                    .bucket(ModItems.GLYCEROL_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> NITROGLYCERIN =
            FLUIDS.register("nitroglycerin", () -> new BaseFlowingFluid.Source(ModFluids.NITROGLYCERIN_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_NITROGLYCERIN =
            FLUIDS.register("flowing_nitroglycerin", () -> new BaseFlowingFluid.Flowing(ModFluids.NITROGLYCERIN_PROPERTIES));

    public static final BaseFlowingFluid.Properties NITROGLYCERIN_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.NITROGLYCERIN, NITROGLYCERIN, FLOWING_NITROGLYCERIN)
                    .block(ModBlocks.NITROGLYCERIN)
                    .bucket(ModItems.NITROGLYCERIN_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> REFORMATE =
            FLUIDS.register("reformate", () -> new BaseFlowingFluid.Source(ModFluids.REFORMATE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_REFORMATE =
            FLUIDS.register("flowing_reformate", () -> new BaseFlowingFluid.Flowing(ModFluids.REFORMATE_PROPERTIES));

    public static final BaseFlowingFluid.Properties REFORMATE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.REFORMATE, REFORMATE, FLOWING_REFORMATE)
                    .block(ModBlocks.REFORMATE)
                    .bucket(ModItems.REFORMATE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> BENZENE =
            FLUIDS.register("benzene", () -> new BaseFlowingFluid.Source(ModFluids.BENZENE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_BENZENE =
            FLUIDS.register("flowing_benzene", () -> new BaseFlowingFluid.Flowing(ModFluids.BENZENE_PROPERTIES));

    public static final BaseFlowingFluid.Properties BENZENE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.BENZENE, BENZENE, FLOWING_BENZENE)
                    .block(ModBlocks.BENZENE)
                    .bucket(ModItems.BENZENE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> TOLUENE =
            FLUIDS.register("toluene", () -> new BaseFlowingFluid.Source(ModFluids.TOLUENE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_TOLUENE =
            FLUIDS.register("flowing_toluene", () -> new BaseFlowingFluid.Flowing(ModFluids.TOLUENE_PROPERTIES));

    public static final BaseFlowingFluid.Properties TOLUENE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.TOLUENE, TOLUENE, FLOWING_TOLUENE)
                    .block(ModBlocks.TOLUENE)
                    .bucket(ModItems.TOLUENE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> XYLENE =
            FLUIDS.register("xylene", () -> new BaseFlowingFluid.Source(ModFluids.XYLENE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_XYLENE =
            FLUIDS.register("flowing_xylene", () -> new BaseFlowingFluid.Flowing(ModFluids.XYLENE_PROPERTIES));

    public static final BaseFlowingFluid.Properties XYLENE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.XYLENE, XYLENE, FLOWING_XYLENE)
                    .block(ModBlocks.XYLENE)
                    .bucket(ModItems.XYLENE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIGHT_SWEET_CRUDE =
            FLUIDS.register("light_sweet_crude", () -> new BaseFlowingFluid.Source(ModFluids.LIGHT_SWEET_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LIGHT_SWEET_CRUDE =
            FLUIDS.register("flowing_light_sweet_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.LIGHT_SWEET_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties LIGHT_SWEET_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LIGHT_SWEET_CRUDE, LIGHT_SWEET_CRUDE, FLOWING_LIGHT_SWEET_CRUDE)
                    .block(ModBlocks.LIGHT_SWEET_CRUDE)
                    .bucket(ModItems.LIGHT_SWEET_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIGHT_SOUR_CRUDE =
            FLUIDS.register("light_sour_crude", () -> new BaseFlowingFluid.Source(ModFluids.LIGHT_SOUR_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_LIGHT_SOUR_CRUDE =
            FLUIDS.register("flowing_light_sour_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.LIGHT_SOUR_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties LIGHT_SOUR_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.LIGHT_SOUR_CRUDE, LIGHT_SOUR_CRUDE, FLOWING_LIGHT_SOUR_CRUDE)
                    .block(ModBlocks.LIGHT_SOUR_CRUDE)
                    .bucket(ModItems.LIGHT_SOUR_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MEDIUM_SWEET_CRUDE =
            FLUIDS.register("medium_sweet_crude", () -> new BaseFlowingFluid.Source(ModFluids.MEDIUM_SWEET_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MEDIUM_SWEET_CRUDE =
            FLUIDS.register("flowing_medium_sweet_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.MEDIUM_SWEET_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties MEDIUM_SWEET_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MEDIUM_SWEET_CRUDE, MEDIUM_SWEET_CRUDE, FLOWING_MEDIUM_SWEET_CRUDE)
                    .block(ModBlocks.MEDIUM_SWEET_CRUDE)
                    .bucket(ModItems.MEDIUM_SWEET_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MEDIUM_SOUR_CRUDE =
            FLUIDS.register("medium_sour_crude", () -> new BaseFlowingFluid.Source(ModFluids.MEDIUM_SOUR_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MEDIUM_SOUR_CRUDE =
            FLUIDS.register("flowing_medium_sour_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.MEDIUM_SOUR_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties MEDIUM_SOUR_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MEDIUM_SOUR_CRUDE, MEDIUM_SOUR_CRUDE, FLOWING_MEDIUM_SOUR_CRUDE)
                    .block(ModBlocks.MEDIUM_SOUR_CRUDE)
                    .bucket(ModItems.MEDIUM_SOUR_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HEAVY_SWEET_CRUDE =
            FLUIDS.register("heavy_sweet_crude", () -> new BaseFlowingFluid.Source(ModFluids.HEAVY_SWEET_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HEAVY_SWEET_CRUDE =
            FLUIDS.register("flowing_heavy_sweet_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.HEAVY_SWEET_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties HEAVY_SWEET_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HEAVY_SWEET_CRUDE, HEAVY_SWEET_CRUDE, FLOWING_HEAVY_SWEET_CRUDE)
                    .block(ModBlocks.HEAVY_SWEET_CRUDE)
                    .bucket(ModItems.HEAVY_SWEET_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> HEAVY_SOUR_CRUDE =
            FLUIDS.register("heavy_sour_crude", () -> new BaseFlowingFluid.Source(ModFluids.HEAVY_SOUR_CRUDE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_HEAVY_SOUR_CRUDE =
            FLUIDS.register("flowing_heavy_sour_crude", () -> new BaseFlowingFluid.Flowing(ModFluids.HEAVY_SOUR_CRUDE_PROPERTIES));

    public static final BaseFlowingFluid.Properties HEAVY_SOUR_CRUDE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.HEAVY_SOUR_CRUDE, HEAVY_SOUR_CRUDE, FLOWING_HEAVY_SOUR_CRUDE)
                    .block(ModBlocks.HEAVY_SOUR_CRUDE)
                    .bucket(ModItems.HEAVY_SOUR_CRUDE_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> MOLTEN_SULFUR =
            FLUIDS.register("molten_sulfur", () -> new BaseFlowingFluid.Source(ModFluids.MOLTEN_SULFUR_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_MOLTEN_SULFUR =
            FLUIDS.register("flowing_molten_sulfur", () -> new BaseFlowingFluid.Flowing(ModFluids.MOLTEN_SULFUR_PROPERTIES));

    public static final BaseFlowingFluid.Properties MOLTEN_SULFUR_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.MOLTEN_SULFUR, MOLTEN_SULFUR, FLOWING_MOLTEN_SULFUR)
                    .block(ModBlocks.MOLTEN_SULFUR)
                    .bucket(ModItems.MOLTEN_SULFUR_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOUR_NAPHTHA =
            FLUIDS.register("sour_naphtha", () -> new BaseFlowingFluid.Source(ModFluids.SOUR_NAPHTHA_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_SOUR_NAPHTHA =
            FLUIDS.register("flowing_sour_naphtha", () -> new BaseFlowingFluid.Flowing(ModFluids.SOUR_NAPHTHA_PROPERTIES));

    public static final BaseFlowingFluid.Properties SOUR_NAPHTHA_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.SOUR_NAPHTHA, SOUR_NAPHTHA, FLOWING_SOUR_NAPHTHA)
                    .block(ModBlocks.SOUR_NAPHTHA)
                    .bucket(ModItems.SOUR_NAPHTHA_BUCKET);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOUR_KEROSENE =
            FLUIDS.register("sour_kerosene", () -> new BaseFlowingFluid.Source(ModFluids.SOUR_KEROSENE_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING_SOUR_KEROSENE =
            FLUIDS.register("flowing_sour_kerosene", () -> new BaseFlowingFluid.Flowing(ModFluids.SOUR_KEROSENE_PROPERTIES));

    public static final BaseFlowingFluid.Properties SOUR_KEROSENE_PROPERTIES =
            new BaseFlowingFluid.Properties(ModFluidTypes.SOUR_KEROSENE, SOUR_KEROSENE, FLOWING_SOUR_KEROSENE)
                    .block(ModBlocks.SOUR_KEROSENE)
                    .bucket(ModItems.SOUR_KEROSENE_BUCKET);

    public static void register(IEventBus modEventBus) {
        FLUIDS.register(modEventBus);
    }
}
