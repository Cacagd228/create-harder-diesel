package com.harderdiesel.content.oil;

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
 * Принудительные переопределения сорта нефти по чанкам.
 * Используется командой /harderdiesel oil add — позволяет насильно
 * задать сорт даже когда OilEnabled=false. Хранится отдельно от
 * OilChunksSavedData (CDG) чтобы не трогать чужой формат.
 */
public class OilGradeOverrideSavedData extends SavedData {
    private static final String FILE_ID = "harderdiesel_grade_overrides";
    private final Map<Long, String> overrides = new HashMap<>();

    public OilGradeOverrideSavedData() {}

    public static OilGradeOverrideSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        OilGradeOverrideSavedData d = new OilGradeOverrideSavedData();
        ListTag list = tag.getList("Overrides", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag e = list.getCompound(i);
            d.overrides.put(e.getLong("Pos"), e.getString("Grade"));
        }
        return d;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag list = new ListTag();
        for (Map.Entry<Long, String> e : overrides.entrySet()) {
            CompoundTag ct = new CompoundTag();
            ct.putLong("Pos", e.getKey());
            ct.putString("Grade", e.getValue());
            list.add(ct);
        }
        tag.put("Overrides", list);
        return tag;
    }

    public static OilGradeOverrideSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(OilGradeOverrideSavedData::new, OilGradeOverrideSavedData::load),
                FILE_ID
        );
    }

    public CrudeGrade get(ChunkPos pos) {
        String name = overrides.get(pos.toLong());
        if (name == null) return null;
        try { return CrudeGrade.valueOf(name); } catch (Exception e) { return null; }
    }

    public void set(ChunkPos pos, CrudeGrade grade) {
        if (grade == null) {
            if (overrides.remove(pos.toLong()) != null) setDirty();
        } else {
            String prev = overrides.put(pos.toLong(), grade.name());
            if (!grade.name().equals(prev)) setDirty();
        }
    }

    public void clear(ChunkPos pos) {
        if (overrides.remove(pos.toLong()) != null) setDirty();
    }
}
