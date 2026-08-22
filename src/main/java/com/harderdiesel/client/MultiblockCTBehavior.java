package com.harderdiesel.client;

import com.harderdiesel.content.multiblock.ReactorBlock;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * Параметризованный CT-бихевиор для мультиблочных семейств
 * (крекинг-реактор, сепаратор, баки). Заменяет четыре одинаковых класса.
 */
public class MultiblockCTBehavior extends ConnectedTextureBehaviour.Base {

    private final Class<? extends Block> blockClass;
    private final CTSpriteShiftEntry horizontal;
    @Nullable
    private final CTSpriteShiftEntry vertical;
    @Nullable
    private final CTSpriteShiftEntry north;

    public MultiblockCTBehavior(Class<? extends Block> blockClass,
                                CTSpriteShiftEntry horizontal,
                                @Nullable CTSpriteShiftEntry vertical,
                                @Nullable CTSpriteShiftEntry north) {
        this.blockClass = blockClass;
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.north = north;
    }

    public MultiblockCTBehavior(Class<? extends Block> blockClass,
                                CTSpriteShiftEntry horizontal,
                                CTSpriteShiftEntry vertical) {
        this(blockClass, horizontal, vertical, null);
    }

    @Override
    public @Nullable CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        if (direction.getAxis().isVertical() && vertical != null)
            return vertical;
        if (direction == Direction.NORTH && north != null)
            return north;
        return horizontal;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset) {
        if (ReactorBlock.class.isAssignableFrom(blockClass)) {
            if (pos.above(1).equals(otherPos))
                return !state.getValue(ReactorBlock.TOP);
            if (pos.below(1).equals(otherPos))
                return !state.getValue(ReactorBlock.BOTTOM);
        }
        return blockClass.isInstance(other.getBlock()) && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }
}
