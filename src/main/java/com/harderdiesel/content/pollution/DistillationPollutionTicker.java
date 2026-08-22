package com.harderdiesel.content.pollution;

import com.harderdiesel.ModConfig;
import com.jesz.createdieselgenerators.content.distillation.DistillationTankBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Эмиссия от активной Distillation Tower (CDG). Проверяем isController && isBottom && processingTime >=0.
 * Сканирует загруженные чанки через рефлексию (chunkMap приватный).
 * <p>
 * Отражённые Field/Method резолвятся один раз и кэшируются в статических полях:
 * раньше поиск с setAccessible выполнялся каждый тик для каждого BlockEntity.
 */
public class DistillationPollutionTicker {

    // Кэш рефлексии; null означает «член отсутствует» (интеграция сломана/изменилась)
    private static volatile boolean resolved = false;
    private static Field fChunkMap;
    private static Method mGetChunks;
    private static Method mIsBottom;
    private static Field fProcessingTime;
    private static Field fCurrentRecipe;
    // метод getTickingChunk зависит от класса holder'а — кэшируем по классу
    private static final Map<Class<?>, Method> TICKING_CHUNK_METHODS = new ConcurrentHashMap<>();

    private static void ensureResolved() {
        if (resolved) return;
        synchronized (DistillationPollutionTicker.class) {
            if (resolved) return;
            try {
                Class<?> chunkCacheClass = net.minecraft.server.level.ServerChunkCache.class;
                fChunkMap = chunkCacheClass.getDeclaredField("chunkMap");
                fChunkMap.setAccessible(true);
                mGetChunks = fChunkMap.getType().getMethod("getChunks");
            } catch (Throwable t) {
                fChunkMap = null;
                mGetChunks = null;
            }
            try {
                mIsBottom = DistillationTankBlockEntity.class.getMethod("isBottom");
            } catch (Throwable t) {
                mIsBottom = null;
            }
            try {
                fProcessingTime = DistillationTankBlockEntity.class.getDeclaredField("processingTime");
                fProcessingTime.setAccessible(true);
            } catch (Throwable t) {
                fProcessingTime = null;
            }
            try {
                fCurrentRecipe = DistillationTankBlockEntity.class.getDeclaredField("currentRecipe");
                fCurrentRecipe.setAccessible(true);
            } catch (Throwable t) {
                fCurrentRecipe = null;
            }
            resolved = true;
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel sl)) return;
        if (sl.getGameTime() % 20 != 0) return;

        ensureResolved();
        try {
            Object chunkMap = fChunkMap != null ? fChunkMap.get(sl.getChunkSource()) : null;
            Collection<?> chunks = getChunksFromMap(chunkMap);
            if (chunks == null) return;

            for (Object holder : chunks) {
                LevelChunk chunk = getTickingChunk(holder);
                if (chunk == null) continue;
                for (var be : chunk.getBlockEntities().values()) {
                    if (!(be instanceof DistillationTankBlockEntity dbe)) continue;
                    if (!dbe.isController()) continue;
                    // isBottom — если есть метод, проверяем
                    if (mIsBottom != null) {
                        try {
                            if (!(boolean) mIsBottom.invoke(dbe)) continue;
                        } catch (Throwable ignored) {}
                    }

                    boolean active = false;
                    try {
                        if (fProcessingTime != null && fProcessingTime.getInt(dbe) >= 0) active = true;
                    } catch (Throwable ignored) {}
                    if (!active) {
                        try {
                            if (fCurrentRecipe != null && fCurrentRecipe.get(dbe) != null) active = true;
                        } catch (Throwable ignored) {}
                    }
                    if (!active) continue;

                    float emit = ModConfig.POLLUTION_DISTILLATION_EMIT.get().floatValue();
                    if (emit <= 0) continue;
                    // тикер раз в 20 тиков (1/с), эмиссия в конфиге per-tick — умножаем на 20; множитель учтётся в PollutionManager.emit
                    PollutionManager.emit(sl, dbe.getBlockPos(), emit * 20F);
                    if (sl.getGameTime() % 100 == 0)
                        com.harderdiesel.HarderDiesel.LOGGER.debug("[Pollution] distillation emit at {} chunk={} emit/t={} (x20/с)",
                                dbe.getBlockPos(), new ChunkPos(dbe.getBlockPos()), emit);
                }
            }
        } catch (Throwable t) {
            com.harderdiesel.HarderDiesel.LOGGER.error("[Pollution] distillation ticker error", t);
        }
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> getChunksFromMap(Object chunkMap) {
        if (chunkMap == null || mGetChunks == null) return null;
        try {
            Object res = mGetChunks.invoke(chunkMap);
            if (res instanceof Collection<?> c) return c;
            // альтернатива: getChunks() возвращает Iterable<ChunkHolder>
            if (res instanceof Iterable<?> it) {
                List<Object> list = new ArrayList<>();
                for (Object o : it) list.add(o);
                return list;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static LevelChunk getTickingChunk(Object holder) {
        if (holder instanceof LevelChunk lc) return lc;
        if (holder == null) return null;
        Method m = TICKING_CHUNK_METHODS.computeIfAbsent(holder.getClass(), cls -> {
            try {
                return cls.getMethod("getTickingChunk");
            } catch (Throwable t) {
                return null;
            }
        });
        if (m != null) {
            try {
                if (m.invoke(holder) instanceof LevelChunk result) return result;
            } catch (Throwable ignored) {}
        }
        return null;
    }
}
