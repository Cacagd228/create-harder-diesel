package com.harderdiesel;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HarderDiesel.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HARDER_DIESEL_TAB =
            CREATIVE_MODE_TABS.register("harder_diesel_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.harderdiesel"))
                    .icon(() -> new ItemStack(ModItems.NAPHTHA_BUCKET.get()))
                    .displayItems((params, output) -> {
                        for (ModGenerators.Family family : ModGenerators.FAMILIES.values()) {
                            output.accept(family.normalItem.get());
                            output.accept(family.modularItem.get());
                            output.accept(family.hugeItem.get());
                        }
                        output.accept(ModItems.COKE_COAL);
                        output.accept(ModItems.GALVANIZED_TANK);
                        output.accept(ModItems.WEAR_RESISTANT_TANK);
                        output.accept(ModItems.CRACKING_CONTROLLER);
                        output.accept(ModItems.SEPARATOR_CONTROLLER);
                        output.accept(ModItems.NAPHTHA_BUCKET);
                        output.accept(ModItems.KEROSENE_BUCKET);
                        output.accept(ModItems.HEAVY_OIL_BUCKET);
                        output.accept(ModItems.TAR_BUCKET);
                        output.accept(ModItems.LOW_OCTANE_GASOLINE_BUCKET);
                        output.accept(ModItems.HIGH_OCTANE_GASOLINE_BUCKET);
                        output.accept(ModItems.ARTISAN_HIGH_OCTANE_GASOLINE_BUCKET);
                        output.accept(ModItems.NITROMETHANE_BUCKET);
                        output.accept(ModItems.PROPANE_BUCKET);
                        output.accept(ModItems.MIXED_NITROALKANES_BUCKET);
                        output.accept(ModItems.NITROETHANE_BUCKET);
                        output.accept(ModItems.NITROPROPANE_BUCKET);
                        output.accept(ModItems.LOW_CETANE_DIESEL_BUCKET);
                        output.accept(ModItems.MEDIUM_CETANE_DIESEL_BUCKET);
                        output.accept(ModItems.HIGH_CETANE_DIESEL_BUCKET);
                        output.accept(ModItems.MAZUT_BUCKET);
                        output.accept(ModItems.BUTANE_GAS_BUCKET);
                        output.accept(ModItems.BUTANE_LIQUID_BUCKET);
                        output.accept(ModItems.LPG_GAS_BUCKET);
                        output.accept(ModItems.LPG_LIQUID_BUCKET);
                        output.accept(ModItems.WET_GAS_BUCKET);
                        output.accept(ModItems.PROPANE_LIQUID_BUCKET);
                        output.accept(ModItems.VACUUM_GAS_OIL_BUCKET);
                        output.accept(ModItems.VACUUM_RESIDUE_BUCKET);
                        output.accept(ModItems.CRACKED_NAPHTHA_BUCKET);
                        output.accept(ModItems.LIGHT_CYCLE_OIL_BUCKET);
                        output.accept(ModItems.FCC_GAS_BUCKET);
                        output.accept(ModItems.ALKYLATE_BUCKET);
                        output.accept(ModItems.GLYCEROL_BUCKET);
                        output.accept(ModItems.NITROGLYCERIN_BUCKET);
                        output.accept(ModItems.REFORMATE_BUCKET);
                        output.accept(ModItems.BENZENE_BUCKET);
                        output.accept(ModItems.TOLUENE_BUCKET);
                        output.accept(ModItems.XYLENE_BUCKET);
                        output.accept(ModItems.LIGHT_SWEET_CRUDE_BUCKET);
                        output.accept(ModItems.LIGHT_SOUR_CRUDE_BUCKET);
                        output.accept(ModItems.MEDIUM_SWEET_CRUDE_BUCKET);
                        output.accept(ModItems.MEDIUM_SOUR_CRUDE_BUCKET);
                        output.accept(ModItems.HEAVY_SWEET_CRUDE_BUCKET);
                        output.accept(ModItems.HEAVY_SOUR_CRUDE_BUCKET);
                        output.accept(ModItems.MOLTEN_SULFUR_BUCKET);
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
