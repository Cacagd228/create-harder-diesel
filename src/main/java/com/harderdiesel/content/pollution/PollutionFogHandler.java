package com.harderdiesel.content.pollution;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

/**
 * Ванильный туман от загрязнения. Клиент.
 * Использует ViewportEvent.RenderFog и ComputeFogColor.
 * Плотность зависит от PollutionTier.
 */
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class PollutionFogHandler {

    private static float getClientPollution() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Level level = mc.level;
        if (player == null || level == null) return 0F;
        String dim = level.dimension().location().toString();
        ChunkPos cp = new ChunkPos(player.blockPosition());
        try {
            // пробуем через Xaeros ClientPollutionData если доступен, иначе 0
            Class<?> cls = Class.forName("com.slavav.xaeroszones.client.pollution.ClientPollutionData");
            return (float) cls.getMethod("getPollution", String.class, int.class, int.class)
                    .invoke(null, dim, cp.x, cp.z);
        } catch (Throwable ignored) {
            return 0F;
        }
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        float pollution = getClientPollution();
        PollutionTier tier = PollutionTier.from(pollution);
        if (tier == PollutionTier.T0) return;

        // Чем выше тир — тем ближе fog
        float factor = tier.fogFarFactor();
        // Vanilla far plane ~ 192 * viewDistance; мы масштабируем
        float vanillaNear = event.getNearPlaneDistance();
        float vanillaFar = event.getFarPlaneDistance();

        // Смещаем ближе: near уменьшается сильнее, far — ещё сильнее
        float newNear = vanillaNear * (0.6F + 0.4F * factor);
        float newFar = vanillaFar * factor;
        // clamp
        if (newFar < newNear + 4) newFar = newNear + 4;

        event.setNearPlaneDistance(newNear);
        event.setFarPlaneDistance(newFar);
        // Отменяем отмену? event.setCanceled(false) — не нужно, просто меняем дистанции
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        float pollution = getClientPollution();
        PollutionTier tier = PollutionTier.from(pollution);
        if (tier == PollutionTier.T0) return;

        // Серо-жёлтый смог: подмешиваем к ванильному цвету
        float opacity = tier.fogOpacity * 0.55F; // чтобы не полностью перекрывать
        // целевая цветовая примесь смога (RGB 0.45, 0.42, 0.36)
        float targetR = 0.46F;
        float targetG = 0.44F;
        float targetB = 0.38F;

        float r = event.getRed();
        float g = event.getGreen();
        float b = event.getBlue();

        r = r * (1 - opacity) + targetR * opacity;
        g = g * (1 - opacity) + targetG * opacity;
        b = b * (1 - opacity) + targetB * opacity;

        event.setRed(r);
        event.setGreen(g);
        event.setBlue(b);
    }
}
