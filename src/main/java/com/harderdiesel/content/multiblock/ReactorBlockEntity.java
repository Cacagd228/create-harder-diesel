package com.harderdiesel.content.multiblock;

import com.harderdiesel.HarderDiesel;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.CreateLang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Общая логика мультиблочного реактора (крекинг-реактор, сепаратор):
 * сборка структуры, мульти-танк, обработка рецепта, загрязнение.
 *
 * @see com.harderdiesel.content.cracking.CrackingReactorBlockEntity
 * @see com.harderdiesel.content.separator.SeparatorBlockEntity
 */
public abstract class ReactorBlockEntity extends SmartBlockEntity implements IMultiBlockEntityContainer.Fluid, IHaveGoggleInformation, IHaveHoveringInformation {
    private static final int MAX_SIZE = 3;

    public float progress;
    protected IFluidHandler fluidCapability;
    protected boolean forceFluidLevelUpdate;
    public ReactorFluidHandler tankInventory;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected boolean updateCapability;
    public boolean window;
    protected int luminosity;
    public int width;
    protected int height;
    boolean tanksFull = false;

    private static final int SYNC_RATE = 8;
    protected int syncCooldown;
    protected boolean queuedSync;

    // For rendering purposes only
    private LerpedFloat fluidLevel;
    BlazeBurnerBlock.HeatLevel highestHeatLevel = BlazeBurnerBlock.HeatLevel.NONE;
    int numberOfHighestHeat = 0;

    public ReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        tankInventory = createInventory();
        forceFluidLevelUpdate = true;
        updateConnectivity = false;
        updateCapability = false;
        window = false;
        height = 1;
        width = 1;
        refreshCapability();
    }

    /** Количество слотов мульти-танка (у крекинга 6, у сепаратора 7). */
    protected abstract int getTankSlotCount();

    /** Тип рецептов, обрабатываемых этим реактором. */
    protected abstract RecipeType<?> getRecipeType();

    /**
     * Загрязнение за тик активной обработки. Читается из конфига напрямую —
     * миграция legacy-дефолтов выполняется один раз при загрузке конфига
     * (см. {@code ModConfig.migrateLegacyEmissionDefaults}).
     */
    protected abstract float pollutionEmission();

    protected abstract String emptyHintKey();

    protected abstract String fullHintTitleKey();

    protected abstract String fullHintKey();

    protected ReactorFluidHandler createInventory() {
        return new ReactorFluidHandler(getTankSlotCount(), getCapacityMultiplier(), this::onFluidStackChanged);
    }

    public BlazeBurnerBlock.HeatLevel getHeat() {
        int width = getControllerBE().width;
        BlazeBurnerBlock.HeatLevel highestHeat = BlazeBurnerBlock.HeatLevel.NONE;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos pos = getController().offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(blockState);
                if (!highestHeat.isAtLeast(heat))
                    highestHeat = heat;
            }
        }
        return highestHeat;
    }

    // counts the number of burners if an EXACT heat level
    public int getNumberOfHeat(BlazeBurnerBlock.HeatLevel heatTest) {
        int width = getControllerBE().width;
        int heatCount = 0;

        for (int xOffset = 0; xOffset < width; xOffset++) {
            for (int zOffset = 0; zOffset < width; zOffset++) {
                BlockPos pos = getController().offset(xOffset, -1, zOffset);
                BlockState blockState = level.getBlockState(pos);
                BlazeBurnerBlock.HeatLevel heat = BasinBlockEntity.getHeatLevelOf(blockState);
                if (heatTest == heat)
                    heatCount++;
            }
        }
        return heatCount;
    }

    public void updateConnectivity() {
        updateConnectivity = false;
        if (level.isClientSide)
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
    }

    public int processingTime = -1;
    public ReactorRecipe currentRecipe;

    private void startProcessing() {
        if (currentRecipe == null)
            return;
        processingTime = currentRecipe.getProcessingDuration();
        if (!level.isClientSide)
            sendData();
    }

    @Override
    public void tick() {
        boolean prevTanksFull = tanksFull;
        tanksFull = false;
        if (isController() && isBottom()) {
            if (processingTime >= 0 && currentRecipe == null) {
                List<Recipe<?>> r = getMatchingRecipes();
                if (!r.isEmpty())
                    currentRecipe = (ReactorRecipe) r.get(0);
            }

            if (processingTime > -1 && currentRecipe != null) {
                // emit every tick while reactor has active processingTime (even if apply fails next tick)
                if (!level.isClientSide && processingTime >= 0) {
                    float emit = pollutionEmission();
                    if (emit > 0)
                        com.harderdiesel.content.pollution.PollutionManager.emit(level, worldPosition, emit);
                }
                if (currentRecipe.apply(this, true)) {
                    processingTime--;
                } else {
                    tanksFull = true;
                }

                if (!(hasIngredients() && currentRecipe.getRequiredHeat().testBlazeBurner(highestHeatLevel))) {
                    currentRecipe = null;
                    processingTime = -1;
                }
            }
            if (processingTime == 0 && currentRecipe != null) {
                if (!level.isClientSide) {
                    level.playSound(null,
                            getBlockPos().offset(width / 2, height / 2, width / 2),
                            SoundEvents.BREWING_STAND_BREW,
                            SoundSource.BLOCKS, 0.05f, 0.5f);

                    level.playSound(null,
                            getBlockPos().offset(width / 2, height / 2, width / 2),
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.BLOCKS, 0.05f, 0.5f);
                }

                if (currentRecipe.getRequiredHeat().testBlazeBurner(highestHeatLevel)) {
                    for (int j = 0; j < numberOfHighestHeat; j++) {
                        if (hasIngredients()) {
                            drainIngredients();
                        } else {
                            tanksFull = true;
                        }
                    }
                }

                currentRecipe = null;
                processingTime = -1;
                if (isController() && isBottom())
                    checkForRecipes();
            }

            progress = currentRecipe != null ? (float) processingTime / (currentRecipe.getProcessingDuration()) : 0;
        }

        super.tick();

        if (tanksFull != prevTanksFull)
            sendData();

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
        if (fluidLevel != null)
            fluidLevel.tickChaser();

        if (level.isClientSide)
            CatnipServices.PLATFORM.executeOnClientOnly(() -> this::tickClient);
    }

    protected void tickClient() {
        IFluidHandler cap = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null);
        if (cap instanceof ReactorFluidHandler handler) {
            float fill = fillStateOf(handler);
            if (fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(fill);
            fluidLevel.chase(fill, .5f, LerpedFloat.Chaser.EXP);
        }
    }

    protected float fillStateOf(ReactorFluidHandler handler) {
        for (int i = 0; i < handler.getTanks(); i++) {
            FluidStack stack = handler.getFluidInTank(i);
            if (stack.isEmpty())
                continue;
            int capacity = handler.getTankCapacity(i);
            return capacity == 0 ? 0 : (float) stack.getAmount() / capacity;
        }
        return 0;
    }

    protected boolean hasIngredients() {
        if (currentRecipe == null)
            return false;
        return currentRecipe.apply(this, true);
    }

    protected void drainIngredients() {
        if (currentRecipe == null)
            return;
        currentRecipe.apply(this, false);
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null ||
                worldPosition.getX() == controller.getX() &&
                worldPosition.getY() == controller.getY() &&
                worldPosition.getZ() == controller.getZ();
    }

    @Override
    public void initialize() {
        super.initialize();
        updateTemperature();
        sendData();
        if (level.isClientSide)
            invalidateRenderBoundingBox();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    protected List<Recipe<?>> getMatchingRecipes() {
        List<RecipeHolder<? extends Recipe<?>>> list = RecipeFinder.get(getRecipeCacheKey(), level,
                recipe -> recipe.value().getType() == getRecipeType());
        return list.stream()
                .map(RecipeHolder::value)
                .sorted((r1, r2) -> {
                    if (r1 instanceof ReactorRecipe recipe1 && r2 instanceof ReactorRecipe recipe2)
                        return recipe2.getRequiredHeat().ordinal() - recipe1.getRequiredHeat().ordinal();
                    return 0;
                })
                .filter(r -> {
                    if (r instanceof ReactorRecipe recipe) {
                        if (!recipe.getRequiredHeat().testBlazeBurner(highestHeatLevel))
                            return false;
                        return recipe.apply(this, true);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    Object getRecipeCacheKey() {
        // Уникальный ключ на конкретный класс BE, иначе кэш RecipeFinder
        // смешает рецепты крекинга и сепаратора.
        return getClass();
    }

    public void onFluidStackChanged() {
        if (!hasLevel())
            return;
        ReactorBlockEntity controllerBE = getControllerBE();
        if (controllerBE != null)
            controllerBE.checkForRecipes();
        if (!level.isClientSide) {
            setChanged();
            sendData();
        }
    }

    protected void onFluidStackChanged(FluidStack newFluidStack) {
        if (!hasLevel())
            return;
        ReactorBlockEntity controllerBE = getControllerBE();
        if (controllerBE != null)
            controllerBE.checkForRecipes();
        FluidType attributes = newFluidStack.getFluid()
                .getFluidType();
        int luminosity = (int) (attributes.getLightLevel(newFluidStack) / 1.2f);
        boolean reversed = attributes.isLighterThanAir();
        int maxY = (int) ((getFillState() * height) + 1);

        for (int yOffset = 0; yOffset < height; yOffset++) {
            boolean isBright = reversed ? (height - yOffset <= maxY) : (yOffset < maxY);
            int actualLuminosity = isBright ? luminosity : luminosity > 0 ? 1 : 0;

            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {
                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    ReactorBlockEntity tankAt = ConnectivityHandler.partAt(getType(), level, pos);
                    if (tankAt == null)
                        continue;
                    level.updateNeighbourForOutputSignal(pos, tankAt.getBlockState()
                            .getBlock());
                    if (tankAt.luminosity == actualLuminosity)
                        continue;
                    tankAt.setLuminosity(actualLuminosity);
                }
            }
        }

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

        if (isVirtual()) {
            if (fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(getFillState());
            fluidLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
        }
    }

    protected void setLuminosity(int luminosity) {
        if (level.isClientSide)
            return;
        if (this.luminosity == luminosity)
            return;
        this.luminosity = luminosity;
        sendData();
    }

    @Override
    public ReactorBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity blockEntity = level.getBlockEntity(controller);
        if (blockEntity instanceof ReactorBlockEntity
                && blockEntity.getType() == getType())
            return (ReactorBlockEntity) blockEntity;
        return null;
    }

    public void applyFluidTankSize(int blocks) {
        // Capacity scales with the reactor footprint (width x width), not its height.
        tankInventory.setCapacity(getTotalTankSize() * getCapacityMultiplier());
        forceFluidLevelUpdate = true;
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
        applyFluidTankSize(1);
        onFluidStackChanged(tankInventory.getFluid());

        BlockState state = getBlockState();
        if (ReactorBlock.isReactor(state)) {
            state = state.setValue(ReactorBlock.BOTTOM, true);
            state = state.setValue(ReactorBlock.TOP, true);
            state = state.setValue(ReactorBlock.SHAPE, window ? FluidTankBlock.Shape.WINDOW : FluidTankBlock.Shape.PLAIN);
            level.setBlock(worldPosition, state, 6);
        }

        refreshCapability();
        setChanged();
        sendData();
    }

    public void toggleWindows() {
        ReactorBlockEntity be = getControllerBE();
        if (be == null)
            return;
        be.setWindows(!be.window);
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

    public void setWindows(boolean window) {
        this.window = window;
        for (int yOffset = 0; yOffset < height; yOffset++) {
            for (int xOffset = 0; xOffset < width; xOffset++) {
                for (int zOffset = 0; zOffset < width; zOffset++) {

                    BlockPos pos = this.worldPosition.offset(xOffset, yOffset, zOffset);
                    BlockState blockState = level.getBlockState(pos);
                    if (!ReactorBlock.isReactor(blockState))
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
                    level.setBlock(pos, blockState.setValue(ReactorBlock.SHAPE, shape), 22);
                    level.getChunkSource()
                            .getLightEngine()
                            .checkBlock(pos);
                }
            }
        }
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
        if (level == null)
            return tankInventory;
        ReactorBlockEntity controllerBE = getControllerBE();
        if (isBottom())
            return controllerBE != null ? controllerBE.tankInventory : tankInventory;
        if (controllerBE == null)
            return tankInventory;
        // All blocks of an output layer share the tank of the layer's first block
        int layer = worldPosition.getY() - controllerBE.getBlockPos().getY();
        BlockPos repPos = controllerBE.getBlockPos().offset(0, layer, 0);
        BlockEntity rep = level.getBlockEntity(repPos);
        return rep instanceof ReactorBlockEntity repBE && rep.getType() == getType() ? repBE.tankInventory : tankInventory;
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    public boolean isOutputLayerRepresentative() {
        ReactorBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return false;
        int layer = worldPosition.getY() - controllerBE.getBlockPos().getY();
        return worldPosition.equals(controllerBE.getBlockPos().offset(0, layer, 0));
    }

    @Override
    protected AABB createRenderBoundingBox() {
        if (isController())
            return super.createRenderBoundingBox().expandTowards(width - 1, 0, width - 1);
        if (isOutputLayerRepresentative()) {
            ReactorBlockEntity controllerBE = getControllerBE();
            if (controllerBE != null)
                return super.createRenderBoundingBox().expandTowards(controllerBE.getWidth() - 1, 0, controllerBE.getWidth() - 1);
        }
        return super.createRenderBoundingBox();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        IFluidHandler fluids = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition, null);
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
                    .add(Component.translatable(emptyHintKey()))
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
        int prevLum = luminosity;
        tanksFull = tag.getBoolean("TanksFull");

        updateConnectivity = tag.contains("Uninitialized");
        luminosity = tag.getInt("Luminosity");
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
        }
        tankInventory.readFromNBT(registries, tag.getCompound("TankContent"));
        if (tag.contains("ForceFluidLevel") || fluidLevel == null)
            fluidLevel = LerpedFloat.linear()
                    .startWithValue(getFillState());

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
        if (isController()) {
            float fillState = getFillState();
            if (tag.contains("ForceFluidLevel") || fluidLevel == null)
                fluidLevel = LerpedFloat.linear()
                        .startWithValue(fillState);
            fluidLevel.chase(fillState, 0.5f, LerpedFloat.Chaser.EXP);
            processingTime = tag.getInt("Progress");
        }
        if (luminosity != prevLum && hasLevel())
            level.getChunkSource()
                    .getLightEngine()
                    .checkBlock(worldPosition);

        if (tag.contains("LazySync"))
            fluidLevel.chase(fluidLevel.getChaseTarget(), 0.125f, LerpedFloat.Chaser.EXP);
        updateTemperature();
        List<Recipe<?>> r = getMatchingRecipes();
        if (!r.isEmpty()) {
            currentRecipe = (ReactorRecipe) r.get(0);
            if (processingTime <= 0)
                startProcessing();
        }
    }

    public float getFillState() {
        for (int i = 0; i < tankInventory.getTanks(); i++) {
            FluidStack stack = tankInventory.getFluidInTank(i);
            if (stack.isEmpty())
                continue;
            int capacity = tankInventory.getTankCapacity(i);
            return capacity == 0 ? 0 : (float) stack.getAmount() / capacity;
        }
        return 0;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (tanksFull)
            tag.putBoolean("TanksFull", true);
        if (updateConnectivity)
            tag.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            tag.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            tag.put("Controller", NbtUtils.writeBlockPos(controller));
        tag.put("TankContent", tankInventory.writeToNBT(registries, new CompoundTag()));
        if (isController()) {
            tag.putBoolean("Window", window);
            tag.putInt("Size", width);
            tag.putInt("Height", height);
            tag.putInt("Progress", processingTime);

        }
        tag.putInt("Luminosity", luminosity);

        if (!clientPacket)
            return;
        if (forceFluidLevelUpdate)
            tag.putBoolean("ForceFluidLevel", true);
        if (queuedSync)
            tag.putBoolean("LazySync", true);
        forceFluidLevelUpdate = false;
    }

    protected static <BE extends ReactorBlockEntity> void registerFluidCapability(RegisterCapabilitiesEvent event, BlockEntityType<BE> type) {
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

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public int getTotalTankSize() {
        return width * width;
    }

    public IFluidHandler getFluidCapability() {
        return fluidCapability;
    }

    public static int getCapacityMultiplier() {
        return AllConfigs.server().fluids.fluidTankCapacity.get() * 1000;
    }

    public LerpedFloat getFluidLevel() {
        return fluidLevel;
    }

    public BlazeBurnerBlock.HeatLevel getHighestHeatLevel() {
        return highestHeatLevel;
    }

    @Override
    public void preventConnectivityUpdate() {
        updateConnectivity = false;
    }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (ReactorBlock.isReactor(state)) { // safety
            state = state.setValue(ReactorBlock.BOTTOM, getBottomConnectivity());
            state = state.setValue(ReactorBlock.TOP, getTopConnectivity());
            level.setBlock(getBlockPos(), state, 6);
        }
        if (isController()) {
            applyFluidTankSize(1);
            setWindows(window);
        }
        onFluidStackChanged(tankInventory.getFluid());
        setChanged();
    }

    private boolean getBottomConnectivity() {
        if (level.getBlockEntity(getBlockPos().below()) instanceof ReactorBlockEntity be)
            return !isSameMultiBlock(be);
        return true;
    }

    private boolean getTopConnectivity() {
        if (level.getBlockEntity(getBlockPos().above()) instanceof ReactorBlockEntity be)
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
        if (longAxis == Direction.Axis.Y)
            return 1;
        return getMaxWidth();
    }

    @Override
    public int getMaxWidth() {
        return MAX_SIZE;
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
        if (tank < tankInventory.getTanks())
            return tankInventory.tanks.get(tank);
        return tankInventory;
    }

    @Override
    public FluidStack getFluid(int tank) {
        return tankInventory.getFluidInTank(tank)
                .copy();
    }

    public void updateVerticalMulti() {
        BlockState state = this.getBlockState();
        if (ReactorBlock.isReactor(state)) { // safety
            state = state.setValue(ReactorBlock.BOTTOM, getBottomConnectivity());
            state = state.setValue(ReactorBlock.TOP, getTopConnectivity());
            if (state != this.getBlockState())
                level.setBlock(getBlockPos(), state, 3);
        }
        if (level.getBlockEntity(getBlockPos().below()) instanceof ReactorBlockEntity be)
            be.updateVerticalMulti();
    }

    public boolean isBottom() {
        return !(level.getBlockEntity(getBlockPos().below()) instanceof ReactorBlockEntity be && isSameMultiBlock(be));
    }

    void checkForRecipes() {
        if (processingTime <= -1) {
            List<Recipe<?>> r = getMatchingRecipes();
            if (!r.isEmpty()) {
                currentRecipe = (ReactorRecipe) r.get(0);
                startProcessing();
            } else {
                currentRecipe = null;
            }
        }
    }

    public boolean isSameMultiBlock(ReactorBlockEntity be) {
        ReactorBlockEntity otherControllerBE = be.getControllerBE();
        ReactorBlockEntity controllerBE = getControllerBE();
        if (otherControllerBE == null || controllerBE == null)
            return false;
        if (otherControllerBE == controllerBE)
            return true;
        if (otherControllerBE.getBlockPos().getX() == controllerBE.getBlockPos().getX()
                && otherControllerBE.getBlockPos().getZ() == controllerBE.getBlockPos().getZ())
            return otherControllerBE.width == controllerBE.width;
        return false;
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (!isController()) {
            ReactorBlockEntity controller = getControllerBE();
            if (controller == null)
                return false;
            return controller.addToTooltip(tooltip, isPlayerSneaking);
        }

        BlockEntity below = level.getBlockEntity(getBlockPos().below());
        if (below instanceof ReactorBlockEntity bottomBe
                && bottomBe.getType() == getType()
                && bottomBe.width == width
                && bottomBe.getController().equals(getController().below())) {
            return bottomBe.addToTooltip(tooltip, isPlayerSneaking);
        }

        if (currentRecipe == null || !tanksFull)
            return false;

        Lang.builder(HarderDiesel.MODID)
                .translate(fullHintTitleKey())
                .style(ChatFormatting.GOLD)
                .forGoggles(tooltip);
        Component hint =
                Lang.builder(HarderDiesel.MODID)
                        .translate(fullHintKey())
                        .component();
        List<Component> cutComponent = TooltipHelper.cutTextComponent(hint, FontHelper.Palette.GRAY_AND_WHITE);
        for (Component component : cutComponent)
            CreateLang.builder().add(component).forGoggles(tooltip);
        return true;
    }

    public void updateTemperature() {
        if (!isBottom())
            return;
        if (isController()) {
            highestHeatLevel = getHeat();
            numberOfHighestHeat = getNumberOfHeat(highestHeatLevel);
            sendData();
            onFluidStackChanged(tankInventory.getFluid());
            return;
        }
        ReactorBlockEntity be = getControllerBE();
        if (be == null)
            return;
        be.updateTemperature();
    }

    public static class ReactorFluidHandler extends SmartFluidTank {
        int tankCount;
        NonNullList<FluidTank> tanks = NonNullList.create();

        public ReactorFluidHandler(int tankCount, int capacity, Consumer<FluidStack> updateCallback) {
            super(capacity, updateCallback);
            for (int i = 0; i < tankCount; i++)
                tanks.add(new FluidTank(capacity));

            this.tankCount = tankCount;
        }

        @Override
        public int getTanks() {
            return tankCount;
        }

        @Override
        public @org.jetbrains.annotations.NotNull FluidStack getFluidInTank(int tank) {
            return tanks.get(tank).getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return tanks.get(tank).getCapacity();
        }

        @Override
        public boolean isFluidValid(int tank, @org.jetbrains.annotations.NotNull FluidStack stack) {
            return true;
        }

        @Override
        public int fill(@org.jetbrains.annotations.NotNull FluidStack resource, IFluidHandler.FluidAction action) {
            for (FluidTank tank : tanks) {
                if (FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                    int result = tank.fill(resource, action);
                    if (action.execute())
                        onContentsChanged();
                    return result;
                }
            }

            for (FluidTank tank : tanks) {
                if (tank.getFluid().isEmpty()) {
                    int result = tank.fill(resource, action);
                    if (action.execute())
                        onContentsChanged();
                    return result;
                }
            }
            return 0;
        }

        @Override
        public @org.jetbrains.annotations.NotNull FluidStack drain(@org.jetbrains.annotations.NotNull FluidStack resource, IFluidHandler.FluidAction action) {
            for (FluidTank tank : tanks) {
                if (FluidStack.isSameFluidSameComponents(tank.getFluid(), resource)) {
                    FluidStack result = tank.drain(resource, action);
                    if (action.execute())
                        onContentsChanged();
                    return result;
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @org.jetbrains.annotations.NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            for (FluidTank tank : tanks) {
                if (!tank.getFluid().isEmpty()) {
                    FluidStack result = tank.drain(maxDrain, action);
                    if (action.execute())
                        onContentsChanged();
                    return result;
                }
            }
            return FluidStack.EMPTY;
        }

        @Override
        public @org.jetbrains.annotations.NotNull CompoundTag writeToNBT(HolderLookup.Provider registries, CompoundTag compound) {
            ListTag list = new ListTag();
            for (FluidTank tank : tanks)
                list.add(tank.writeToNBT(registries, new CompoundTag()));

            compound.put("Tanks", list);
            return compound;
        }

        @Override
        public @org.jetbrains.annotations.NotNull FluidTank readFromNBT(HolderLookup.Provider registries, CompoundTag tag) {
            ListTag list = tag.getList("Tanks", Tag.TAG_COMPOUND);
            for (int i = 0; i < tanks.size() && i < list.size(); i++) {
                FluidTank tank = tanks.get(i);
                tank.readFromNBT(registries, list.getCompound(i));
            }
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull FluidTank setCapacity(int capacity) {
            for (FluidTank tank : tanks)
                tank.setCapacity(capacity);
            return super.setCapacity(capacity);
        }
    }
}
