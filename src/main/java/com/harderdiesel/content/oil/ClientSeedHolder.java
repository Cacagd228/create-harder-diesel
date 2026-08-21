package com.harderdiesel.content.oil;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

/**
 * Только клиент! Вызывается из OilGradeAccess.seedOf под гардом dist==CLIENT
 * (ленивая загрузка класса: на выделенном сервере ветка не исполняется).
 * Возвращает seed интегрированного сервера или 0 (SEED_UNKNOWN) на удалённом сервере.
 */
public final class ClientSeedHolder {
    private ClientSeedHolder() {}

    public static long get(Level level) {
        Minecraft mc = Minecraft.getInstance();
        var server = mc != null ? mc.getSingleplayerServer() : null;
        return server != null && server.overworld() != null ? server.overworld().getSeed() : 0L;
    }
}
