package com.harderdiesel.content.pollution;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

/**
 * Анализатор воздуха — зачарованная палка.
 * ПКМ → над ХП (actionbar) количество загрязнения 0..1000 и тир.
 * Работает в креативе без кулдауна.
 */
public class AirAnalyzerItem extends Item {

    public AirAnalyzerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // зачарованный блеск как у Debug Stick
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
            System.err.println("[HarderDiesel][AirAnalyzer] showReading error: " + t);
            t.printStackTrace();
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
        System.out.println("[HarderDiesel][AirAnalyzer] chunk=" + pos + " dim=" + level.dimension().location() + " poll=" + pollution + " tier=" + tierName + " src=" + source + " side=" + (level.isClientSide ? "client" : "server"));
        player.displayClientMessage(msg, true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        ChunkPos pos = new ChunkPos(player.blockPosition());
        showReading(player, level, pos);
        if (!player.isCreative()) {
            player.getCooldowns().addCooldown(this, 10);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Player player = ctx.getPlayer();
        if (player == null) return InteractionResult.PASS;
        ChunkPos pos = new ChunkPos(ctx.getClickedPos());
        showReading(player, level, pos);
        if (!player.isCreative()) {
            player.getCooldowns().addCooldown(this, 10);
        }
        return InteractionResult.SUCCESS;
    }
}
