package com.harderdiesel.mixin;

import com.harderdiesel.content.oil.CrudeGrade;
import com.harderdiesel.content.oil.OilGradeAccess;
import com.harderdiesel.content.oil.OilToggle;
import com.jesz.createdieselgenerators.content.tools.OilScannerItem;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Сканер нефти: добавляет название сорта к сообщению ("Loads of Oil" и т.д.).
 * При выключенной нефти тик сканера отменяется целиком — он молчит.
 */
@Mixin(value = OilScannerItem.class, remap = false)
public abstract class OilScannerItemMixin {

    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true, remap = false)
    private void hd_scannerSilentWhenDisabled(ItemStack stack, Level level, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (!level.isClientSide && !OilToggle.enabled())
            ci.cancel();
    }

    @Redirect(method = "inventoryTick",
            at = @At(value = "NEW",
                    target = "(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/protocol/game/ClientboundSetActionBarTextPacket;"),
            remap = false)
    private ClientboundSetActionBarTextPacket hd_scannerAddGrade(Component message, ItemStack stack, Level level,
                                                                 Entity entity, int slot, boolean selected) {
        Component wrapped = message;
        try {
            if (entity instanceof ServerPlayer sp && sp.level() instanceof ServerLevel sl
                    && message.getContents() instanceof TranslatableContents tc) {
                String key = tc.getKey();
                boolean hasOil = key.endsWith("oil_low") || key.endsWith("oil_high") || key.endsWith("oil_bottomless");
                if (hasOil) {
                    CrudeGrade grade = OilGradeAccess.getForChunk(sl, new ChunkPos(sp.blockPosition()));
                    if (grade != null)
                        wrapped = message.copy().append(" \u00B7 "
                                + Component.translatable(grade.displayNameKey()).getString());
                }
            }
        } catch (Throwable ignored) {}
        return new ClientboundSetActionBarTextPacket(wrapped);
    }
}
