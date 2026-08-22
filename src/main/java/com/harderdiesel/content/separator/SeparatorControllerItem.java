package com.harderdiesel.content.separator;

import com.harderdiesel.ModBlocks;
import com.harderdiesel.ModConfig;
import com.harderdiesel.content.multiblock.ReactorControllerItem;
import net.minecraft.world.item.Item;

public class SeparatorControllerItem extends ReactorControllerItem {

    public SeparatorControllerItem(Properties properties) {
        super(properties,
                ModConfig.SEPARATOR_MIN_HEIGHT,
                ModBlocks.WEAR_RESISTANT_TANK,
                ModBlocks.SEPARATOR,
                "harderdiesel.actionbar.separator_controller");
    }
}
