package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.content.diesel_engine.huge.HugeDieselEngineBlockEntity;
import com.jesz.createdieselgenerators.content.diesel_engine.huge.PoweredEngineShaftBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Большой генератор: бак принимает только топливо категории {@link FuelCategory}.
 */
public class FueledHugeDieselEngineBlockEntity extends HugeDieselEngineBlockEntity {

    public FueledHugeDieselEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        FuelCategory category = category();
        getTank().setValidator(fs -> category.accepts(getLevel(), fs.getFluid()));
    }

    private FuelCategory category() {
        return ((FueledHugeDieselEngineBlock) getBlockState().getBlock()).category;
    }

    @Override
    public Float getTargetAngle() {
        float angle;
        BlockState state = getBlockState();
        if (!(state.getBlock() instanceof FueledHugeDieselEngineBlock))
            return null;

        Direction facing = state.getValue(FueledHugeDieselEngineBlock.FACING);
        PoweredEngineShaftBlockEntity shaft = getShaft();
        Direction.Axis facingAxis = facing.getAxis();
        Direction.Axis axis;

        if (shaft == null)
            return null;

        axis = KineticBlockEntityRenderer.getRotationAxisOf(shaft);
        angle = KineticBlockEntityRenderer.getAngleForBe(shaft, shaft.getBlockPos(), axis);
        if (axis == facingAxis)
            return null;
        if (axis.isHorizontal() && (facingAxis == Direction.Axis.X ^ facing.getAxisDirection() == Direction.AxisDirection.POSITIVE))
            angle *= -1;
        if (axis == Direction.Axis.X && facing == Direction.DOWN)
            angle *= -1;
        return angle;
    }
}
