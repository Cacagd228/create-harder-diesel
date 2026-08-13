package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import net.minecraft.resources.ResourceLocation;

public class HarderDieselSpriteShifts {
    public static final CTSpriteShiftEntry CRACKING_REACTOR = rectangle("cracking_reactor/cracking_reactor"),
            CRACKING_REACTOR_TOP = rectangle("cracking_reactor/cracking_reactor_top"),
            CRACKING_REACTOR_NORTH = rectangle("cracking_reactor/cracking_reactor", "cracking_reactor/cracking_reactor_pipes_connected");

    public static final CTSpriteShiftEntry SEPARATOR = rectangle("separator/separator"),
            SEPARATOR_TOP = rectangle("separator/separator_top"),
            SEPARATOR_NORTH = rectangle("separator/separator", "separator/separator_pipes_connected");

    public static void init() {
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
