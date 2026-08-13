package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.CDGConfig;
import com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlockEntity;
import com.jesz.createdieselgenerators.content.diesel_engine.normal.DieselEngineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Модульный генератор: бак принимает только топливо категории {@link FuelCategory},
 * а соседние сегменты соединяются в мультиблок только внутри одного семейства.
 */
public class FueledModularDieselEngineBlockEntity extends ModularDieselEngineBlockEntity {

    public FueledModularDieselEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        FuelCategory category = category();
        tankInventory.setValidator(fs -> category.accepts(getLevel(), fs.getFluid()));
    }

    private FuelCategory category() {
        return ((FueledModularDieselEngineBlock) getBlockState().getBlock()).category;
    }

    @Override
    public boolean enabled() {
        if (!validFS())
            return false;
        if (CDGConfig.ANALOG_SPEED_CONTROL.get())
            return true;
        if (!CDGConfig.ENGINES_DISABLED_WITH_REDSTONE.get())
            return true;
        if (getBlockState().getValue(DieselEngineBlock.POWERED))
            return false;
        for (int i = 1; i < length; i++) {
            BlockState state = level.getBlockState(getBlockPos().relative(getMainConnectionAxis(), i));
            if (state.getBlock() instanceof FueledModularDieselEngineBlock)
                if (state.getValue(DieselEngineBlock.POWERED))
                    return false;
        }
        return true;
    }
}
