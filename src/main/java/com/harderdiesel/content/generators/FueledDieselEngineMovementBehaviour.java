package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.contraption.DieselEngineMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/** Поведение на контрапциях (поездах) для генераторов Harder Diesel. */
public class FueledDieselEngineMovementBehaviour extends DieselEngineMovementBehaviour {
    private final Supplier<ItemStack> blockItem;

    public FueledDieselEngineMovementBehaviour(Supplier<ItemStack> blockItem) {
        this.blockItem = blockItem;
    }

    @Override
    @Nullable
    public ItemStack canBeDisabledVia(MovementContext context) {
        return blockItem.get();
    }
}
