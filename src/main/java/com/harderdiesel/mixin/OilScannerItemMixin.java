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
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Сканер нефти: добавляет название сорта к сообщению ("Loads of Oil" и прочее).
 * При выключенной нефти сканер работает как обычно (звук, цикл),
 * но вместо результата сообщает о неведомой силе.
 */
@Mixin(value = OilScannerItem.class, remap = false)
public abstract class OilScannerItemMixin {

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
                if (!OilToggle.enabled()) {
                    // Нефть выключена: обычный звук и тайминги, но результат подменён
                    return new ClientboundSetActionBarTextPacket(
                            Component.translatable("harderdiesel.scanner.blocked"));
                }
                String key = tc.getKey();
                boolean hasOil = key.endsWith("oil_low") || key.endsWith("oil_high") || key.endsWith("oil_bottomless");
                if (hasOil) {
                    CrudeGrade grade = OilGradeAccess.getForChunk(sl, new ChunkPos(sp.blockPosition()));
                    if (grade != null)
                        wrapped = message.copy()
                                .append(Component.literal(" \u00B7 "))
                                .append(Component.translatable(grade.displayNameKey()));
                }
            }
        } catch (Throwable ignored) {}
        return new ClientboundSetActionBarTextPacket(wrapped);
    }
}
