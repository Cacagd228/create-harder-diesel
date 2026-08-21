package com.harderdiesel.content.oil;

import com.jesz.createdieselgenerators.world.OilChunksSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Перехватывает вывод помпы: вместо crude_oil из CDG льём сорт по биому (тяжёлая/средняя).
 * Механика Oil Rig не меняется (без давления/истощения).
 */
public final class OilGradePumpjackHandler {
    private OilGradePumpjackHandler() {}

    // Вызывается из миксина PumpjackHoleBlockEntity.pumpjackRotation — подменяем список жидкостей тега pumpjack_output
    public static Fluid resolvePumpjackFluid(ServerLevel level, BlockPos pos) {
        ChunkPos cp = new ChunkPos(pos);
        CrudeGrade grade = OilGradeAccess.getForChunk(level, cp);
        Fluid f = grade.fluid();
        if (f != null) return f;
        // fallback — первый из pumpjack_output
        var tag = BuiltInRegistries.FLUID.getTag(com.jesz.createdieselgenerators.CDGTags.PUMPJACK_OUTPUT);
        if (tag.isPresent()) {
            var list = tag.get().stream().map(h -> h.value()).toList();
            if (!list.isEmpty()) return list.get(0);
        }
        return null;
    }
}
