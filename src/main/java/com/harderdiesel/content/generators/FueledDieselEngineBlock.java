package com.harderdiesel.content.generators;

import com.jesz.createdieselgenerators.CDGConfig;
import com.jesz.createdieselgenerators.content.diesel_engine.EngineUpgrades;
import com.jesz.createdieselgenerators.content.diesel_engine.normal.DieselEngineBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.context.UseOnContext;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Обычный генератор, покрашенный в цвет семейства топлива. Принимает в бак
 * только жидкости из категории {@link FuelCategory}.
 */
public class FueledDieselEngineBlock extends DieselEngineBlock {
    public final FuelCategory category;
    private final Supplier<BlockEntityType<? extends FueledDieselEngineBlockEntity>> blockEntityType;
    private final Supplier<ItemStack> blockItem;

    public FueledDieselEngineBlock(FuelCategory category,
                                   Supplier<BlockEntityType<? extends FueledDieselEngineBlockEntity>> blockEntityType,
                                   Supplier<ItemStack> blockItem, Properties properties) {
        super(properties);
        this.category = category;
        this.blockEntityType = blockEntityType;
        this.blockItem = blockItem;
    }

    @Override
    public BlockEntityType<? extends FueledDieselEngineBlockEntity> getBlockEntityType() {
        return blockEntityType.get();
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        List<ItemStack> list = new ArrayList<>();
        list.add(blockItem.get());
        if (blockEntity instanceof FueledDieselEngineBlockEntity be) {
            ItemStack upgradeItem = be.getUpgrade().getItem();
            if (!upgradeItem.isEmpty())
                list.add(upgradeItem);
        }
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, list);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                              Player player, InteractionHand hand, BlockHitResult hitResult) {
        for (EngineUpgrades upgrade : EngineUpgrades.allUpgrades) {
            if (upgrade == EngineUpgrades.EMPTY)
                continue;
            if (upgrade.getItem().is(stack.getItem())) {
                withBlockEntityDo(level, pos, be -> {
                    if (!upgrade.canAddOn(be))
                        return;
                    if (be.getUpgrade() != EngineUpgrades.EMPTY)
                        return;
                    if (!player.isCreative())
                        stack.shrink(1);
                    be.setUpgrade(upgrade);
                    IWrenchable.playRotateSound(level, pos);
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
}
