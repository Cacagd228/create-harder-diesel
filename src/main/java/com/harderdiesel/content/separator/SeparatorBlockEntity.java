package com.harderdiesel.content.separator;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModConfig;
import com.harderdiesel.ModRecipeTypes;
import com.harderdiesel.content.multiblock.ReactorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class SeparatorBlockEntity extends ReactorBlockEntity {

    public SeparatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected int getTankSlotCount() {
        return 7;
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return ModRecipeTypes.SEPARATING.getType();
    }

    @Override
    protected float pollutionEmission() {
        try {
            float emit = ModConfig.POLLUTION_SEPARATOR_EMIT.get().floatValue();
            if (Math.abs(emit - 2.0F) < 0.001F)
                emit = 0.0008F;
            if (Math.abs(emit - 0.25F) < 0.001F)
                emit = 0.0008F;
            return emit;
        } catch (Throwable t) {
            return 0.0008F;
        }
    }

    @Override
    protected String emptyHintKey() {
        return "harderdiesel.hint.separator_empty";
    }

    @Override
    protected String fullHintTitleKey() {
        return "hint.separator_full.title";
    }

    @Override
    protected String fullHintKey() {
        return "hint.separator_full";
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerFluidCapability(event, ModBlockEntityTypes.SEPARATOR.get());
    }
}
