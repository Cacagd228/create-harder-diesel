package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.CDGConfig;
import com.jesz.createdieselgenerators.content.diesel_engine.EngineUpgrades;
import com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlock;
import com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlockEntity;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.placement.PoleHelper;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Модульный (multi-block) генератор, покрашенный в цвет семейства топлива.
 * Принимает в бак только жидкости из категории {@link FuelCategory}.
 */
public class FueledModularDieselEngineBlock extends ModularDieselEngineBlock {
    public final FuelCategory category;
    private final Supplier<BlockEntityType<? extends FueledModularDieselEngineBlockEntity>> blockEntityType;
    private final Supplier<ItemStack> blockItem;
    private final int placementHelperId;

    public FueledModularDieselEngineBlock(FuelCategory category,
                                          Supplier<BlockEntityType<? extends FueledModularDieselEngineBlockEntity>> blockEntityType,
                                          Supplier<ItemStack> blockItem, Properties properties) {
        super(properties);
        this.category = category;
        this.blockEntityType = blockEntityType;
        this.blockItem = blockItem;
        this.placementHelperId = PlacementHelpers.register(new FueledPlacementHelper(this));
    }

    @Override
    public BlockEntityType<? extends FueledModularDieselEngineBlockEntity> getBlockEntityType() {
        return blockEntityType.get();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        List<ItemStack> list = List.of(blockItem.get());
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, list);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (!player.isShiftKeyDown() && player.mayBuild()) {
            if (placementHelper.matchesItem(stack)) {
                placementHelper.getOffset(player, level, state, pos, hitResult)
                        .placeInWorld(level, (net.minecraft.world.item.BlockItem) stack.getItem(), player, hand, hitResult);
                return ItemInteractionResult.SUCCESS;
            }
        }

        for (EngineUpgrades upgrade : EngineUpgrades.allUpgrades) {
            if (upgrade == EngineUpgrades.EMPTY)
                continue;
            if (upgrade.getItem().is(stack.getItem())) {
                withBlockEntityDo(level, pos, be -> {
                    ModularDieselEngineBlockEntity controller = be.getControllerBE();
                    if (controller == null || !upgrade.canAddOn(be) || controller.getUpgrade() != EngineUpgrades.EMPTY)
                        return;
                    if (!player.isCreative())
                        stack.shrink(1);
                    be.setUpgrade(upgrade);
                    IWrenchable.playRotateSound(level, pos);
                    controller.sendData();
                });
                return ItemInteractionResult.SUCCESS;
            }
        }
        if (!CDGConfig.ENGINES_FILLED_WITH_ITEMS.get() || stack.isEmpty() || !(level.getBlockEntity(pos) instanceof SmartBlockEntity be))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        IFluidHandler tank = level.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
        if (tank == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (stack.getItem() instanceof BucketItem || stack.getItem() instanceof MilkBucketItem) {
            Fluid fluid = stack.getItem() instanceof BucketItem bi ? bi.content : NeoForgeMod.MILK.get();

            if (!tank.getFluidInTank(0).isEmpty())
                return ItemInteractionResult.FAIL;
            if (!tank.isFluidValid(0, new FluidStack(fluid, 1000)))
                return ItemInteractionResult.FAIL;

            tank.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
            if (!player.isCreative())
                player.setItemInHand(hand, new ItemStack(Items.BUCKET));

            return ItemInteractionResult.SUCCESS;
        }

        IFluidHandlerItem itemTank = Capabilities.FluidHandler.ITEM.getCapability(stack, null);
        if (itemTank == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        itemTank.drain(tank.fill(itemTank.getFluidInTank(0), IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if (pContext.getPlayer() != null && pContext.getPlayer().isShiftKeyDown())
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
        else
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    private static class FueledPlacementHelper extends PoleHelper<Direction> {
        private final FueledModularDieselEngineBlock block;

        public FueledPlacementHelper(FueledModularDieselEngineBlock block) {
            super(s -> s.getBlock() == block, state -> state.getValue(FACING).getAxis(), FACING);
            this.block = block;
        }

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return stack -> stack.is(block.asItem());
        }
    }
}
