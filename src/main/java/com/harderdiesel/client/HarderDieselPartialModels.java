package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class HarderDieselPartialModels {
    public static final PartialModel CRACKING_REACTOR_GAUGE = model("block/cracking_reactor/gauge");
    public static final PartialModel JEI_REACTOR_TOP = model("block/jei_reactor/top");
    public static final PartialModel JEI_REACTOR_MIDDLE = model("block/jei_reactor/middle");
    public static final PartialModel JEI_REACTOR_BOTTOM = model("block/jei_reactor/bottom");

    public static final PartialModel SEPARATOR_GAUGE = model("block/separator/gauge");
    public static final PartialModel JEI_SEPARATOR_TOP = model("block/jei_separator/top");
    public static final PartialModel JEI_SEPARATOR_MIDDLE = model("block/jei_separator/middle");
    public static final PartialModel JEI_SEPARATOR_BOTTOM = model("block/jei_separator/bottom");

    public static PartialModel model(String id) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, id));
    }

    public static void init() {
    }
}
