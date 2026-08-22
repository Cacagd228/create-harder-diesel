package com.harderdiesel.content.cracking;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModConfig;
import com.harderdiesel.ModRecipeTypes;
import com.harderdiesel.content.multiblock.ReactorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class CrackingReactorBlockEntity extends ReactorBlockEntity {

    public CrackingReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected int getTankSlotCount() {
        return 6;
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return ModRecipeTypes.CRACKING.getType();
    }

    @Override
    protected float pollutionEmission() {
        return ModConfig.POLLUTION_CRACKING_EMIT.get().floatValue();
    }

    @Override
    protected String emptyHintKey() {
        return "harderdiesel.hint.reactor_empty";
    }

    @Override
    protected String fullHintTitleKey() {
        return "hint.reactor_full.title";
    }

    @Override
    protected String fullHintKey() {
        return "hint.reactor_full";
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerFluidCapability(event, ModBlockEntityTypes.CRACKING_REACTOR.get());
    }
}
