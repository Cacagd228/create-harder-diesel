package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.ModGenerators;
import com.harderdiesel.ModGenerators.Family;
import com.harderdiesel.content.generators.FuelCategory;
import com.harderdiesel.content.generators.FueledDieselEngineRenderer;
import com.harderdiesel.content.generators.FueledHugeDieselEngineInstance;
import com.harderdiesel.content.generators.FueledHugeDieselEngineRenderer;
import com.harderdiesel.content.generators.FueledModularDieselEngineCTBehavior;
import com.harderdiesel.content.generators.FueledModularDieselEngineRenderer;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.kinetics.base.ShaftVisual;
import com.simibubi.create.foundation.block.connected.CTModel;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = HarderDiesel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModGeneratorClientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Family family : ModGenerators.FAMILIES.values()) {
                SimpleBlockEntityVisualizer.builder(family.normalBE.get())
                        .factory(ShaftVisual::new).neverSkipVanillaRender().apply();
                SimpleBlockEntityVisualizer.builder(family.modularBE.get())
                        .factory(ShaftVisual::new).neverSkipVanillaRender().apply();
                SimpleBlockEntityVisualizer.builder(family.hugeBE.get())
                        .factory(FueledHugeDieselEngineInstance::new).neverSkipVanillaRender().apply();
            }

            for (FuelCategory category : FuelCategory.values()) {
                Family family = ModGenerators.FAMILIES.get(category);
                ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(family.modular.get());
                CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                        .register(blockKey, model -> new CTModel(model, new FueledModularDieselEngineCTBehavior(category)));
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (Family family : ModGenerators.FAMILIES.values()) {
            event.registerBlockEntityRenderer(family.normalBE.get(), FueledDieselEngineRenderer::new);
            event.registerBlockEntityRenderer(family.modularBE.get(), FueledModularDieselEngineRenderer::new);
            event.registerBlockEntityRenderer(family.hugeBE.get(), FueledHugeDieselEngineRenderer::new);
        }
    }
}
