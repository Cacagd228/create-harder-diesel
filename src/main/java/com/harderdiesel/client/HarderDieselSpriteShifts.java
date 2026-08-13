package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.content.generators.FuelCategory;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import net.minecraft.resources.ResourceLocation;

public class HarderDieselSpriteShifts {
    public static final CTSpriteShiftEntry CRACKING_REACTOR = rectangle("cracking_reactor/cracking_reactor"),
            CRACKING_REACTOR_TOP = rectangle("cracking_reactor/cracking_reactor_top"),
            CRACKING_REACTOR_NORTH = rectangle("cracking_reactor/cracking_reactor", "cracking_reactor/cracking_reactor_pipes_connected");

    public static void init() {
    }

    /** Соединяемая текстура модульного генератора конкретного семейства топлива. */
    public static CTSpriteShiftEntry modularDieselEngine(FuelCategory category) {
        return CTSpriteShifter.getCT(AllCTTypes.CROSS,
                ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + category.id + "/diesel_engine_big"),
                ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + category.id + "/diesel_engine_big_connected"));
    }

    private static CTSpriteShiftEntry rectangle(String name) {
        return rectangle(name, name + "_connected");
    }

    private static CTSpriteShiftEntry rectangle(String name, String connectedName) {
        return CTSpriteShifter.getCT(AllCTTypes.RECTANGLE,
                ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + name),
                ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + connectedName));
    }
}
