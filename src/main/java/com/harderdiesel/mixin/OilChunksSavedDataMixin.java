package com.harderdiesel.mixin;

import com.harderdiesel.content.oil.OilToggle;
import com.jesz.createdieselgenerators.world.OilChunksSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Редкость и глобальный тумблер нефти.
 * getBaseOilAmount вызывается только для чанков, которых ещё нет в
 * OilChunksSavedData: отсев части чанков делает месторождения реже,
 * а при выключенной нефти возвращает 0. Уже сохранённые месторождения
 * идут мимо этого метода и не затрагиваются.
 */
@Mixin(value = OilChunksSavedData.class, remap = false)
public abstract class OilChunksSavedDataMixin {

    @Inject(method = "getBaseOilAmount", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hd_gateBaseOil(ServerLevel level, ChunkPos pos, CallbackInfoReturnable<Integer> cir) {
        if (!OilToggle.enabled() || !OilToggle.chunkHasOil(level, pos))
            cir.setReturnValue(0);
    }
}
