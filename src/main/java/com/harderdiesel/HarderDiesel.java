package com.harderdiesel;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(HarderDiesel.MODID)
public class HarderDiesel {
    public static final String MODID = "harderdiesel";

    public HarderDiesel(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}
