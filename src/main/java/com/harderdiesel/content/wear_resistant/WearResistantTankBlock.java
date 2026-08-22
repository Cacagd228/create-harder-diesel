package com.harderdiesel.content.wear_resistant;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModItems;
import com.harderdiesel.content.multiblock.TankBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class WearResistantTankBlock extends TankBlock<WearResistantTankBlockEntity> {

    public static boolean isTank(BlockState state) {
        return state.getBlock() instanceof WearResistantTankBlock;
    }

    public WearResistantTankBlock(Properties properties) {
        super(properties, ModItems.WEAR_RESISTANT_TANK);
    }

    @Override
    public Class<WearResistantTankBlockEntity> getBlockEntityClass() {
        return WearResistantTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WearResistantTankBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.WEAR_RESISTANT_TANK.get();
    }
}
