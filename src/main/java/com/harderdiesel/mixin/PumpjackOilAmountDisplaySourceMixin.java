package com.harderdiesel.mixin;

import com.harderdiesel.content.oil.CrudeGrade;
import com.harderdiesel.content.oil.OilGradeAccess;
import com.jesz.createdieselgenerators.content.pumpjack.PumpjackHoleBlockEntity;
import com.jesz.createdieselgenerators.content.pumpjack.PumpjackOilAmountDisplaySource;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Display Link у помпы: показывает "Сорт · NB" вместо просто "NB".
 */
@Mixin(value = PumpjackOilAmountDisplaySource.class, remap = false)
public abstract class PumpjackOilAmountDisplaySourceMixin {

    @Inject(method = "provideLine", at = @At("HEAD"), cancellable = true, remap = false)
    private void hd_displayAddGrade(DisplayLinkContext context, DisplayTargetStats stats,
                                    CallbackInfoReturnable<MutableComponent> cir) {
        if (!(context.getSourceBlockEntity() instanceof PumpjackHoleBlockEntity be)) return;
        CrudeGrade grade = be.getLevel() != null
                ? OilGradeAccess.getForChunk(be.getLevel(), new ChunkPos(be.getBlockPos()))
                : null;
        String prefix = grade != null ? Component.translatable(grade.displayNameKey()).getString() + " \u00B7 " : "";
        cir.setReturnValue(Component.literal(prefix + be.oilAmount / 1000 + "B"));
    }
}
