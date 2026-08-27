package com.harderdiesel.content.multiblock;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Предмет-контроллер, превращающий мультиблочный бак в реактор
 * (крекинг-реактор / сепаратор). Разница между семействами —
 * только минимальная высота, целевой бак/блок и ключи локализации.
 *
 * @see com.harderdiesel.content.cracking.CrackingControllerItem
 * @see com.harderdiesel.content.separator.SeparatorControllerItem
 */
public abstract class ReactorControllerItem extends Item {

    private final Supplier<Integer> minHeight;
    private final Supplier<? extends Block> tankBlock;
    private final Supplier<? extends Block> reactorBlock;
    private final String actionbarKey;

    protected ReactorControllerItem(Properties properties,
                                    Supplier<Integer> minHeight,
                                    Supplier<? extends Block> tankBlock,
                                    Supplier<? extends Block> reactorBlock,
                                    String actionbarKey) {
        super(properties);
        this.minHeight = minHeight;
        this.tankBlock = tankBlock;
        this.reactorBlock = reactorBlock;
        this.actionbarKey = actionbarKey;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!(context.getLevel().getBlockEntity(context.getClickedPos()) instanceof TankBlockEntity obbe
                && context.getLevel().getBlockState(context.getClickedPos()).is(tankBlock.get())))
            return super.useOn(context);
        ItemStack itemInHand = context.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        BlockPos controllerPos = obbe.getController();
        int width = obbe.getControllerBE().getWidth();
        int height = obbe.getControllerBE().getHeight();

        if (height < minHeight.get()) {
            if (context.getPlayer() instanceof ServerPlayer sp)
                sp.connection.send(new ClientboundSetActionBarTextPacket(
                        Component.translatable(actionbarKey + ".too_short", minHeight.get())
                                .withStyle(ChatFormatting.RED)));
            return InteractionResult.FAIL;
        }

        // Сохраняем все слоты исходного бака (multi-tank), не только слот 0
        IFluidHandler tank = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, obbe.getBlockPos(), null);
        List<FluidStack> fluidsToTransfer = new ArrayList<>();
        if (tank != null) {
            for (int i = 0; i < tank.getTanks(); i++) {
                FluidStack fs = tank.getFluidInTank(i);
                if (!fs.isEmpty()) fluidsToTransfer.add(fs.copy());
            }
        }
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
                        Component.translatable(actionbarKey + ".not_enough").withStyle(ChatFormatting.RED)));

            return InteractionResult.FAIL;
        }

        for (BlockPos pos : positions) {
            context.getLevel().setBlock(pos, reactorBlock.get().defaultBlockState(), 3);
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

        if (context.getLevel().getBlockEntity(controllerPos) instanceof ReactorBlockEntity be) {
            be.updateConnectivity();
            be.updateVerticalMulti();
            be.updateTemperature();
            IFluidHandler reactorTank = context.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, controllerPos, null);
            if (reactorTank != null) {
                for (FluidStack fs : fluidsToTransfer) {
                    int filled = reactorTank.fill(fs, IFluidHandler.FluidAction.EXECUTE);
                    if (filled < fs.getAmount() && !context.getLevel().isClientSide) {
                        // Не влезло — предупреждаем, не воидим молча
                        if (context.getPlayer() instanceof ServerPlayer sp)
                            sp.connection.send(new ClientboundSetActionBarTextPacket(
                                    Component.translatable(actionbarKey + ".fluid_overflow").withStyle(ChatFormatting.YELLOW)));
                        break;
                    }
                }
            }
        }

        return InteractionResult.SUCCESS;

    }
}
