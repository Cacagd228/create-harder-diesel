package com.harderdiesel.content.multiblock;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * РћР±С‰Р°СЏ Р»РѕРіРёРєР° РјСѓР»СЊС‚РёР±Р»РѕС‡РЅРѕРіРѕ Р±Р°РєР° (РіР°Р»СЊРІР°РЅРёС‡РµСЃРєРёР№ / РёР·РЅРѕСЃРѕСЃС‚РѕР№РєРёР№),
 * СЃР»СѓР¶Р°С‰РµРіРѕ РѕСЃРЅРѕРІРѕР№ РґР»СЏ СЂРµР°РєС‚РѕСЂРѕРІ.
 *
 * @see com.harderdiesel.content.galvanized.GalvanizedTankBlockEntity
 * @see com.harderdiesel.content.wear_resistant.WearResistantTankBlockEntity
 */
public abstract class TankBlockEntity extends SmartBlockEntity
        implements IMultiBlockEntityContainer.Fluid, IHaveGoggleInformation {
    private static final int MAX_WIDTH = 3;

    protected IFluidHandler fluidCapability;
    protected SmartFluidTank tankInventory;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected boolean updateCapability;
    public boolean window;
    public int width;
    protected int height;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

    public TankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        updateConnectivity = false;
        updateCapability = false;
        window = false;
        height = 1;
        width = 1;
        refreshCapability();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    protected SmartFluidTank createInventory() {
        return new SmartFluidTank(getCapacityMultiplier(), this::onFluidStackChanged);
    }

    public void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    @Override
    public void tick() {
        super.tick();
        if (syncCooldown > 0) {
            syncCooldown--;
            if (syncCooldown == 0 && queuedSync)
                sendData();
        }

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition)) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity)
            updateConnectivity();

        if (updateCapability) {
            updateCapability = false;
            refreshCapability();
        }
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;

        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    TankBlockEntity tankAt = ConnectivityHandler.partAt(getType(), level, pos);
                    if (tankAt == null)
                        continue;
                    level.updateNeighbourForOutputSignal(pos, tankAt.getBlockState()
                            .getBlock());
                }
            }
        }

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    @Override
    public TankBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof TankBlockEntity
                && blockEntity.getType() == getType())
            return (TankBlockEntity) blockEntity;
        return null;
    }

    public void applyFluidTankSize(int blocks) {
        tankInventory.setCapacity(blocks * getCapacityMultiplier());
        int overflow = tankInventory.getFluidAmount() - tankInventory.getCapacity();
        if (overflow > 0)
            tankInventory.drain(overflow, IFluidHandler.FluidAction.EXECUTE);
    }

    public void removeController(boolean keepFluids) {
        if (level.isClientSide)
            return;
        updateConnectivity = true;
        if (!keepFluids)
            applyFluidTankSize(1);
        controller = null;
        width = 1;
        height = 1;

        BlockState state = getBlockState();
        if (isOwnBlock(state)) {
            state = state.setValue(TankBlock.BOTTOM, true);
            state = state.setValue(TankBlock.TOP, true);
            state = state.setValue(TankBlock.SHAPE, window ? FluidTankBlock.Shape.WINDOW : FluidTankBlock.Shape.PLAIN);
            level.setBlock(worldPosition, state, 6);
        }

        onFluidStackChanged(tankInventory.getFluid());

        refreshCapability();
        setChanged();
        sendData();
    }

    /** РџСЂРѕРІРµСЂРєР°, С‡С‚Рѕ СЃРѕСЃС‚РѕСЏРЅРёРµ РїСЂРёРЅР°РґР»РµР¶РёС‚ Р±Р»РѕРєСѓ РёРјРµРЅРЅРѕ СЌС‚РѕРіРѕ СЃРµРјРµР№СЃС‚РІР° Р±Р°РєРѕРІ. */
    private boolean isOwnBlock(BlockState state) {
        return getType().isValid(state);
    }

    @Override
    public void sendData() {
        if (syncCooldown > 0) {
            queuedSync = true;
            return;
        }
        super.sendData();
        queuedSync = false;
        syncCooldown = SYNC_RATE;
    }

    @Override
    public void setController(BlockPos controller) {
        if (level.isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;

        refreshCapability();
        setChanged();
        sendData();
    }

    protected void refreshCapability() {
        fluidCapability = handlerForCapability();
        invalidateCapabilities();
    }

    private IFluidHandler handlerForCapability() {
        if (isController())
            return tankInventory;
        TankBlockEntity controllerBE = getControllerBE();
        return controllerBE != null ? controllerBE.handlerForCapability() : tankInventory;
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        TankBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return false;
        IFluidHandler fluids = level.getCapability(Capabilities.FluidHandler.BLOCK, controllerBE.getBlockPos(), null);
        if (fluids == null)
            return false;

        CreateLang.translate("gui.goggles.fluid_container")
                .forGoggles(tooltip);

        boolean isEmpty = true;
        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            CreateLang.text("")
                    .add(CreateLang.fluidName(fluidStack)
                            .add(CreateLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(CreateLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.GOLD)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        if (isEmpty)
            CreateLang.text(" ")
                    .add(Component.translatable("harderdiesel.hint.separator_empty"))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);

        return true;
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = width;
        int prevHeight = height;

        updateConnectivity = tag.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;

        if (tag.contains("LastKnownPos"))
            lastKnownPos = NBTHelper.readBlockPos(tag, "LastKnownPos");
        if (tag.contains("Controller"))
            controller = NBTHelper.readBlockPos(tag, "Controller");

        if (isController()) {
            window = tag.getBoolean("Window");
            width = tag.getInt("Size");
            height = tag.getInt("Height");
            tankInventory.setCapacity(getTotalTankSize() * getCapacityMultiplier());
            tankInventory.readFromNBT(registries, tag.getCompound("TankContent"));
        }

        updateCapability = true;

        if (!clientPacket)
            return;

        boolean changeOfController =
                !Objects.equals(controllerBefore, controller);
        if (changeOfController || prevSize != width || prevHeight != height) {
            if (hasLevel())
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 16);
            if (isController())
                tankInventory.setCapacity(getCapacityMultiplier() * getTotalTankSize());
            invalidateRenderBoundingBox();
        }
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        if (updateConnectivity)
            tag.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            tag.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            tag.put("Controller", NbtUtils.writeBlockPos(getController()));
        if (isController()) {
            tag.putBoolean("Window", window);
            tag.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
            tag.putInt("Size", width);
            tag.putInt("Height", height);
        }

        if (!clientPacket)
            return;
        if (queuedSync)
            tag.putBoolean("LazySync", true);
    }

    protected static <BE extends TankBlockEntity> void registerFluidCapability(RegisterCapabilitiesEvent event, BlockEntityType<BE> type) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                type,
                (BE be, Direction context) -> {
                    if (be.fluidCapability == null)
                        be.refreshCapability();
                    return be.fluidCapability;
                }
        );
    }

    public int getTotalTankSize() {
        return width * width * height;
    }

    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (isOwnBlock(state)) { // safety
            state = state.setValue(TankBlock.BOTTOM, getBottomConnectivity());
            state = state.setValue(TankBlock.TOP, getTopConnectivity());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController()) {
            setWindows(window);
        }
        onFluidStackChanged(tankInventory.getFluid());
        setChanged();
    }

    private boolean getBottomConnectivity() {
        if (level.getBlockEntity(getBlockPos().below()) instanceof TankBlockEntity be)
            return !isSameMultiBlock(be);
        return true;
    }

    private boolean getTopConnectivity() {
        if (level.getBlockEntity(getBlockPos().above()) instanceof TankBlockEntity be)
            return !isSameMultiBlock(be);
        return true;
    }

    @Override
    public void setExtraData(@Nullable Object data) {
        if (data instanceof Boolean)
            window = (boolean) data;
    }

    @Override
    @Nullable
    public Object getExtraData() {
        return window;
    }

    @Override
    public Object modifyExtraData(Object data) {
        if (data instanceof Boolean windows) {
            windows |= window;
            return windows;
        }
        return data;
    }

    @Override
    public Direction.Axis getMainConnectionAxis() {
        return Direction.Axis.Y;
    }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        return width * 4;
    }

    @Override
    public int getMaxWidth() {
        return MAX_WIDTH;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public boolean hasTank() {
        return true;
    }

    @Override
    public int getTankSize(int tank) {
        return getCapacityMultiplier();
    }

    @Override
    public void setTankSize(int tank, int blocks) {
        applyFluidTankSize(blocks);
    }

    @Override
    public IFluidTank getTank(int tank) {
        return tankInventory;
    }

    @Override
    public FluidStack getFluid(int tank) {
        return tankInventory.getFluid()
                .copy();
    }

    public void updateVerticalMulti() {
        BlockState state = this.getBlockState();
        if (isOwnBlock(state)) { // safety
            state = state.setValue(TankBlock.BOTTOM, getBottomConnectivity());
            state = state.setValue(TankBlock.TOP, getTopConnectivity());
            if (state != this.getBlockState())
                level.setBlock(getBlockPos(), state, 3);
        }
        if (level.getBlockEntity(getBlockPos().below()) instanceof TankBlockEntity be)
            be.updateVerticalMulti();
    }

    public boolean isBottom() {
        return !(level.getBlockEntity(getBlockPos().below()) instanceof TankBlockEntity be && isSameMultiBlock(be));
    }

    public boolean isSameMultiBlock(TankBlockEntity be) {
        TankBlockEntity otherControllerBE = be.getControllerBE();
        TankBlockEntity controllerBE = getControllerBE();
        if (otherControllerBE == null || controllerBE == null)
            return false;
        if (otherControllerBE == controllerBE)
            return true;
        if (otherControllerBE.getBlockPos().getX() == controllerBE.getBlockPos().getX()
                && otherControllerBE.getBlockPos().getZ() == controllerBE.getBlockPos().getZ())
            return otherControllerBE.width == controllerBE.width;
        return false;
    }

    public void toggleWindows() {
        TankBlockEntity be = getControllerBE();
        if (be == null)
            return;
        be.setWindows(!be.window);
    }

    public void setWindows(boolean window) {
        this.window = window;
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {

                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!(blockState.getBlock() instanceof TankBlock))
                        continue;

                    FluidTankBlock.Shape shape = FluidTankBlock.Shape.PLAIN;
                    if (window) {

                        if (width == 1)
                            shape = FluidTankBlock.Shape.WINDOW;

                        if (width == 2)
                            shape = xOffset == 0 ? zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NW : FluidTankBlock.Shape.WINDOW_SW
                                    : zOffset == 0 ? FluidTankBlock.Shape.WINDOW_NE : FluidTankBlock.Shape.WINDOW_SE;

                        if (width == 3 && Math.abs(xOffset - zOffset) == 1)
                            shape = FluidTankBlock.Shape.WINDOW;
                    }
                    level.setBlock(pos, blockState.setValue(TankBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
    }
}
