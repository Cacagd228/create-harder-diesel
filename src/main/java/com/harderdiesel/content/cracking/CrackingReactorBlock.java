package com.harderdiesel.content.cracking;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModBlocks;
import com.harderdiesel.ModItems;
import com.harderdiesel.content.multiblock.ReactorBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CrackingReactorBlock extends ReactorBlock<CrackingReactorBlockEntity> {

    public static boolean isReactor(BlockState state) {
        return state.getBlock() instanceof CrackingReactorBlock;
    }

    public CrackingReactorBlock(Properties properties) {
        super(properties, ModBlocks.GALVANIZED_TANK, ModItems.CRACKING_CONTROLLER);
    }

    @Override
    public Class<CrackingReactorBlockEntity> getBlockEntityClass() {
        return CrackingReactorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CrackingReactorBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.CRACKING_REACTOR.get();
    }
}
