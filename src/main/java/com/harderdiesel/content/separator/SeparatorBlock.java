package com.harderdiesel.content.separator;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModBlocks;
import com.harderdiesel.ModItems;
import com.harderdiesel.content.multiblock.ReactorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SeparatorBlock extends ReactorBlock<SeparatorBlockEntity> {

    public static boolean isSeparator(BlockState state) {
        return state.getBlock() instanceof SeparatorBlock;
    }

    public SeparatorBlock(Properties properties) {
        super(properties, ModBlocks.WEAR_RESISTANT_TANK, ModItems.SEPARATOR_CONTROLLER);
    }

    @Override
    public Class<SeparatorBlockEntity> getBlockEntityClass() {
        return SeparatorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SeparatorBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.SEPARATOR.get();
    }
}
