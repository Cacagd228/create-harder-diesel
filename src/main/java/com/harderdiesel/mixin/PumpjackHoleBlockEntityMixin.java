package com.harderdiesel.mixin;

import com.harderdiesel.content.oil.CrudeGrade;
import com.harderdiesel.content.oil.OilGradeAccess;
import com.harderdiesel.content.oil.OilToggle;
import com.jesz.createdieselgenerators.content.pumpjack.PumpjackHoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Помпа CDG качает сорт нефти, детерминированный по чанку (вместо первого флюида тега pumpjack_output).
 * Гогглы показывают сорт рядом с количеством.
 */
@Mixin(value = PumpjackHoleBlockEntity.class, remap = false)
public abstract class PumpjackHoleBlockEntityMixin {

    private Level hd_level() {
        return ((BlockEntity) (Object) this).getLevel();
    }

    private BlockPos hd_pos() {
        return ((BlockEntity) (Object) this).getBlockPos();
    }

    /** Нефть выключена: помпа не качает и не перезаписывает месторождение нулём. */
    @Inject(method = "pumpjackRotation", at = @At("HEAD"), cancellable = true, remap = false)
    private void hd_blockPumpingWhenDisabled(boolean rotation, CallbackInfo ci) {
        if (!OilToggle.enabled())
            ci.cancel();
    }

    /** Подмена stackList.get(0): и при закачке в танк (сервер), и в партиклах (клиент). */
    @Redirect(method = "pumpjackRotation",
            at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", remap = false),
            remap = false)
    private Object hd_pumpGradeFluid(List<Fluid> list, int index) {
        // Сужаем перехват: только индекс 0 и только когда сорт известен и нефть включена
        if (index == 0 && hd_level() != null && OilToggle.enabled() && !list.isEmpty()) {
            try {
                CrudeGrade grade = OilGradeAccess.getForChunk(hd_level(), new ChunkPos(hd_pos()));
                Fluid f = grade != null ? grade.fluid() : null;
                if (f != null) return f;
            } catch (Throwable ignored) {}
        }
        return list.get(index);
    }

    /** Гогглы: "1,234,567 mB" → "1,234,567 mB · Тяжёлая кислая нефть". */
    @Redirect(method = "addToGoggleTooltip",
            at = @At(value = "INVOKE",
                    target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
                    remap = false),
            remap = false)
    private String hd_gogglesAddGrade(String format, Object[] args) {
        String s = String.format(format, args);
        if (hd_level() != null && OilToggle.enabled()) {
            try {
                CrudeGrade grade = OilGradeAccess.getForChunk(hd_level(), new ChunkPos(hd_pos()));
                if (grade != null)
                    return s + " \u00B7 " + Component.translatable(grade.displayNameKey()).getString();
            } catch (Throwable ignored) {}
        }
        return s;
    }
}
