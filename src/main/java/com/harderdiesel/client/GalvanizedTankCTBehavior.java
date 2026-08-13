package com.harderdiesel.client;

import com.harderdiesel.content.galvanized.GalvanizedTankBlock;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GalvanizedTankCTBehavior extends ConnectedTextureBehaviour.Base {

    @Override
    public @Nullable CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        if (direction.getAxis().isVertical())
            return HarderDieselSpriteShifts.GALVANIZED_TANK_TOP;
        return HarderDieselSpriteShifts.GALVANIZED_TANK;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset) {
        return other.getBlock() instanceof GalvanizedTankBlock && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }
}
