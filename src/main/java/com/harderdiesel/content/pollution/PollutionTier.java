package com.harderdiesel.content.pollution;

/**
 * Тиры загрязнения 0..5. Шкала 0..1000.
 * T0 0-200 нет эффекта, T1 200-400 тошнота, T2 400-600 + отравление, T3 600-800 + голод, T4 800-1000 + иссушение, T5 усиление на 1000.
 */
public enum PollutionTier {
    T0(0, 0F, 200F, 0F, 0F),
    T1(1, 200F, 400F, 0.02F, 0.15F),
    T2(2, 400F, 600F, 0.06F, 0.35F),
    T3(3, 600F, 800F, 0.13F, 0.58F),
    T4(4, 800F, 1000F, 0.25F, 0.82F),
    T5(5, 1000F, 1000F, 0.35F, 1.0F);

    public final int tier;
    public final float min;
    public final float max;
    /** Плотность тумана для ванильного fog */
    public final float fogDensity;
    public final float fogOpacity; // 0..1 для mix

    PollutionTier(int tier, float min, float max, float fogDensity, float fogOpacity) {
        this.tier = tier;
        this.min = min;
        this.max = max;
        this.fogDensity = fogDensity;
        this.fogOpacity = fogOpacity;
    }

    public static PollutionTier from(float pollution) {
        float p = Math.max(0, Math.min(1000, pollution));
        if (p >= 1000) return T5;
        if (p >= 800) return T4;
        if (p >= 600) return T3;
        if (p >= 400) return T2;
        if (p >= 200) return T1;
        return T0;
    }

    public boolean hasNausea() { return tier >= 1; }
    public boolean hasPoison() { return tier >= 2; }
    public boolean hasHunger() { return tier >= 3; }
    public boolean hasWither() { return tier >= 4; }

    /** Fog near/far distance multiplier: 1.0 = vanilla, <1 = плотнее */
    public float fogFarFactor() {
        return switch (tier) {
            case 0 -> 1.0F;
            case 1 -> 0.75F;
            case 2 -> 0.55F;
            case 3 -> 0.38F;
            case 4 -> 0.24F;
            case 5 -> 0.16F;
            default -> 1.0F;
        };
    }
}
