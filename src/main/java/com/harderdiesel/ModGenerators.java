package com.harderdiesel;

import com.harderdiesel.content.generators.FuelCategory;
import com.harderdiesel.content.generators.FueledDieselEngineBlock;
import com.harderdiesel.content.generators.FueledDieselEngineBlockEntity;
import com.harderdiesel.content.generators.FueledDieselEngineMovementBehaviour;
import com.harderdiesel.content.generators.FueledHugeDieselEngineBlock;
import com.harderdiesel.content.generators.FueledHugeDieselEngineBlockEntity;
import com.harderdiesel.content.generators.FueledModularDieselEngineBlock;
import com.harderdiesel.content.generators.FueledModularDieselEngineBlockEntity;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.foundation.data.SharedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.jesz.createdieselgenerators.content.diesel_engine.huge.HugeDieselEngineBlock.FACING;

/**
 * Регистрация генераторов по семействам топлива: для каждой категории
 * (бензин/дизель/газ/нитро) три типоразмера — обычный, модульный и большой.
 */
public class ModGenerators {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HarderDiesel.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HarderDiesel.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BE_TYPES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, HarderDiesel.MODID);

    public static final Map<FuelCategory, Family> FAMILIES = new EnumMap<>(FuelCategory.class);

    static {
        for (FuelCategory category : FuelCategory.values())
            FAMILIES.put(category, new Family(category));
    }

    public static class Family {
        public final FuelCategory category;

        public DeferredHolder<BlockEntityType<?>, BlockEntityType<FueledDieselEngineBlockEntity>> normalBE;
        public DeferredHolder<BlockEntityType<?>, BlockEntityType<FueledModularDieselEngineBlockEntity>> modularBE;
        public DeferredHolder<BlockEntityType<?>, BlockEntityType<FueledHugeDieselEngineBlockEntity>> hugeBE;

        public DeferredBlock<FueledDieselEngineBlock> normal;
        public DeferredBlock<FueledModularDieselEngineBlock> modular;
        public DeferredBlock<FueledHugeDieselEngineBlock> huge;

        public DeferredItem<BlockItem> normalItem;
        public DeferredItem<BlockItem> modularItem;
        public DeferredItem<BlockItem> hugeItem;

        private Family(FuelCategory category) {
            this.category = category;
            String id = category.id;

            normalBE = BE_TYPES.register(id + "_generator", () -> BlockEntityType.Builder
                    .of((BlockPos pos, BlockState state) -> new FueledDieselEngineBlockEntity(normalBE.get(), pos, state),
                            normal.get())
                    .build(null));
            modularBE = BE_TYPES.register(id + "_generator_large", () -> BlockEntityType.Builder
                    .of((BlockPos pos, BlockState state) -> new FueledModularDieselEngineBlockEntity(modularBE.get(), pos, state),
                            modular.get())
                    .build(null));
            hugeBE = BE_TYPES.register(id + "_generator_huge", () -> BlockEntityType.Builder
                    .of((BlockPos pos, BlockState state) -> new FueledHugeDieselEngineBlockEntity(hugeBE.get(), pos, state),
                            huge.get())
                    .build(null));

            normal = BLOCKS.register(id + "_generator", () -> new FueledDieselEngineBlock(
                    category, () -> (BlockEntityType<FueledDieselEngineBlockEntity>) (BlockEntityType<?>) normalBE.get(),
                    () -> normalItem.get().getDefaultInstance(),
                    BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal()).mapColor(category.mapColor)));

            modular = BLOCKS.register("large_" + id + "_generator", () -> new FueledModularDieselEngineBlock(
                    category, () -> (BlockEntityType<FueledModularDieselEngineBlockEntity>) (BlockEntityType<?>) modularBE.get(),
                    () -> modularItem.get().getDefaultInstance(),
                    BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal()).mapColor(category.mapColor)));

            huge = BLOCKS.register("huge_" + id + "_generator", () -> new FueledHugeDieselEngineBlock(
                    category, () -> (BlockEntityType<FueledHugeDieselEngineBlockEntity>) (BlockEntityType<?>) hugeBE.get(),
                    BlockBehaviour.Properties.ofFullCopy(SharedProperties.softMetal())
                            .mapColor(category.mapColor).noOcclusion()));

            normalItem = ITEMS.register(id + "_generator", () -> new BlockItem(normal.get(), new Item.Properties()));
            modularItem = ITEMS.register("large_" + id + "_generator", () -> new BlockItem(modular.get(), new Item.Properties()));
            hugeItem = ITEMS.register("huge_" + id + "_generator", () -> new BlockItem(huge.get(), new Item.Properties()));
        }
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Family family : FAMILIES.values()) {
            registerNormalCapability(event, family.normalBE.get());
            registerModularCapability(event, family.modularBE.get());
            registerHugeCapability(event, family.hugeBE.get());
        }
    }

    private static void registerNormalCapability(RegisterCapabilitiesEvent event,
                                                 BlockEntityType<FueledDieselEngineBlockEntity> type) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type,
                (be, side) -> {
                    if (side == null)
                        return be.getTank();
                    Direction facing = be.getBlockState().getValue(
                            com.jesz.createdieselgenerators.content.diesel_engine.normal.DieselEngineBlock.FACING);
                    if (facing.getAxis().isVertical()) {
                        Direction.Axis portAxis = (facing == Direction.DOWN) ? Direction.Axis.X : Direction.Axis.Z;
                        if (side.getAxis() == portAxis)
                            return be.getTank();
                    } else {
                        if (side == Direction.DOWN)
                            return be.getTank();
                    }
                    return null;
                });
    }

    private static void registerModularCapability(RegisterCapabilitiesEvent event,
                                                  BlockEntityType<FueledModularDieselEngineBlockEntity> type) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type,
                (be, side) -> {
                    if (side == null || (side == Direction.UP && be.getBlockState().getValue(
                            com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlock.PIPE)))
                        return handlerFor(be);
                    return null;
                });
    }

    private static final IFluidHandler EMPTY_TANK = new FluidTank(0);

    private static IFluidHandler handlerFor(FueledModularDieselEngineBlockEntity be) {
        if (be.isController())
            return be.getTank();
        com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlockEntity controller =
                be.getControllerBE();
        return controller != null ? controller.getTank() : EMPTY_TANK;
    }

    private static void registerHugeCapability(RegisterCapabilitiesEvent event,
                                               BlockEntityType<FueledHugeDieselEngineBlockEntity> type) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type,
                (be, side) -> {
                    if (side == null || side.getAxis() != be.getBlockState().getValue(FACING).getAxis())
                        return be.getTank();
                    return null;
                });
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BE_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        modEventBus.addListener(ModGenerators::onRegisterBlocks);
    }

    private static void onRegisterBlocks(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.BLOCK))
            return;
        for (Family family : FAMILIES.values()) {
            MovementBehaviour.REGISTRY.register(family.normal.get(),
                    new FueledDieselEngineMovementBehaviour(() -> family.normalItem.get().getDefaultInstance()));
            MovementBehaviour.REGISTRY.register(family.modular.get(),
                    new FueledDieselEngineMovementBehaviour(() -> family.modularItem.get().getDefaultInstance()));
            MovementBehaviour.REGISTRY.register(family.huge.get(),
                    new FueledDieselEngineMovementBehaviour(() -> family.hugeItem.get().getDefaultInstance()));
        }
    }
}
