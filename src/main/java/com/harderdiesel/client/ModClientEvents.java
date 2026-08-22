package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.ModBlockEntityTypes;
import com.harderdiesel.ModFluidTypes;
import com.harderdiesel.ModItems;
import com.simibubi.create.CreateClient;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

@EventBusSubscriber(modid = HarderDiesel.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerFluidType(fluidTextures("naphtha"), ModFluidTypes.NAPHTHA.get());
        event.registerFluidType(fluidTextures("kerosene"), ModFluidTypes.KEROSENE.get());
        event.registerFluidType(fluidTextures("heavy_gas_oil"), ModFluidTypes.HEAVY_GAS_OIL.get());
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
        event.registerFluidType(fluidTextures("butane"), ModFluidTypes.BUTANE.get());
        event.registerFluidType(fluidTextures("butane_liquid"), ModFluidTypes.BUTANE_LIQUID.get());
        event.registerFluidType(fluidTextures("lpg_liquid"), ModFluidTypes.LPG_LIQUID.get());
        event.registerFluidType(fluidTextures("petroleum_gas"), ModFluidTypes.PETROLEUM_GAS.get());
        event.registerFluidType(fluidTextures("propane_liquid"), ModFluidTypes.PROPANE_LIQUID.get());
        event.registerFluidType(fluidTextures("vacuum_gas_oil"), ModFluidTypes.VACUUM_GAS_OIL.get());
        event.registerFluidType(fluidTextures("vacuum_residue"), ModFluidTypes.VACUUM_RESIDUE.get());
        event.registerFluidType(fluidTextures("cracked_naphtha"), ModFluidTypes.CRACKED_NAPHTHA.get());
        event.registerFluidType(fluidTextures("light_cycle_oil"), ModFluidTypes.LIGHT_CYCLE_OIL.get());
        event.registerFluidType(fluidTextures("fcc_gas"), ModFluidTypes.FCC_GAS.get());
        event.registerFluidType(fluidTextures("alkylate"), ModFluidTypes.ALKYLATE.get());
        event.registerFluidType(fluidTextures("glycerol"), ModFluidTypes.GLYCEROL.get());
        event.registerFluidType(fluidTextures("nitroglycerin"), ModFluidTypes.NITROGLYCERIN.get());
        event.registerFluidType(fluidTextures("reformate"), ModFluidTypes.REFORMATE.get());
        event.registerFluidType(fluidTextures("benzene"), ModFluidTypes.BENZENE.get());
        event.registerFluidType(fluidTextures("toluene"), ModFluidTypes.TOLUENE.get());
        event.registerFluidType(fluidTextures("xylene"), ModFluidTypes.XYLENE.get());
        event.registerFluidType(fluidTextures("light_sweet_crude"), ModFluidTypes.LIGHT_SWEET_CRUDE.get());
        event.registerFluidType(fluidTextures("light_sour_crude"), ModFluidTypes.LIGHT_SOUR_CRUDE.get());
        event.registerFluidType(fluidTextures("medium_sweet_crude"), ModFluidTypes.MEDIUM_SWEET_CRUDE.get());
        event.registerFluidType(fluidTextures("medium_sour_crude"), ModFluidTypes.MEDIUM_SOUR_CRUDE.get());
        event.registerFluidType(fluidTextures("heavy_sweet_crude"), ModFluidTypes.HEAVY_SWEET_CRUDE.get());
        event.registerFluidType(fluidTextures("heavy_sour_crude"), ModFluidTypes.HEAVY_SOUR_CRUDE.get());
        event.registerFluidType(fluidTextures("molten_sulfur"), ModFluidTypes.MOLTEN_SULFUR.get());
        event.registerFluidType(fluidTextures("sour_naphtha"), ModFluidTypes.SOUR_NAPHTHA.get());
        event.registerFluidType(fluidTextures("sour_kerosene"), ModFluidTypes.SOUR_KEROSENE.get());
        event.registerFluidType(fluidTextures("ethylene"), ModFluidTypes.ETHYLENE.get());
        event.registerFluidType(fluidTextures("propylene"), ModFluidTypes.PROPYLENE.get());
        event.registerFluidType(fluidTextures("butadiene"), ModFluidTypes.BUTADIENE.get());
        event.registerFluidType(fluidTextures("aviation_fuel"), ModFluidTypes.AVIATION_FUEL.get());
        event.registerFluidType(fluidTextures("sulfuric_acid"), ModFluidTypes.SULFURIC_ACID.get());
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
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HEAVY_GAS_OIL_BUCKET.get());
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
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.BUTANE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.BUTANE_LIQUID_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LPG_LIQUID_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.PETROLEUM_GAS_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.PROPANE_LIQUID_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.VACUUM_GAS_OIL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.VACUUM_RESIDUE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.CRACKED_NAPHTHA_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LIGHT_CYCLE_OIL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.FCC_GAS_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.ALKYLATE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.GLYCEROL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.NITROGLYCERIN_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.REFORMATE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.BENZENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.TOLUENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.XYLENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LIGHT_SWEET_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.LIGHT_SOUR_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MEDIUM_SWEET_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MEDIUM_SOUR_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HEAVY_SWEET_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.HEAVY_SOUR_CRUDE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.MOLTEN_SULFUR_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.SOUR_NAPHTHA_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.SOUR_KEROSENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.ETHYLENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.PROPYLENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.BUTADIENE_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.AVIATION_FUEL_BUCKET.get());
        event.register(new DynamicFluidContainerModel.Colors(), ModItems.SULFURIC_ACID_BUCKET.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(HarderDieselSpriteShifts::init);
        HarderDieselPartialModels.init();
        net.createmod.ponder.foundation.PonderIndex.addPlugin(new HarderDieselPonderPlugin());
        CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                .register(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "cracking_reactor"),
                        model -> new MultiblockCTModel(model, new MultiblockCTBehavior(com.harderdiesel.content.cracking.CrackingReactorBlock.class,
                                HarderDieselSpriteShifts.CRACKING_REACTOR, HarderDieselSpriteShifts.CRACKING_REACTOR_TOP, HarderDieselSpriteShifts.CRACKING_REACTOR_NORTH)));
        CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                .register(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "separator"),
                        model -> new MultiblockCTModel(model, new MultiblockCTBehavior(com.harderdiesel.content.separator.SeparatorBlock.class,
                                HarderDieselSpriteShifts.SEPARATOR, HarderDieselSpriteShifts.SEPARATOR_TOP, HarderDieselSpriteShifts.SEPARATOR_NORTH)));
        CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                .register(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "galvanized_reactor_tank"),
                        model -> new MultiblockCTModel(model, new MultiblockCTBehavior(com.harderdiesel.content.galvanized.GalvanizedTankBlock.class,
                                HarderDieselSpriteShifts.GALVANIZED_TANK, HarderDieselSpriteShifts.GALVANIZED_TANK_TOP)));
        CreateClient.MODEL_SWAPPER.getCustomBlockModels()
                .register(ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "wear_resistant_tank"),
                        model -> new MultiblockCTModel(model, new MultiblockCTBehavior(com.harderdiesel.content.wear_resistant.WearResistantTankBlock.class,
                                HarderDieselSpriteShifts.WEAR_RESISTANT_TANK, HarderDieselSpriteShifts.WEAR_RESISTANT_TANK_TOP)));

        // XaerosZones Pollution provider — через рефлексию чтобы не крашить без xaerolib/xaeroszones
        event.enqueueWork(() -> {
            try {
                Class.forName("com.slavav.xaeroszones.api.pollution.PollutionRegistry");
                Class<?> providerCls = Class.forName("com.harderdiesel.content.pollution.HarderDieselPollutionProvider");
                Object provider = providerCls.getDeclaredConstructor().newInstance();
                Class<?> registryCls = Class.forName("com.slavav.xaeroszones.api.pollution.PollutionRegistry");
                java.lang.reflect.Method reg = registryCls.getMethod("registerProvider", ResourceLocation.class, Class.forName("com.slavav.xaeroszones.api.pollution.IPollutionProvider"));
                reg.invoke(null, ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "pollution"), provider);
                try {
                    java.lang.reflect.Method setActive = registryCls.getMethod("setActiveProvider", ResourceLocation.class);
                    setActive.invoke(null, ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "pollution"));
                } catch (Throwable ignored) {}
                try {
                    Class<?> cpd = Class.forName("com.slavav.xaeroszones.client.pollution.ClientPollutionData");
                    cpd.getField("showPollution").setBoolean(null, true);
                    cpd.getField("showPollutionWorldMap").setBoolean(null, true);
                    cpd.getField("showPollutionMinimap").setBoolean(null, true);
                    cpd.getField("pollutionOpacity").setFloat(null, 0.9F);
                } catch (Throwable ignored) {}
                LOGGER.info("Pollution provider registered and enabled");
            } catch (ClassNotFoundException cnf) {
                LOGGER.info("XaerosZones not present, pollution map overlay disabled");
            } catch (Throwable t) {
                LOGGER.error("Failed to register pollution provider", t);
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntityTypes.CRACKING_REACTOR.get(), CrackingReactorRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntityTypes.SEPARATOR.get(), SeparatorRenderer::new);
    }
}
