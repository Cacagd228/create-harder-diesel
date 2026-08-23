package com.harderdiesel.content.pollution;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Анализатор воздуха — латунный сканер.
 * ПКМ → сканирование с прогресс-баром в actionbar, затем количество
 * загрязнения 0..1000 и тир над ХП.
 */
public class AirAnalyzerItem extends Item {

    private static final int SCAN_DURATION = 40; // тиков (2 сек)
    private static final int BAR_WIDTH = 24;
    /** Остаток сканирования по игроку. */
    private static final Map<UUID, Integer> SCANNING = new HashMap<>();

    public AirAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // зачарованный блеск как у Debug Stick
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(level instanceof ServerLevel sl) || !(entity instanceof ServerPlayer player))
            return;
        UUID id = player.getUUID();
        Integer left = SCANNING.get(id);
        if (left == null)
            return;

        // сканирование прерывается, если предмет больше не в руках
        if (!player.getMainHandItem().is(this) && !player.getOffhandItem().is(this)) {
            SCANNING.remove(id);
            return;
        }

        left--;
        if (left <= 0) {
            SCANNING.remove(id);
            showReading(player, sl, new ChunkPos(player.blockPosition()));
            if (!player.isCreative())
                player.getCooldowns().addCooldown(this, 10);
            return;
        }
        SCANNING.put(id, left);
        player.displayClientMessage(progressBar(left), true);
    }

    private static Component progressBar(int ticksLeft) {
        float frac = 1f - (float) ticksLeft / SCAN_DURATION;
        int filled = Math.round(frac * BAR_WIDTH);
        return Component.literal("[" + "\u2588".repeat(Math.max(0, filled))
                + "\u2591".repeat(Math.max(0, BAR_WIDTH - filled)) + "] ")
                .withStyle(ChatFormatting.DARK_GREEN)
                .append(Component.literal((int) (frac * 100) + "%")
                        .withStyle(ChatFormatting.GREEN));
    }

    private static void showReading(Player player, Level level, ChunkPos pos) {
        float pollution = 0F;
        String source = "unknown";
        try {
            if (!level.isClientSide && level instanceof ServerLevel sl) {
                pollution = PollutionManager.getPollution(sl, pos);
                source = "server";
            } else {
                String dim = level.dimension().location().toString();
                try {
                    Class<?> cls = Class.forName("com.slavav.xaeroszones.client.pollution.ClientPollutionData");
                    pollution = (float) cls.getMethod("getPollution", String.class, int.class, int.class)
                            .invoke(null, dim, pos.x, pos.z);
                    source = "xaero-client";
                } catch (Throwable t) {
                    if (player instanceof ServerPlayer sp && sp.serverLevel() != null) {
                        pollution = PollutionManager.getPollution(sp.serverLevel(), pos);
                        source = "server-fallback";
                    } else {
                        // последний fallback — пробуем напрямую из level если это ServerLevel на клиенте инт. сервера
                        if (level instanceof ServerLevel sl2) {
                            pollution = PollutionManager.getPollution(sl2, pos);
                            source = "server-level-direct";
                        }
                    }
                }
            }
        } catch (Throwable t) {
            com.harderdiesel.HarderDiesel.LOGGER.error("[AirAnalyzer] showReading error", t);
            pollution = 0F;
        }
        PollutionTier tier = PollutionTier.from(pollution);
        String tierName = "T" + tier.tier;
        ChatFormatting color = switch (tier.tier) {
            case 0 -> ChatFormatting.GRAY;
            case 1 -> ChatFormatting.YELLOW;
            case 2 -> ChatFormatting.GOLD;
            case 3 -> ChatFormatting.RED;
            default -> ChatFormatting.DARK_RED;
        };
        Component msg = Component.translatable("item.harderdiesel.air_analyzer.reading",
                        String.format("%.0f", pollution), tierName)
                .withStyle(color);
        com.harderdiesel.HarderDiesel.LOGGER.debug("[AirAnalyzer] chunk={} dim={} poll={} tier={} src={} side={}",
                pos, level.dimension().location(), pollution, tierName, source, level.isClientSide ? "client" : "server");
        player.displayClientMessage(msg, true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        startScan(player);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;
        startScan(player);
        return InteractionResult.SUCCESS;
    }

    private static void startScan(Player player) {
        // повторное использование во время скана не сбрасывает его
        SCANNING.putIfAbsent(player.getUUID(), SCAN_DURATION);
    }
}
