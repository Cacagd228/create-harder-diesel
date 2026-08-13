package com.harderdiesel.client;

import com.harderdiesel.content.cracking.CrackingReactorBlock;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CrackingReactorCTBehavior extends ConnectedTextureBehaviour.Base {

    @Override
    public @Nullable CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        if (direction.getAxis().isVertical())
            return HarderDieselSpriteShifts.CRACKING_REACTOR_TOP;
        if (direction == Direction.NORTH)
            return HarderDieselSpriteShifts.CRACKING_REACTOR_NORTH;
        return HarderDieselSpriteShifts.CRACKING_REACTOR;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset) {
        if (pos.above(1).equals(otherPos))
            return !state.getValue(CrackingReactorBlock.TOP);
        if (pos.below(1).equals(otherPos))
            return !state.getValue(CrackingReactorBlock.BOTTOM);
        return other.getBlock() instanceof CrackingReactorBlock && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }
}
