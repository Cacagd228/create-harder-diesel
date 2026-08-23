package com.harderdiesel.content.platinum;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.content.multiblock.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class PlatinumTankBlockEntity extends TankBlockEntity {

    /** Платина: удвоенная ёмкость на блок. */
    @Override
    protected float capacityMultiplier() {
        return 2.0f;
    }

    public PlatinumTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerFluidCapability(event, ModBlockEntityTypes.PLATINUM_TANK.get());
    }
}
