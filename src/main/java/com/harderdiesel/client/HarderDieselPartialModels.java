package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.content.generators.FuelCategory;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public class HarderDieselPartialModels {
    public static final PartialModel CRACKING_REACTOR_GAUGE = model("block/cracking_reactor/gauge");
    public static final PartialModel JEI_REACTOR_TOP = model("block/jei_reactor/top");
    public static final PartialModel JEI_REACTOR_MIDDLE = model("block/jei_reactor/middle");
    public static final PartialModel JEI_REACTOR_BOTTOM = model("block/jei_reactor/bottom");

    /** Поршни обычного генератора по категориям: {@code PISTONS[cat][frame]}. */
    public static final Map<FuelCategory, PartialModel[]> PISTONS = new EnumMap<>(FuelCategory.class);
    /** Вертикальные поршни обычного генератора по категориям. */
    public static final Map<FuelCategory, PartialModel[]> PISTONS_VERTICAL = new EnumMap<>(FuelCategory.class);
    /** Поршни модульного генератора по категориям. */
    public static final Map<FuelCategory, PartialModel[]> MODULAR_PISTONS = new EnumMap<>(FuelCategory.class);
    /** Большие генераторы: [поршень, шатун, соединитель вала]. */
    public static final Map<FuelCategory, PartialModel[]> HUGE_PARTS = new EnumMap<>(FuelCategory.class);

    static {
        for (FuelCategory cat : FuelCategory.values()) {
            PISTONS.put(cat, pistonModels(cat, false));
            PISTONS_VERTICAL.put(cat, pistonModels(cat, true));
            MODULAR_PISTONS.put(cat, pistonModels(cat, false, "large_"));
            HUGE_PARTS.put(cat, new PartialModel[]{
                    model("block/huge_" + cat.id + "_generator/piston"),
                    model("block/huge_" + cat.id + "_generator/linkage"),
                    model("block/huge_" + cat.id + "_generator/shaft_connector")
            });
        }
    }

    private static PartialModel[] pistonModels(FuelCategory cat, boolean vertical) {
        return pistonModels(cat, vertical, "");
    }

    private static PartialModel[] pistonModels(FuelCategory cat, boolean vertical, String prefix) {
        PartialModel[] arr = new PartialModel[5];
        String dir = "block/" + prefix + cat.id + "_generator/pistons/";
        String stem = vertical ? "vertical_" : "pistons_";
        for (int i = 0; i < 5; i++)
            arr[i] = model(dir + stem + i);
        return arr;
    }

    public static PartialModel model(String id) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, id));
    }

    /** Выбор кадра поршня по углу поворота (как в CDG). */
    public static int pistonFrame(int angle) {
        return switch (angle) {
            case 10 -> 0;
            case 9 -> 1;
            case 8 -> 2;
            case 7 -> 3;
            case 6, 5 -> 4;
            case 4 -> 3;
            case 3 -> 2;
            case 2 -> 1;
            default -> 0;
        };
    }

    public static PartialModel enginePiston(FuelCategory cat, int angle, boolean vertical) {
        return (vertical ? PISTONS_VERTICAL : PISTONS).get(cat)[pistonFrame(angle)];
    }

    public static PartialModel modularPiston(FuelCategory cat, int angle) {
        return MODULAR_PISTONS.get(cat)[pistonFrame(angle)];
    }

    public static void init() {
    }
}

