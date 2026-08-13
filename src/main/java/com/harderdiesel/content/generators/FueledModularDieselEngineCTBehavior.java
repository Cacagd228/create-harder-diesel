package com.harderdiesel.content.generators;

import com.harderdiesel.client.HarderDieselSpriteShifts;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

/** Соединяемые текстуры модульного генератора внутри одного семейства топлива. */
public class FueledModularDieselEngineCTBehavior extends ConnectedTextureBehaviour {
    private final FuelCategory category;

    public FueledModularDieselEngineCTBehavior(FuelCategory category) {
        this.category = category;
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite) {
        return HarderDieselSpriteShifts.modularDieselEngine(category);
    }

    @Override
    public CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        return AllCTTypes.CROSS;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter level, BlockPos pos, BlockPos otherPos,
                              Direction face, Direction primaryOffset, Direction secondaryOffset) {
        if (!(state.getBlock() instanceof FueledModularDieselEngineBlock && other.getBlock() instanceof FueledModularDieselEngineBlock))
            return false;
        return ConnectivityHandler.isConnected(level, pos, otherPos);
    }
}
