package com.harderdiesel.content.separator;

import com.harderdiesel.ModRecipeTypes;
import com.harderdiesel.content.multiblock.ReactorBlockEntity;
import com.harderdiesel.content.multiblock.ReactorRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
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
     * Atomic: EXECUTE never happens if outputs don't fit.
     */
    public boolean apply(ReactorBlockEntity be, boolean simulate) {
        IFluidHandler fluidCap = be.getFluidCapability();
        if (!(fluidCap instanceof ReactorBlockEntity.ReactorFluidHandler availableFluids))
            return false;
        if (be.getLevel() == null) return false;
        BlazeBurnerBlock.HeatLevel heat = be.getHighestHeatLevel();
        if (!getRequiredHeat().testBlazeBurner(heat))
            return false;

        if (!checkIngredients(availableFluids)) return false;
        if (!applyOutputs(be, true)) return false;
        if (simulate) return true;

        drainIngredients(availableFluids);
        be.onFluidStackChanged();
        if (!applyOutputs(be, false)) return false;
        return true;
    }

    private boolean checkIngredients(ReactorBlockEntity.ReactorFluidHandler availableFluids) {
        int[] extracted = new int[availableFluids.getTanks()];
        FluidIngredients:
        for (SizedFluidIngredient ing : getFluidIngredients()) {
            int need = ing.amount();
            for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                FluidStack stack = availableFluids.getFluidInTank(tank);
                int available = stack.getAmount() - extracted[tank];
                if (available <= 0) continue;
                if (!ing.test(stack)) continue;
                int use = Math.min(need, available);
                extracted[tank] += use;
                need -= use;
                if (need == 0) continue FluidIngredients;
            }
            return false;
        }
        return true;
    }

    private void drainIngredients(ReactorBlockEntity.ReactorFluidHandler availableFluids) {
        int[] extracted = new int[availableFluids.getTanks()];
        for (SizedFluidIngredient ing : getFluidIngredients()) {
            int need = ing.amount();
            for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                FluidStack stack = availableFluids.getFluidInTank(tank);
                int available = stack.getAmount() - extracted[tank];
                if (available <= 0) continue;
                if (!ing.test(stack)) continue;
                int use = Math.min(need, available);
                stack.shrink(use);
                extracted[tank] += use;
                need -= use;
                if (need == 0) break;
            }
        }
    }

    private boolean applyOutputs(ReactorBlockEntity be, boolean simulate) {
        if (be.getLevel() == null) return false;
        int i = 0;
        for (FluidStack fluidResult : getFluidResults()) {
            if (fluidResult.isEmpty()) {
                i++;
                continue;
            }
            net.minecraft.core.BlockPos outPos = be.getBlockPos().above(i + 1);
            if (!be.getLevel().isLoaded(outPos)) return false;
            BlockEntity target = be.getLevel().getBlockEntity(outPos);
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
                int filled = outBe.tankInventory.fill(fluidResult.copy(), IFluidHandler.FluidAction.EXECUTE);
                if (filled < fluidResult.getAmount()) return false;
            }
            i++;
        }
        return true;
    }
}
