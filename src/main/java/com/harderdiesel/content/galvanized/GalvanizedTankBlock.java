package com.harderdiesel.content.galvanized;

import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModItems;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.api.schematic.requirement.SpecialBlockItemRequirement;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;

import java.util.ArrayList;
import java.util.List;

public class GalvanizedTankBlock extends Block implements IBE<GalvanizedTankBlockEntity>, IWrenchable, SpecialBlockItemRequirement {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final EnumProperty<FluidTankBlock.Shape> SHAPE = EnumProperty.create("shape", FluidTankBlock.Shape.class);

    public GalvanizedTankBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(TOP, true).setValue(BOTTOM, true).setValue(SHAPE, FluidTankBlock.Shape.PLAIN));
    }

    public static boolean isTank(BlockState state) {
        return state.getBlock() instanceof GalvanizedTankBlock;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos otherPos, boolean p_60514_) {
        super.neighborChanged(state, level, pos, block, otherPos, p_60514_);
        withBlockEntityDo(level, pos, GalvanizedTankBlockEntity::updateVerticalMulti);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TOP, BOTTOM, SHAPE);
    }

    @Override
    public Class<GalvanizedTankBlockEntity> getBlockEntityClass() {
        return GalvanizedTankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GalvanizedTankBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.GALVANIZED_TANK.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        withBlockEntityDo(context.getLevel(), context.getClickedPos(), GalvanizedTankBlockEntity::toggleWindows);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasBlockEntity() && (state.getBlock() != newState.getBlock() || !newState.hasBlockEntity())) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!(be instanceof GalvanizedTankBlockEntity))
                return;
            GalvanizedTankBlockEntity tankBE = (GalvanizedTankBlockEntity) be;
            world.removeBlockEntity(pos);
            ConnectivityHandler.splitMulti(tankBE);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return new ItemStack(ModItems.GALVANIZED_TANK.get());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (oldState.getBlock() == state.getBlock())
            return;
        if (moved)
            return;
        withBlockEntityDo(level, pos, GalvanizedTankBlockEntity::updateConnectivity);
        withBlockEntityDo(level, pos, GalvanizedTankBlockEntity::updateVerticalMulti);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        if (mirror == Mirror.NONE)
            return state;
        boolean x = mirror == Mirror.FRONT_BACK;
        return switch (state.getValue(SHAPE)) {
            case WINDOW_NE -> state.setValue(SHAPE, x ? FluidTankBlock.Shape.WINDOW_NW : FluidTankBlock.Shape.WINDOW_SE);
            case WINDOW_NW -> state.setValue(SHAPE, x ? FluidTankBlock.Shape.WINDOW_NE : FluidTankBlock.Shape.WINDOW_SW);
            case WINDOW_SE -> state.setValue(SHAPE, x ? FluidTankBlock.Shape.WINDOW_SW : FluidTankBlock.Shape.WINDOW_NE);
            case WINDOW_SW -> state.setValue(SHAPE, x ? FluidTankBlock.Shape.WINDOW_SE : FluidTankBlock.Shape.WINDOW_NW);
            default -> state;
        };
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        for (int i = 0; i < rotation.ordinal(); i++)
            state = rotateOnce(state);
        return state;
    }

    private BlockState rotateOnce(BlockState state) {
        return switch (state.getValue(SHAPE)) {
            case WINDOW_NE -> state.setValue(SHAPE, FluidTankBlock.Shape.WINDOW_SE);
            case WINDOW_NW -> state.setValue(SHAPE, FluidTankBlock.Shape.WINDOW_NE);
            case WINDOW_SE -> state.setValue(SHAPE, FluidTankBlock.Shape.WINDOW_SW);
            case WINDOW_SW -> state.setValue(SHAPE, FluidTankBlock.Shape.WINDOW_NW);
            default -> state;
        };
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity blockEntity) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(ModItems.GALVANIZED_TANK.get()));
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, list);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, net.minecraft.world.entity.Entity entity) {
        return SoundType.METAL;
    }
}
