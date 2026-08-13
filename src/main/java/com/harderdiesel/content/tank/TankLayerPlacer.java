package com.harderdiesel.content.tank;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Mirrors the Create fluid tank convenience: placing a single tank block on the
 * top or bottom of an existing multi-tank (width > 1) instantly fills the whole
 * layer of that footprint, consuming one item per block.
 */
public class TankLayerPlacer {

    public static void fillLayer(Level level, BlockPos pos, BlockEntityType<?> type, Block block, Player player,
                                 ItemStack stack) {
        if (level.isClientSide)
            return;
        for (BlockPos neighborPos : new BlockPos[] { pos.above(), pos.below() }) {
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            if (!(neighbor instanceof IMultiBlockEntityContainer part))
                continue;
            // only extend structures built from the same block
            if (level.getBlockState(neighborPos).getBlock() != block)
                continue;
            IMultiBlockEntityContainer controllerBE = part.getControllerBE();
            if (controllerBE == null)
                continue;
            int width = controllerBE.getWidth();
            if (width <= 1)
                continue;
            BlockPos controller = controllerBE.getController();
            int height = controllerBE.getHeight();
            int layerY;
            if (pos.getY() == controller.getY() + height) {
                layerY = pos.getY();
            } else if (pos.getY() == controller.getY() - 1) {
                layerY = pos.getY();
            } else {
                continue;
            }
            if (pos.getX() < controller.getX() || pos.getX() >= controller.getX() + width)
                continue;
            if (pos.getZ() < controller.getZ() || pos.getZ() >= controller.getZ() + width)
                continue;

            List<BlockPos> missing = new ArrayList<>();
            for (int dx = 0; dx < width; dx++) {
                for (int dz = 0; dz < width; dz++) {
                    BlockPos target = new BlockPos(controller.getX() + dx, layerY, controller.getZ() + dz);
                    if (target.equals(pos))
                        continue;
                    if (level.getBlockEntity(target) instanceof IMultiBlockEntityContainer)
                        continue;
                    BlockState targetState = level.getBlockState(target);
                    if (!targetState.canBeReplaced())
                        continue;
                    missing.add(target);
                }
            }
            if (missing.isEmpty())
                return;

            // The placed block itself consumes one item right after setPlacedBy,
            // so the whole layer can only be auto-filled if the player can afford
            // the missing blocks plus that one. Otherwise build it block by block.
            boolean creative = player.isCreative();
            if (!creative && stack.getCount() < missing.size() + 1)
                return;

            for (BlockPos target : missing) {
                level.setBlock(target, block.defaultBlockState(), 3);
                if (!creative)
                    stack.shrink(1);
            }
            return;
        }
    }
}
