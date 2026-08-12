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
        event.registerFluidType(fluidTextures("low_octane_gasoline"), ModFluidTypes.LOW_OCTANE_GASOLINE.get());
        event.registerFluidType(fluidTextures("high_octane_gasoline"), ModFluidTypes.HIGH_OCTANE_GASOLINE.get());
        event.registerFluidType(fluidTextures("artisan_high_octane_gasoline"), ModFluidTypes.ARTISAN_HIGH_OCTANE_GASOLINE.get());
        event.registerFluidType(fluidTextures("nitromethane"), ModFluidTypes.NITROMETHANE.get());
        event.registerFluidType(fluidTextures("propane"), ModFluidTypes.PROPANE.get());
        event.registerFluidType(fluidTextures("mixed_nitroalkanes"), ModFluidTypes.MIXED_NITROALKANES.get());
        event.registerFluidType(fluidTextures("nitroethane"), ModFluidTypes.NITROETHANE.get());
        event.registerFluidType(fluidTextures("nitropropane"), ModFluidTypes.NITROPROPANE.get());
        event.registerFluidType(fluidTextures("low_cetane_diesel"), ModFluidTypes.LOW_CETANE_DIESEL.get());
        event.registerFluidType(fluidTextures("medium_cetane_diesel"), ModFluidTypes.MEDIUM_CETANE_DIESEL.get());
        event.registerFluidType(fluidTextures("high_cetane_diesel"), ModFluidTypes.HIGH_CETANE_DIESEL.get());
        event.registerFluidType(fluidTextures("mazut"), ModFluidTypes.MAZUT.get());
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
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LOW_OCTANE_GASOLINE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HIGH_OCTANE_GASOLINE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.ARTISAN_HIGH_OCTANE_GASOLINE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.NITROMETHANE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.PROPANE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MIXED_NITROALKANES_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.NITROETHANE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.NITROPROPANE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LOW_CETANE_DIESEL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MEDIUM_CETANE_DIESEL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HIGH_CETANE_DIESEL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MAZUT_BUCKET.get());
    }
}
