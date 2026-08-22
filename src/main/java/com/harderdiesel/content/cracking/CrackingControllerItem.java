package com.harderdiesel.content.cracking;

import com.harderdiesel.ModBlocks;
import com.harderdiesel.ModConfig;
import com.harderdiesel.content.multiblock.ReactorControllerItem;
import net.minecraft.world.item.Item;

public class CrackingControllerItem extends ReactorControllerItem {

    public CrackingControllerItem(Properties properties) {
        super(properties,
                ModConfig.CRACKING_MIN_HEIGHT,
                ModBlocks.GALVANIZED_TANK,
                ModBlocks.CRACKING_REACTOR,
                "harderdiesel.actionbar.cracking_controller");
    }
}
