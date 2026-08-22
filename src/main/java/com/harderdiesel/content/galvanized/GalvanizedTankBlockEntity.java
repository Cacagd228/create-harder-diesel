package com.harderdiesel.content.galvanized;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.content.multiblock.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class GalvanizedTankBlockEntity extends TankBlockEntity {

    public GalvanizedTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerFluidCapability(event, ModBlockEntityTypes.GALVANIZED_TANK.get());
    }
}
