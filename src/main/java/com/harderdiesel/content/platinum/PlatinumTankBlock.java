package com.harderdiesel.content.platinum;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModItems;
import com.harderdiesel.content.multiblock.TankBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class PlatinumTankBlock extends TankBlock<PlatinumTankBlockEntity> {

    public static boolean isTank(BlockState state) {
        return state.getBlock() instanceof PlatinumTankBlock;
    }

    public PlatinumTankBlock(Properties properties) {
        super(properties, ModItems.PLATINUM_TANK);
    }

    @Override
    public Class<PlatinumTankBlockEntity> getBlockEntityClass() {
        return PlatinumTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PlatinumTankBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.PLATINUM_TANK.get();
    }
}
