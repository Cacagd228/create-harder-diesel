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
                        output.accept(ModItems.NAPHTHA_BUCKET);
                        output.accept(ModItems.KEROSENE_BUCKET);
                        output.accept(ModItems.HEAVY_OIL_BUCKET);
                        output.accept(ModItems.TAR_BUCKET);
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
