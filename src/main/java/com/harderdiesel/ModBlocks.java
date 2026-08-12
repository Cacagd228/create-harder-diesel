package com.harderdiesel;

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

    public static final DeferredBlock<LiquidBlock> HEAVY_OIL = BLOCKS.register("heavy_oil",
            () -> new LiquidBlock(ModFluids.HEAVY_OIL.get(), BlockBehaviour.Properties.of()
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

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
