package com.harderdiesel;

import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;
import com.harderdiesel.content.galvanized.GalvanizedTankBlockEntity;
import com.harderdiesel.content.pollution.DistillationPollutionTicker;
import com.harderdiesel.content.pollution.PollutionCommand;
import com.harderdiesel.content.pollution.PollutionEffectHandler;
import com.harderdiesel.content.pollution.PollutionEvents;
import com.harderdiesel.content.separator.SeparatorBlockEntity;
import com.harderdiesel.content.wear_resistant.WearResistantTankBlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;

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
        modEventBus.addListener((net.neoforged.fml.event.config.ModConfigEvent event) ->
                ModConfig.migrateLegacyEmissionDefaults(event));

        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                CrackingReactorBlockEntity.registerCapabilities(event));
        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                SeparatorBlockEntity.registerCapabilities(event));
        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                GalvanizedTankBlockEntity.registerCapabilities(event));
        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                WearResistantTankBlockEntity.registerCapabilities(event));
        modEventBus.addListener((RegisterCapabilitiesEvent event) ->
                ModGenerators.registerCapabilities(event));

        // Pollution — серверные события (GAME bus)
        NeoForge.EVENT_BUS.register(PollutionEvents.class);
        NeoForge.EVENT_BUS.register(PollutionEffectHandler.class);
        NeoForge.EVENT_BUS.register(DistillationPollutionTicker.class);
        NeoForge.EVENT_BUS.register(PollutionCommand.class);
    }
}
