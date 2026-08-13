package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.content.diesel_engine.normal.DieselEngineBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Обычный генератор с ограничением принимаемого топлива: в бак можно залить
 * только жидкости из категории {@link FuelCategory}.
 */
public class FueledDieselEngineBlockEntity extends DieselEngineBlockEntity {

    public FueledDieselEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        FuelCategory category = category();
        getTank().setValidator(fs -> category.accepts(getLevel(), fs.getFluid()));
    }

    private FuelCategory category() {
        return ((FueledDieselEngineBlock) getBlockState().getBlock()).category;
    }
}
