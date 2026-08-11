package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.ModFluidTypes;
import com.harderdiesel.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

@EventBusSubscriber(modid = HarderDiesel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(fluidTextures("naphtha"), ModFluidTypes.NAPHTHA.get());
        event.registerFluidType(fluidTextures("kerosene"), ModFluidTypes.KEROSENE.get());
        event.registerFluidType(fluidTextures("heavy_oil"), ModFluidTypes.HEAVY_OIL.get());
        event.registerFluidType(fluidTextures("tar"), ModFluidTypes.TAR.get());
    }

    private static IClientFluidTypeExtensions fluidTextures(String name) {
        return new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + name + "_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "block/" + name + "_flow");
            }
        };
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.NAPHTHA_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.KEROSENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HEAVY_OIL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.TAR_BUCKET.get());
    }
}
