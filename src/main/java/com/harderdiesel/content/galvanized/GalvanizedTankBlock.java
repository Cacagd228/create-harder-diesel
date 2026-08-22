package com.harderdiesel.content.galvanized;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModItems;
import com.harderdiesel.content.multiblock.TankBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GalvanizedTankBlock extends TankBlock<GalvanizedTankBlockEntity> {

    public static boolean isTank(BlockState state) {
        return state.getBlock() instanceof GalvanizedTankBlock;
    }

    public GalvanizedTankBlock(Properties properties) {
        super(properties, ModItems.GALVANIZED_TANK);
    }

    @Override
    public Class<GalvanizedTankBlockEntity> getBlockEntityClass() {
        return GalvanizedTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GalvanizedTankBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.GALVANIZED_TANK.get();
    }
}
