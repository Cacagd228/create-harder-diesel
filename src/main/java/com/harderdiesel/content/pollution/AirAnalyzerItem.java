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
 * РђРЅР°Р»РёР·Р°С‚РѕСЂ РІРѕР·РґСѓС…Р° вЂ” Р»Р°С‚СѓРЅРЅС‹Р№ СЃРєР°РЅРµСЂ.
 * РџРљРњ в†’ СЃРєР°РЅРёСЂРѕРІР°РЅРёРµ СЃ РїСЂРѕРіСЂРµСЃСЃ-Р±Р°СЂРѕРј РІ actionbar, Р·Р°С‚РµРј РєРѕР»РёС‡РµСЃС‚РІРѕ
 * Р·Р°РіСЂСЏР·РЅРµРЅРёСЏ 0..1000 Рё С‚РёСЂ РЅР°Рґ РҐРџ.
 */
public class AirAnalyzerItem extends Item {

    private static final int SCAN_DURATION = 40; // С‚РёРєРѕРІ (2 СЃРµРє)
    private static final int BAR_WIDTH = 5;
    /** РћСЃС‚Р°С‚РѕРє СЃРєР°РЅРёСЂРѕРІР°РЅРёСЏ РїРѕ РёРіСЂРѕРєСѓ. */
    private static final Map<UUID, Integer> SCANNING = new HashMap<>();

    public AirAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // Р·Р°С‡Р°СЂРѕРІР°РЅРЅС‹Р№ Р±Р»РµСЃРє РєР°Рє Сѓ Debug Stick
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (!(level instanceof ServerLevel sl) || !(entity instanceof ServerPlayer player))
            return;
        UUID id = player.getUUID();
        Integer left = SCANNING.get(id);
        if (left == null)
            return;

        // СЃРєР°РЅРёСЂРѕРІР°РЅРёРµ РїСЂРµСЂС‹РІР°РµС‚СЃСЏ, РµСЃР»Рё РїСЂРµРґРјРµС‚ Р±РѕР»СЊС€Рµ РЅРµ РІ СЂСѓРєР°С…
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
        if (left % 8 == 0)
            sl.playSound(null, player.blockPosition(),
                    com.simibubi.create.AllSoundEvents.SCROLL_VALUE.getMainEvent(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 0.75f, 1f);
        player.displayClientMessage(progressBar(left), true);
    }

    private static Component progressBar(int ticksLeft) {
        float frac = 1f - (float) ticksLeft / SCAN_DURATION;
        int filled = Math.round(frac * BAR_WIDTH);
        // С‚РѕС‚ Р¶Рµ СЃС‚РёР»СЊ Р±Р°СЂР°, С‡С‚Рѕ Сѓ СЃРєР°РЅРµСЂР° РЅРµС„С‚Рё (TooltipHelper.makeProgressBar),
        // РЅРѕ 5 РєРІР°РґСЂР°С‚РѕРІ Рё СЃРµСЂР°СЏ СЃС‚СЂРѕРєР°
        return Component.literal(com.simibubi.create.foundation.item.TooltipHelper
                        .makeProgressBar(BAR_WIDTH, filled))
                .withStyle(ChatFormatting.GRAY)
                .append(Component.literal(" " + (int) (frac * 100) + "%")
                        .withStyle(ChatFormatting.GRAY));
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
                        // РїРѕСЃР»РµРґРЅРёР№ fallback вЂ” РїСЂРѕР±СѓРµРј РЅР°РїСЂСЏРјСѓСЋ РёР· level РµСЃР»Рё СЌС‚Рѕ ServerLevel РЅР° РєР»РёРµРЅС‚Рµ РёРЅС‚. СЃРµСЂРІРµСЂР°
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
        // РїРѕРІС‚РѕСЂРЅРѕРµ РёСЃРїРѕР»СЊР·РѕРІР°РЅРёРµ РІРѕ РІСЂРµРјСЏ СЃРєР°РЅР° РЅРµ СЃР±СЂР°СЃС‹РІР°РµС‚ РµРіРѕ
        SCANNING.putIfAbsent(player.getUUID(), SCAN_DURATION);
    }
}
