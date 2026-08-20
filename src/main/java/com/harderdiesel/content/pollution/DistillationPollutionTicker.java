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
import java.util.Collection;

/**
 * Эмиссия от активной Distillation Tower (CDG). Проверяем isController && isBottom && processingTime >=0.
 * Сканирует загруженные чанки через рефлексию (chunkMap приватный).
 */
public class DistillationPollutionTicker {

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel sl)) return;
        if (sl.getGameTime() % 20 != 0) return;

        try {
            // Попытка 1: ServerChunkCache.chunkMap -> VisibleChunkMap -> getChunks()
            Object chunkMap = getChunkMap(sl);
            Collection<?> chunks = getChunksFromMap(chunkMap);
            if (chunks == null) return;

            for (Object holder : chunks) {
                LevelChunk chunk = getTickingChunk(holder);
                if (chunk == null) continue;
                for (var be : chunk.getBlockEntities().values()) {
                    if (!(be instanceof DistillationTankBlockEntity dbe)) continue;
                    if (!dbe.isController()) continue;
                    // isBottom — если есть метод, проверяем
                    try {
                        Method m = dbe.getClass().getMethod("isBottom");
                        if (!(boolean) m.invoke(dbe)) continue;
                    } catch (NoSuchMethodException ignored) {}

                    boolean active = false;
                    try {
                        Field f = dbe.getClass().getDeclaredField("processingTime");
                        f.setAccessible(true);
                        int pt = f.getInt(dbe);
                        if (pt >= 0) active = true;
                    } catch (Throwable ignored) {}
                    if (!active) {
                        try {
                            Field f2 = dbe.getClass().getDeclaredField("currentRecipe");
                            f2.setAccessible(true);
                            if (f2.get(dbe) != null) active = true;
                        } catch (Throwable ignored2) {}
                    }
                    if (!active) continue;

                    float emit = 0.0009F;
                    try { emit = ModConfig.POLLUTION_DISTILLATION_EMIT.get().floatValue(); if (Math.abs(emit - 2.2F) < 0.001F || Math.abs(emit - 0.30F) < 0.001F) emit = 0.0009F; } catch (Throwable ignored2) { emit = 0.0009F; }
                    if (emit <= 0) continue;
                    // тикер раз в 20 тиков (1/с), эмиссия в конфиге per-tick — умножаем на 20; множитель учтётся в PollutionManager.emit
                    PollutionManager.emit(sl, dbe.getBlockPos(), emit * 20F);
                    if (sl.getGameTime() % 100 == 0) System.out.println("[HarderDiesel][Pollution] distillation emit at " + dbe.getBlockPos() + " chunk=" + new ChunkPos(dbe.getBlockPos()) + " emit/t=" + emit + " (×20/с)");
                }
            }
        } catch (Throwable t) { System.err.println("[HarderDiesel][Pollution] distillation ticker error: "+t); t.printStackTrace(); }
    }

    private static Object getChunkMap(ServerLevel sl) {
        try {
            Field f = sl.getChunkSource().getClass().getDeclaredField("chunkMap");
            f.setAccessible(true);
            return f.get(sl.getChunkSource());
        } catch (Throwable t) { return null; }
    }

    @SuppressWarnings("unchecked")
    private static Collection<?> getChunksFromMap(Object chunkMap) {
        if (chunkMap == null) return null;
        try {
            // VisibleChunkMap.getChunks() or ChunkMap.getChunks()
            Method m = chunkMap.getClass().getMethod("getChunks");
            Object res = m.invoke(chunkMap);
            if (res instanceof Collection) return (Collection<?>) res;
            // альтернатива: getChunks() возвращает Iterable<ChunkHolder>
            if (res instanceof Iterable) {
                java.util.ArrayList<Object> list = new java.util.ArrayList<>();
                for (Object o : (Iterable<?>) res) list.add(o);
                return list;
            }
        } catch (Throwable ignored) {}
        return null;
    }

    private static LevelChunk getTickingChunk(Object holder) {
        if (holder == null) return null;
        try {
            Method m = holder.getClass().getMethod("getTickingChunk");
            Object c = m.invoke(holder);
            if (c instanceof LevelChunk lc) return lc;
        } catch (Throwable ignored) {}
        // иногда holder сам LevelChunk
        if (holder instanceof LevelChunk lc) return lc;
        return null;
    }
}
