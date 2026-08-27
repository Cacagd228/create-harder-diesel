package com.harderdiesel.content.oil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * Только клиент! Вызывается из OilGradeAccess.seedOf через рефлексию
 * (прямая ссылка сломала бы dedicated server — NoClassDefFoundError на Minecraft).
 * Возвращает seed интегрированного сервера или SEED_UNKNOWN на удалённом сервере.
 */
@OnlyIn(Dist.CLIENT)
public final class ClientSeedHolder {
    private ClientSeedHolder() {}

    public static long get(Level level) {
        Minecraft mc = Minecraft.getInstance();
        var server = mc != null ? mc.getSingleplayerServer() : null;
        return server != null && server.overworld() != null ? server.overworld().getSeed() : OilGradeAccess.SEED_UNKNOWN;
    }
}
