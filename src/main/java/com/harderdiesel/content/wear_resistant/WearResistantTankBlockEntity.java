package com.harderdiesel.content.wear_resistant;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.content.multiblock.TankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class WearResistantTankBlockEntity extends TankBlockEntity {

    public WearResistantTankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerFluidCapability(event, ModBlockEntityTypes.WEAR_RESISTANT_TANK.get());
    }
}
