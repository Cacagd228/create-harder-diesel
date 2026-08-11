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

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
