package com.harderdiesel.content.separator;

import com.harderdiesel.ModBlocks;
import com.harderdiesel.ModConfig;
import com.harderdiesel.content.wear_resistant.WearResistantTankBlockEntity;
import com.simibubi.create.AllSoundEvents;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class SeparatorControllerItem extends Item {
    public SeparatorControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof WearResistantTankBlockEntity obbe
                && context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.WEAR_RESISTANT_TANK.get())))
            return super.useOn(context);
        ItemStack itemInHand = context.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        BlockPos controllerPos = obbe.getController();
        int width = obbe.getControllerBE().getWidth();
        int height = obbe.getControllerBE().getHeight();

        if (height < ModConfig.SEPARATOR_MIN_HEIGHT.get()) {
            if (context.getPlayer() instanceof ServerPlayer sp)
                sp.connection.send(new ClientboundSetActionBarTextPacket(
                        Component.translatable("harderdiesel.actionbar.separator_controller.too_short",
                                        ModConfig.SEPARATOR_MIN_HEIGHT.get())
                                .withStyle(ChatFormatting.RED)));
            return InteractionResult.FAIL;
        }

        IFluidHandler tank = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, obbe.getBlockPos(), null);
        FluidStack fluidInTank = tank.getFluidInTank(0);
        List<BlockPos> positions = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int z = 0; z < width; z++) {
                for (int x = 0; x < width; x++) {
                    if (positions.size() >= itemInHand.getCount() && !context.getPlayer().isCreative())
                        break;
                    BlockPos currentPos = controllerPos.offset(x, y, z);
                    if (com.simibubi.create.api.connectivity.ConnectivityHandler.isConnected(context.getLevel(), controllerPos, currentPos))
                        positions.add(currentPos);
                }
            }
        }

        if (!context.getPlayer().isCreative() && width * width * height > itemInHand.getCount()) {
            if (context.getPlayer() instanceof ServerPlayer sp)
                sp.connection.send(new ClientboundSetActionBarTextPacket(
                        Component.translatable("harderdiesel.actionbar.separator_controller.not_enough").withStyle(ChatFormatting.RED)));

            return InteractionResult.FAIL;
        }

        for (BlockPos pos : positions) {
            context.getLevel().setBlock(pos, ModBlocks.SEPARATOR.get().defaultBlockState(), 3);
            if (context.getLevel().isClientSide) {
                for (int i = 0; i < 30; i++) {
                    Vec3 offset = VecHelper.offsetRandomly(VecHelper.getCenterOf(pos), context.getLevel().getRandom(), .3f);
                    Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, context.getLevel().getRandom(), .1f);
                    context.getLevel().addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemInHand), offset.x(), offset.y(),
                            offset.z(), motion.x(), motion.y(), motion.z());
                }
            }
        }
        AllSoundEvents.WRENCH_ROTATE.playAt(context.getLevel(), controllerPos.getX() + (double) width / 2, controllerPos.getY() + (double) height / 2, controllerPos.getZ() + (double) width / 2, 2f, 1f, false);

        if (!context.getPlayer().isCreative() && !context.getLevel().isClientSide) {
            itemInHand.shrink(positions.size());
        }

        if (context.getLevel().getBlockEntity(controllerPos) instanceof SeparatorBlockEntity be) {
            be.updateConnectivity();
            be.updateVerticalMulti();
            be.updateTemperature();
            IFluidHandler separatorTank = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, controllerPos, null);
            if (separatorTank != null)
                separatorTank.fill(fluidInTank, IFluidHandler.FluidAction.EXECUTE);
        }

        return InteractionResult.SUCCESS;

    }
}
