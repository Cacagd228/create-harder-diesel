package com.harderdiesel.content.pollution;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.List;

/**
 * Серверные события для PollutionManager: тик диффузии/распада и синк при входе/смене измерения.
 */
public class PollutionEvents {

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        try {
            java.util.ArrayList<ServerLevel> list = new java.util.ArrayList<>();
            for (ServerLevel sl : event.getServer().getAllLevels()) list.add(sl);
            PollutionManager.serverTick(list);
        } catch (Throwable ignored) {}
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        // Старое измерение чистим
        try {
            Class<?> cls = Class.forName("com.slavav.xaeroszones.network.packet.s2c.ClearPollutionPacket");
            Object packet = cls.getConstructor(String.class).newInstance("*");
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(sp, (net.minecraft.network.protocol.common.custom.CustomPacketPayload) packet);
        } catch (Throwable ignored) {}
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // при респавне тоже синкануть область
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel sl)) return;
        ChunkPos cp = new ChunkPos(sp.blockPosition());
        PollutionManager.syncAreaToPlayer(sp, sl, cp.x, cp.z, 8);
    }
}
