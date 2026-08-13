package com.harderdiesel;

import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HarderDiesel.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrackingReactorBlockEntity>> CRACKING_REACTOR =
            BLOCK_ENTITY_TYPES.register("cracking_reactor", () -> createReactorType());

    private static BlockEntityType<CrackingReactorBlockEntity> createReactorType() {
        BlockEntityType<CrackingReactorBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createReactor, ModBlocks.CRACKING_REACTOR.get())
                        .build(null);
        return type;
    }

    private static CrackingReactorBlockEntity createReactor(BlockPos pos, BlockState state) {
        return new CrackingReactorBlockEntity(CRACKING_REACTOR.get(), pos, state);
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
