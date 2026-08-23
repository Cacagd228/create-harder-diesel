package com.harderdiesel;

import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;
import com.harderdiesel.content.galvanized.GalvanizedTankBlockEntity;
import com.harderdiesel.content.platinum.PlatinumTankBlockEntity;
import com.harderdiesel.content.separator.SeparatorBlockEntity;
import com.harderdiesel.content.wear_resistant.WearResistantTankBlockEntity;
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SeparatorBlockEntity>> SEPARATOR =
            BLOCK_ENTITY_TYPES.register("separator", () -> createSeparatorType());

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GalvanizedTankBlockEntity>> GALVANIZED_TANK =
            BLOCK_ENTITY_TYPES.register("galvanized_reactor_tank", () -> createTankType());

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WearResistantTankBlockEntity>> WEAR_RESISTANT_TANK =
            BLOCK_ENTITY_TYPES.register("wear_resistant_tank", () -> createWearResistantTankType());

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlatinumTankBlockEntity>> PLATINUM_TANK =
            BLOCK_ENTITY_TYPES.register("platinum_tank", () -> createPlatinumTankType());

    private static BlockEntityType<CrackingReactorBlockEntity> createReactorType() {
        BlockEntityType<CrackingReactorBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createReactor, ModBlocks.CRACKING_REACTOR.get())
                        .build(null);
        return type;
    }

    private static CrackingReactorBlockEntity createReactor(BlockPos pos, BlockState state) {
        return new CrackingReactorBlockEntity(CRACKING_REACTOR.get(), pos, state);
    }

    private static BlockEntityType<SeparatorBlockEntity> createSeparatorType() {
        BlockEntityType<SeparatorBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createSeparator, ModBlocks.SEPARATOR.get())
                        .build(null);
        return type;
    }

    private static SeparatorBlockEntity createSeparator(BlockPos pos, BlockState state) {
        return new SeparatorBlockEntity(SEPARATOR.get(), pos, state);
    }

    private static BlockEntityType<GalvanizedTankBlockEntity> createTankType() {
        BlockEntityType<GalvanizedTankBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createTank, ModBlocks.GALVANIZED_TANK.get())
                        .build(null);
        return type;
    }

    private static GalvanizedTankBlockEntity createTank(BlockPos pos, BlockState state) {
        return new GalvanizedTankBlockEntity(GALVANIZED_TANK.get(), pos, state);
    }

    private static BlockEntityType<WearResistantTankBlockEntity> createWearResistantTankType() {
        BlockEntityType<WearResistantTankBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createWearResistantTank, ModBlocks.WEAR_RESISTANT_TANK.get())
                        .build(null);
        return type;
    }

    private static WearResistantTankBlockEntity createWearResistantTank(BlockPos pos, BlockState state) {
        return new WearResistantTankBlockEntity(WEAR_RESISTANT_TANK.get(), pos, state);
    }

    private static BlockEntityType<PlatinumTankBlockEntity> createPlatinumTankType() {
        BlockEntityType<PlatinumTankBlockEntity> type =
                BlockEntityType.Builder.of(ModBlockEntityTypes::createPlatinumTank, ModBlocks.PLATINUM_TANK.get())
                        .build(null);
        return type;
    }

    private static PlatinumTankBlockEntity createPlatinumTank(BlockPos pos, BlockState state) {
        return new PlatinumTankBlockEntity(PLATINUM_TANK.get(), pos, state);
    }

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
