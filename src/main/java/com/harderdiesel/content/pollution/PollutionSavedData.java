package com.harderdiesel.content.pollution;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

/**
 * SavedData per ServerLevel — хранит загрязнение чанков 0..1000.
 * Ключ — long ChunkPos#toLong().
 */
public class PollutionSavedData extends SavedData {
    private static final String FILE_ID = "harderdiesel_pollution";
    private final Map<Long, Float> data = new HashMap<>();

    public PollutionSavedData() {}

    public static PollutionSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        PollutionSavedData d = new PollutionSavedData();
        ListTag list = tag.getList("Pollution", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            long key = e.getLong("Pos");
            float val = e.getFloat("Val");
            d.data.put(key, val);
        }
        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (Map.Entry<Long, Float> e : data.entrySet()) {
            if (e.getValue() <= 0.01F) continue;
            CompoundTag ct = new CompoundTag();
            ct.putLong("Pos", e.getKey());
            ct.putFloat("Val", e.getValue());
            list.add(ct);
        }
        tag.put("Pollution", list);
        return tag;
    }

    public static PollutionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(PollutionSavedData::new, PollutionSavedData::load),
                FILE_ID
        );
    }

    public float get(ChunkPos pos) {
        return data.getOrDefault(pos.toLong(), 0F);
    }

    public void set(ChunkPos pos, float value) {
        float clamped = Math.max(0F, Math.min(1000F, value));
        long key = pos.toLong();
        if (clamped <= 0.01F) {
            if (data.remove(key) != null) setDirty();
        } else {
            Float prev = data.get(key);
            if (prev == null || Math.abs(prev - clamped) > 0.001F) {
                data.put(key, clamped);
                setDirty();
            }
        }
    }

    public void add(ChunkPos pos, float delta) {
        set(pos, get(pos) + delta);
    }

    public Map<Long, Float> snapshot() {
        return new HashMap<>(data);
    }

    public Map<ChunkPos, Float> snapshotChunkMap() {
        Map<ChunkPos, Float> out = new HashMap<>();
        for (Map.Entry<Long, Float> e : data.entrySet()) out.put(new ChunkPos(e.getKey()), e.getValue());
        return out;
    }
}
