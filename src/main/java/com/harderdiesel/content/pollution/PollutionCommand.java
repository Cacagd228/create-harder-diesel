package com.harderdiesel.content.pollution;

import com.harderdiesel.content.oil.CrudeGrade;
import com.harderdiesel.content.oil.OilGradeAccess;
import com.harderdiesel.content.oil.OilGradeOverrideSavedData;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
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
                                .then(Commands.literal("add")
                                        .requires(src -> src.hasPermission(2))
                                        // /harderdiesel oil add — дефолт amount + авто-сорт (чанк игрока)
                                        .executes(ctx -> executeOilAdd(ctx.getSource(), null, null))
                                        // совместимость: /harderdiesel oil add <amount> [grade]
                                        .then(Commands.argument("amountOnly", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                .executes(ctx -> executeOilAdd(ctx.getSource(),
                                                        IntegerArgumentType.getInteger(ctx, "amountOnly"), null))
                                                .then(Commands.argument("grade2", StringArgumentType.word())
                                                        .suggests((c, b) -> SharedSuggestionProvider.suggest(
                                                                java.util.Arrays.stream(CrudeGrade.values()).map(g -> g.name().toLowerCase()).toList(), b))
                                                        .executes(ctx -> executeOilAdd(ctx.getSource(),
                                                                IntegerArgumentType.getInteger(ctx, "amountOnly"),
                                                                StringArgumentType.getString(ctx, "grade2")))
                                                )
                                        )
                                        // основной: /harderdiesel oil add <grade> [amount] — сорт первый (с подсказкой)
                                        .then(Commands.argument("grade", StringArgumentType.word())
                                                .suggests((c, b) -> SharedSuggestionProvider.suggest(
                                                        java.util.Arrays.stream(CrudeGrade.values()).map(g -> g.name().toLowerCase()).toList(), b))
                                                .executes(ctx -> {
                                                    String raw = StringArgumentType.getString(ctx, "grade");
                                                    // если ввели число вместо сорта — трактуем как amount (защита от порядка веток)
                                                    if (raw.matches("\\d+")) {
                                                        try { return executeOilAdd(ctx.getSource(), Integer.parseInt(raw), null); } catch (Exception ignored) {}
                                                    }
                                                    return executeOilAdd(ctx.getSource(), null, raw);
                                                })
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                                        .executes(ctx -> executeOilAdd(ctx.getSource(),
                                                                IntegerArgumentType.getInteger(ctx, "amount"),
                                                                StringArgumentType.getString(ctx, "grade")))
                                                )
                                        )
                                )
                                .then(Commands.literal("remove")
                                        .requires(src -> src.hasPermission(2))
                                        .executes(ctx -> executeOilAdd(ctx.getSource(), 0, null))
                                )
                        )
        );
    }

    private static final int DEFAULT_OIL_AMOUNT = 3000000;

    private static int executeOilAdd(CommandSourceStack src, Integer amountOrNull, String gradeRaw) {
        if (!(src.getLevel() instanceof ServerLevel sl)) {
            src.sendFailure(Component.literal("Команда только на сервере"));
            return 0;
        }
        net.minecraft.world.entity.Entity e = src.getEntity();
        if (e == null) {
            src.sendFailure(Component.literal("Команда требует исполнителя в мире (игрока) — чанк берётся из позиции игрока"));
            return 0;
        }
        ChunkPos cp = new ChunkPos(e.blockPosition());
        int amount = amountOrNull != null ? amountOrNull : DEFAULT_OIL_AMOUNT;
        CrudeGrade grade = null;
        if (gradeRaw != null) {
            try { grade = CrudeGrade.valueOf(gradeRaw.toUpperCase()); }
            catch (Exception ex) {
                String avail = String.join(", ", java.util.Arrays.stream(CrudeGrade.values()).map(g -> g.name().toLowerCase()).toList());
                src.sendFailure(Component.literal("Неизвестный сорт нефти: " + gradeRaw + ". Допустимо: " + avail));
                return 0;
            }
        }

        // Принудительно пишем в CDG SavedData игнорируя OilEnabled/OilChunkChance
        try {
            if (amount == 0) {
                com.jesz.createdieselgenerators.world.OilChunksSavedData.removeChunk(sl, cp);
                OilGradeOverrideSavedData.get(sl).clear(cp);
                OilGradeAccess.invalidate(cp, sl.dimension().location().toString());
                src.sendSuccess(() -> Component.literal("Нефть в чанке [" + cp.x + "," + cp.z + "] удалена"), true);
            } else {
                com.jesz.createdieselgenerators.world.OilChunksSavedData.setChunkOilAmount(sl, cp, amount);
                if (grade != null) {
                    OilGradeOverrideSavedData.get(sl).set(cp, grade);
                    OilGradeAccess.invalidate(cp, sl.dimension().location().toString());
                    // прогреть кэш новым сортом
                    OilGradeAccess.getForChunk(sl, cp);
                } else {
                    // сброс оверрайда если сорт не указан а был — оставляем как есть, но чистим кэш чтобы пересчитался детерминированный
                    // не трогаем оверрайд
                }
                String gradeName = grade != null ? grade.name().toLowerCase() : OilGradeAccess.getForChunk(sl, cp) != null ? OilGradeAccess.getForChunk(sl, cp).name().toLowerCase() : "?";
                String msg = grade != null
                        ? "Нефть добавлена в чанк [" + cp.x + "," + cp.z + "]: " + amount + " mB, сорт " + gradeName + " (принудительно, игнорируя тумблер)"
                        : "Нефть добавлена в чанк [" + cp.x + "," + cp.z + "]: " + amount + " mB, сорт " + gradeName + " (авто)";
                src.sendSuccess(() -> Component.literal(msg), true);
            }
        } catch (Throwable t) {
            src.sendFailure(Component.literal("Ошибка записи нефти: " + t.getMessage()));
            return 0;
        }
        return 1;
    }
}
