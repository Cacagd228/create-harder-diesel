package com.harderdiesel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;

class TarSourceFluid extends BaseFlowingFluid.Source {
    TarSourceFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSpreadTo(BlockGetter level, BlockPos pos, BlockState state, Direction direction, BlockPos toPos, BlockState toState, FluidState toFluidState, Fluid fluid) {
        return false;
    }
}

class TarFlowingFluid extends BaseFlowingFluid.Flowing {
    TarFlowingFluid(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSpreadTo(BlockGetter level, BlockPos pos, BlockState state, Direction direction, BlockPos toPos, BlockState toState, FluidState toFluidState, Fluid fluid) {
        return false;
    }
}
