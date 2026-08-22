package com.harderdiesel.content.separator;

import com.harderdiesel.ModRecipeTypes;
import com.harderdiesel.content.multiblock.ReactorBlockEntity;
import com.harderdiesel.content.multiblock.ReactorRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.createmod.catnip.data.Iterate;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.ArrayList;
import java.util.List;

public class SeparatorRecipe extends StandardProcessingRecipe<RecipeInput> implements ReactorRecipe {
    public SeparatorRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.SEPARATING, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 7;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>(super.validate());
        if (getFluidResults().size() < 2)
            errors.add("Separator recipes require at least 2 fluid outputs, but got " + getFluidResults().size());
        return errors;
    }

    /**
     * Checks whether the separator's multi-tank holds the fluid ingredient and
     * whether the output blocks above have space, and when {@code simulate} is
     * false also drains the ingredient and fills the results into the blocks
     * above the controller.
     */
    public boolean apply(ReactorBlockEntity be, boolean simulate) {
        IFluidHandler fluidCap = be.getFluidCapability();
        if (!(fluidCap instanceof ReactorBlockEntity.ReactorFluidHandler availableFluids))
            return false;

        BlazeBurnerBlock.HeatLevel heat = be.getHighestHeatLevel();
        if (!getRequiredHeat().testBlazeBurner(heat))
            return false;

        for (boolean simulated : Iterate.trueAndFalse) {
            if (!simulated && simulate)
                return true;

            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

            FluidIngredients:
            for (SizedFluidIngredient fluidIngredient : getFluidIngredients()) {
                int amountRequired = fluidIngredient.amount();

                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (simulated && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                        continue;
                    if (!fluidIngredient.test(fluidStack))
                        continue;
                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                    if (!simulated)
                        fluidStack.shrink(drainedAmount);
                    amountRequired -= drainedAmount;
                    if (amountRequired != 0)
                        continue;
                    extractedFluidsFromTank[tank] += drainedAmount;
                    continue FluidIngredients;
                }

                return false;
            }

            if (!simulated)
                be.onFluidStackChanged();

            if (!applyOutputs(be, simulated))
                return false;
        }

        return true;
    }

    private boolean applyOutputs(ReactorBlockEntity be, boolean simulate) {
        int i = 0;
        for (FluidStack fluidResult : getFluidResults()) {
            if (fluidResult.isEmpty()) {
                i++;
                continue;
            }
            BlockEntity target = be.getLevel().getBlockEntity(be.getBlockPos().above(i + 1));
            if (!(target instanceof ReactorBlockEntity outBe))
                return false;
            if (outBe.getType() != be.getType())
                return false;
            if (!be.isSameMultiBlock(outBe))
                return false;

            if (simulate) {
                int filled = outBe.tankInventory.fill(fluidResult.copy(), IFluidHandler.FluidAction.SIMULATE);
                if (filled < fluidResult.getAmount())
                    return false;
            } else {
                outBe.tankInventory.fill(fluidResult.copy(), IFluidHandler.FluidAction.EXECUTE);
            }
            i++;
        }
        return true;
    }
}
