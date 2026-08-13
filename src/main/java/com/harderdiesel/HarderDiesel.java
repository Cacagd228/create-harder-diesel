package com.harderdiesel;

import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@Mod(HarderDiesel.MODID)
public class HarderDiesel {
    public static final String MODID = "harderdiesel";

    public HarderDiesel(IEventBus modEventBus, ModContainer container) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModBlockEntityTypes.register(modEventBus);
        ModGenerators.register(modEventBus);
        ModConfig.register(container);

        modEventBus.addListener((RegisterCapabilitiesEvent event) -> {
            CrackingReactorBlockEntity.registerCapabilities(event);
            ModGenerators.registerCapabilities(event);
        });
    }
}
