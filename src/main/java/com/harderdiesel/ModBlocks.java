package com.harderdiesel;

import com.harderdiesel.content.cracking.CrackingReactorBlock;
import com.harderdiesel.content.galvanized.GalvanizedTankBlock;
import com.harderdiesel.content.separator.SeparatorBlock;
import com.harderdiesel.content.wear_resistant.WearResistantTankBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HarderDiesel.MODID);

    public static final DeferredBlock<LiquidBlock> NAPHTHA = BLOCKS.register("naphtha",
            () -> new LiquidBlock(ModFluids.NAPHTHA.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> KEROSENE = BLOCKS.register("kerosene",
            () -> new LiquidBlock(ModFluids.KEROSENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> HEAVY_GAS_OIL = BLOCKS.register("heavy_gas_oil",
            () -> new LiquidBlock(ModFluids.HEAVY_GAS_OIL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> TAR = BLOCKS.register("tar",
            () -> new LiquidBlock(ModFluids.TAR.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LOW_OCTANE_GASOLINE = BLOCKS.register("low_octane_gasoline",
            () -> new LiquidBlock(ModFluids.LOW_OCTANE_GASOLINE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> HIGH_OCTANE_GASOLINE = BLOCKS.register("high_octane_gasoline",
            () -> new LiquidBlock(ModFluids.HIGH_OCTANE_GASOLINE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> ARTISAN_HIGH_OCTANE_GASOLINE = BLOCKS.register("artisan_high_octane_gasoline",
            () -> new LiquidBlock(ModFluids.ARTISAN_HIGH_OCTANE_GASOLINE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> NITROMETHANE = BLOCKS.register("nitromethane",
            () -> new LiquidBlock(ModFluids.NITROMETHANE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> PROPANE = BLOCKS.register("propane",
            () -> new LiquidBlock(ModFluids.PROPANE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MIXED_NITROALKANES = BLOCKS.register("mixed_nitroalkanes",
            () -> new LiquidBlock(ModFluids.MIXED_NITROALKANES.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> NITROETHANE = BLOCKS.register("nitroethane",
            () -> new LiquidBlock(ModFluids.NITROETHANE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> NITROPROPANE = BLOCKS.register("nitropropane",
            () -> new LiquidBlock(ModFluids.NITROPROPANE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LOW_CETANE_DIESEL = BLOCKS.register("low_cetane_diesel",
            () -> new LiquidBlock(ModFluids.LOW_CETANE_DIESEL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MEDIUM_CETANE_DIESEL = BLOCKS.register("medium_cetane_diesel",
            () -> new LiquidBlock(ModFluids.MEDIUM_CETANE_DIESEL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> HIGH_CETANE_DIESEL = BLOCKS.register("high_cetane_diesel",
            () -> new LiquidBlock(ModFluids.HIGH_CETANE_DIESEL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MAZUT = BLOCKS.register("mazut",
            () -> new LiquidBlock(ModFluids.MAZUT.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> BUTANE = BLOCKS.register("butane",
            () -> new LiquidBlock(ModFluids.BUTANE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> BUTANE_LIQUID = BLOCKS.register("butane_liquid",
            () -> new LiquidBlock(ModFluids.BUTANE_LIQUID.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LPG_LIQUID = BLOCKS.register("lpg_liquid",
            () -> new LiquidBlock(ModFluids.LPG_LIQUID.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> PETROLEUM_GAS = BLOCKS.register("petroleum_gas",
            () -> new LiquidBlock(ModFluids.PETROLEUM_GAS.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> PROPANE_LIQUID = BLOCKS.register("propane_liquid",
            () -> new LiquidBlock(ModFluids.PROPANE_LIQUID.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> VACUUM_GAS_OIL = BLOCKS.register("vacuum_gas_oil",
            () -> new LiquidBlock(ModFluids.VACUUM_GAS_OIL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> VACUUM_RESIDUE = BLOCKS.register("vacuum_residue",
            () -> new LiquidBlock(ModFluids.VACUUM_RESIDUE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> CRACKED_NAPHTHA = BLOCKS.register("cracked_naphtha",
            () -> new LiquidBlock(ModFluids.CRACKED_NAPHTHA.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LIGHT_CYCLE_OIL = BLOCKS.register("light_cycle_oil",
            () -> new LiquidBlock(ModFluids.LIGHT_CYCLE_OIL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> FCC_GAS = BLOCKS.register("fcc_gas",
            () -> new LiquidBlock(ModFluids.FCC_GAS.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> ALKYLATE = BLOCKS.register("alkylate",
            () -> new LiquidBlock(ModFluids.ALKYLATE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> GLYCEROL = BLOCKS.register("glycerol",
            () -> new LiquidBlock(ModFluids.GLYCEROL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> NITROGLYCERIN = BLOCKS.register("nitroglycerin",
            () -> new LiquidBlock(ModFluids.NITROGLYCERIN.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> REFORMATE = BLOCKS.register("reformate",
            () -> new LiquidBlock(ModFluids.REFORMATE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> BENZENE = BLOCKS.register("benzene",
            () -> new LiquidBlock(ModFluids.BENZENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> TOLUENE = BLOCKS.register("toluene",
            () -> new LiquidBlock(ModFluids.TOLUENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> XYLENE = BLOCKS.register("xylene",
            () -> new LiquidBlock(ModFluids.XYLENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LIGHT_SWEET_CRUDE = BLOCKS.register("light_sweet_crude",
            () -> new LiquidBlock(ModFluids.LIGHT_SWEET_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> LIGHT_SOUR_CRUDE = BLOCKS.register("light_sour_crude",
            () -> new LiquidBlock(ModFluids.LIGHT_SOUR_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MEDIUM_SWEET_CRUDE = BLOCKS.register("medium_sweet_crude",
            () -> new LiquidBlock(ModFluids.MEDIUM_SWEET_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MEDIUM_SOUR_CRUDE = BLOCKS.register("medium_sour_crude",
            () -> new LiquidBlock(ModFluids.MEDIUM_SOUR_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> HEAVY_SWEET_CRUDE = BLOCKS.register("heavy_sweet_crude",
            () -> new LiquidBlock(ModFluids.HEAVY_SWEET_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> HEAVY_SOUR_CRUDE = BLOCKS.register("heavy_sour_crude",
            () -> new LiquidBlock(ModFluids.HEAVY_SOUR_CRUDE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> MOLTEN_SULFUR = BLOCKS.register("molten_sulfur",
            () -> new LiquidBlock(ModFluids.MOLTEN_SULFUR.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> SOUR_NAPHTHA = BLOCKS.register("sour_naphtha",
            () -> new LiquidBlock(ModFluids.SOUR_NAPHTHA.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> SOUR_KEROSENE = BLOCKS.register("sour_kerosene",
            () -> new LiquidBlock(ModFluids.SOUR_KEROSENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> ETHYLENE = BLOCKS.register("ethylene",
            () -> new LiquidBlock(ModFluids.ETHYLENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GREEN)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> PROPYLENE = BLOCKS.register("propylene",
            () -> new LiquidBlock(ModFluids.PROPYLENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> BUTADIENE = BLOCKS.register("butadiene",
            () -> new LiquidBlock(ModFluids.BUTADIENE.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> AVIATION_FUEL = BLOCKS.register("aviation_fuel",
            () -> new LiquidBlock(ModFluids.AVIATION_FUEL.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<LiquidBlock> SULFURIC_ACID = BLOCKS.register("sulfuric_acid",
            () -> new LiquidBlock(ModFluids.SULFURIC_ACID.get(), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    .noLootTable()
                    .liquid()
                    .sound(SoundType.EMPTY)
                    .pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CrackingReactorBlock> CRACKING_REACTOR = BLOCKS.register("cracking_reactor",
            () -> new CrackingReactorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(3.0F)
                    .noOcclusion()
                    .isRedstoneConductor((p1, p2, p3) -> true)));

    public static final DeferredBlock<SeparatorBlock> SEPARATOR = BLOCKS.register("separator",
            () -> new SeparatorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0F)
                    .noOcclusion()
                    .isRedstoneConductor((p1, p2, p3) -> true)));

    public static final DeferredBlock<GalvanizedTankBlock> GALVANIZED_TANK = BLOCKS.register("galvanized_reactor_tank",
            () -> new GalvanizedTankBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(3.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .isRedstoneConductor((p1, p2, p3) -> true)));

    public static final DeferredBlock<WearResistantTankBlock> WEAR_RESISTANT_TANK = BLOCKS.register("wear_resistant_tank",
            () -> new WearResistantTankBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .strength(3.0F)
                    .noOcclusion()
                    .sound(SoundType.METAL)
                    .isRedstoneConductor((p1, p2, p3) -> true)));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
