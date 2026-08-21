package com.harderdiesel.content.oil;

import com.harderdiesel.ModFluids;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public enum CrudeGrade {
    HEAVY_SOUR("heavy_sour_crude", () -> ModFluids.HEAVY_SOUR_CRUDE.get()),
    HEAVY_SWEET("heavy_sweet_crude", () -> ModFluids.HEAVY_SWEET_CRUDE.get()),
    MEDIUM_SOUR("medium_sour_crude", () -> ModFluids.MEDIUM_SOUR_CRUDE.get()),
    MEDIUM_SWEET("medium_sweet_crude", () -> ModFluids.MEDIUM_SWEET_CRUDE.get());

    private final String fluidName;
    private final Supplier<Fluid> fluidSupplier;

    CrudeGrade(String fluidName, Supplier<Fluid> fluidSupplier) {
        this.fluidName = fluidName;
        this.fluidSupplier = fluidSupplier;
    }

    public String fluidName() {
        return fluidName;
    }

    public Fluid fluid() {
        try {
            return fluidSupplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean matches(Fluid fluid) {
        Fluid f = fluid();
        return f != null && f.isSame(fluid);
    }
}
