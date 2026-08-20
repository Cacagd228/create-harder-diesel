package com.harderdiesel.content.pollution;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Серверные дебафы по тирам загрязнения.
 * T1 тошнóта, T2 + отравление, T3 + голод, T4/5 + иссушение.
 */
public class PollutionEffectHandler {
    private static final Map<UUID, Integer> cooldown = new HashMap<>();
    private static final int EFFECT_INTERVAL = 100; // тиков между попытками наложить

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.isCreative() || player.isSpectator()) return;
        if (player.level().isClientSide) return;
        if (!(player.level() instanceof ServerLevel sl)) return;

        UUID id = player.getUUID();
        int cd = cooldown.getOrDefault(id, 0);
        if (cd > 0) {
            cooldown.put(id, cd - 1);
            return;
        }
        cooldown.put(id, EFFECT_INTERVAL + player.getRandom().nextInt(40));

        ChunkPos cp = new ChunkPos(player.blockPosition());
        float pollution = PollutionManager.getPollution(sl, cp);
        PollutionTier tier = PollutionTier.from(pollution);

        if (tier.tier == 0) return;

        // Интервал зависит от тира — чем выше, тем чаще
        // Уже заложено в cooldown randomization, дополнительно проверяем шанс
        float chance = switch (tier.tier) {
            case 1 -> 0.25F;
            case 2 -> 0.45F;
            case 3 -> 0.65F;
            case 4, 5 -> 0.85F;
            default -> 0F;
        };
        if (player.getRandom().nextFloat() > chance) return;

        // Длительности и усилители
        boolean isT5 = tier == PollutionTier.T5;

        if (tier.hasNausea()) {
            // тошнота: на 10 сек (200т) раз в ~100т
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, isT5 ? 300 : 200, 0, false, true, true));
        }
        if (tier.hasPoison()) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, isT5 ? 120 : 80, isT5 ? 1 : 0, false, true, true));
        }
        if (tier.hasHunger()) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, isT5 ? 200 : 140, isT5 ? 1 : 0, false, true, true));
        }
        if (tier.hasWither()) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER, isT5 ? 120 : 80, isT5 ? 1 : 0, false, true, true));
        }
    }
}
