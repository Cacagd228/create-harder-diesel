package com.harderdiesel.content.pollution;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class PollutionCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("harderdiesel")
                        .then(Commands.literal("pollution")
                                .then(Commands.literal("multiplier")
                                        .executes(ctx -> {
                                            double cur = PollutionManager.getGlobalMultiplier();
                                            ctx.getSource().sendSuccess(() -> Component.literal("Текущий множитель смога: " + cur), false);
                                            return 1;
                                        })
                                        .then(Commands.argument("factor", DoubleArgumentType.doubleArg(0, 100))
                                                .requires(src -> src.hasPermission(2))
                                                .executes(ctx -> {
                                                    double factor = DoubleArgumentType.getDouble(ctx, "factor");
                                                    PollutionManager.setGlobalMultiplier(factor);
                                                    try {
                                                        com.harderdiesel.ModConfig.POLLUTION_GLOBAL_MULTIPLIER.set(factor);
                                                    } catch (Throwable ignored) {}
                                                    ctx.getSource().sendSuccess(() -> Component.literal("Множитель смога установлен: " + factor + " (применяется к эмиссии всех машин)"), true);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("decay")
                                        .executes(ctx -> {
                                            double cur;
                                            try { cur = com.harderdiesel.ModConfig.POLLUTION_DECAY_RATE.get(); } catch (Throwable t) { cur = PollutionManager.DECAY_RATE; }
                                            double c = cur;
                                            ctx.getSource().sendSuccess(() -> Component.literal("Текущий распад: " + c + " (0.00002 = 2× медленнее набора, 1000→0 ~27ч)"), false);
                                            return 1;
                                        })
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 0.2))
                                                .requires(src -> src.hasPermission(2))
                                                .executes(ctx -> {
                                                    double v = DoubleArgumentType.getDouble(ctx, "value");
                                                    PollutionManager.DECAY_RATE = (float) v;
                                                    try { com.harderdiesel.ModConfig.POLLUTION_DECAY_RATE.set(v); } catch (Throwable ignored) {}
                                                    ctx.getSource().sendSuccess(() -> Component.literal("Распад установлен: " + v), true);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("diffusion")
                                        .executes(ctx -> {
                                            double cur;
                                            try { cur = com.harderdiesel.ModConfig.POLLUTION_DIFFUSION_RATE.get(); } catch (Throwable t) { cur = PollutionManager.DIFFUSION_RATE; }
                                            double c = cur;
                                            ctx.getSource().sendSuccess(() -> Component.literal("Текущая диффузия: " + c + " (flux = diffusion*(val-neighbor)/4 downhill)"), false);
                                            return 1;
                                        })
                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 0.3))
                                                .requires(src -> src.hasPermission(2))
                                                .executes(ctx -> {
                                                    double v = DoubleArgumentType.getDouble(ctx, "value");
                                                    PollutionManager.DIFFUSION_RATE = (float) v;
                                                    try { com.harderdiesel.ModConfig.POLLUTION_DIFFUSION_RATE.set(v); } catch (Throwable ignored) {}
                                                    ctx.getSource().sendSuccess(() -> Component.literal("Диффузия установлена: " + v), true);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("get")
                                        .executes(ctx -> {
                                            if (ctx.getSource().getEntity() instanceof net.minecraft.server.level.ServerPlayer sp) {
                                                float poll = PollutionManager.getPollutionForPlayer(sp);
                                                PollutionTier tier = PollutionTier.from(poll);
                                                ctx.getSource().sendSuccess(() -> Component.literal("Загрязнение в чанке: " + String.format("%.1f", poll) + " T" + tier.tier), false);
                                            } else if (ctx.getSource().getLevel() instanceof ServerLevel sl && ctx.getSource().getEntity() == null) {
                                                ctx.getSource().sendFailure(Component.literal("Только для игрока — укажите координаты: /harderdiesel pollution get <chunkX> <chunkZ>"));
                                            } else {
                                                ctx.getSource().sendFailure(Component.literal("Только для игрока"));
                                            }
                                            return 1;
                                        })
                                        .then(Commands.argument("chunkX", IntegerArgumentType.integer())
                                                .then(Commands.argument("chunkZ", IntegerArgumentType.integer())
                                                        .executes(ctx -> {
                                                            int x = IntegerArgumentType.getInteger(ctx, "chunkX");
                                                            int z = IntegerArgumentType.getInteger(ctx, "chunkZ");
                                                            if (ctx.getSource().getLevel() instanceof ServerLevel sl) {
                                                                float poll = PollutionManager.getPollution(sl, x, z);
                                                                PollutionTier tier = PollutionTier.from(poll);
                                                                ctx.getSource().sendSuccess(() -> Component.literal("Загрязнение [" + x + "," + z + "]: " + String.format("%.1f", poll) + " T" + tier.tier), false);
                                                            }
                                                            return 1;
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("set")
                                        .requires(src -> src.hasPermission(2))
                                        .then(Commands.argument("chunkX", IntegerArgumentType.integer())
                                                .then(Commands.argument("chunkZ", IntegerArgumentType.integer())
                                                        .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 1000))
                                                                .executes(ctx -> {
                                                                    int x = IntegerArgumentType.getInteger(ctx, "chunkX");
                                                                    int z = IntegerArgumentType.getInteger(ctx, "chunkZ");
                                                                    double v = DoubleArgumentType.getDouble(ctx, "value");
                                                                    if (ctx.getSource().getLevel() instanceof ServerLevel sl) {
                                                                        PollutionManager.setPollution(sl, new ChunkPos(x, z), (float) v, true);
                                                                        ctx.getSource().sendSuccess(() -> Component.literal("Загрязнение [" + x + "," + z + "] установлено: " + String.format("%.1f", v)), true);
                                                                    } else {
                                                                        ctx.getSource().sendFailure(Component.literal("Команда только на сервере"));
                                                                    }
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("oil")
                                .then(Commands.literal("status")
                                        .executes(ctx -> {
                                            boolean on = com.harderdiesel.content.oil.OilToggle.enabled();
                                            double chance;
                                            try { chance = com.harderdiesel.ModConfig.OIL_CHUNK_CHANCE.get(); } catch (Throwable t) { chance = 1.0; }
                                            double c = chance;
                                            ctx.getSource().sendSuccess(() -> Component.literal(
                                                    "Нефть: " + (on ? "включена" : "выключена (/harderdiesel oil enable)")
                                                    + " · доля чанков с месторождениями: " + String.format("%.0f%%", c * 100)), false);
                                            return 1;
                                        })
                                )
                                .then(Commands.literal("enable")
                                        .requires(src -> src.hasPermission(2))
                                        .executes(ctx -> {
                                            try { com.harderdiesel.ModConfig.OIL_ENABLED.set(true); } catch (Throwable t) {
                                                ctx.getSource().sendFailure(Component.literal("Не удалось изменить конфиг: " + t));
                                                return 0;
                                            }
                                            ctx.getSource().sendSuccess(() -> Component.literal("Нефть включена: месторождения снова видны сканеру и добываются"), true);
                                            return 1;
                                        })
                                )
                                .then(Commands.literal("disable")
                                        .requires(src -> src.hasPermission(2))
                                        .executes(ctx -> {
                                            try { com.harderdiesel.ModConfig.OIL_ENABLED.set(false); } catch (Throwable t) {
                                                ctx.getSource().sendFailure(Component.literal("Не удалось изменить конфиг: " + t));
                                                return 0;
                                            }
                                            ctx.getSource().sendSuccess(() -> Component.literal("Нефть выключена: добыча остановлена, сканер молчит. Существующие месторождения сохранены и возобновят работу после включения"), true);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
