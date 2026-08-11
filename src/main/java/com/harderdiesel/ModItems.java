package com.harderdiesel;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HarderDiesel.MODID);

    public static final DeferredItem<BucketItem> NAPHTHA_BUCKET = ITEMS.register("naphtha_bucket",
            () -> new BucketItem(ModFluids.NAPHTHA.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> KEROSENE_BUCKET = ITEMS.register("kerosene_bucket",
            () -> new BucketItem(ModFluids.KEROSENE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> HEAVY_OIL_BUCKET = ITEMS.register("heavy_oil_bucket",
            () -> new BucketItem(ModFluids.HEAVY_OIL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> TAR_BUCKET = ITEMS.register("tar_bucket",
            () -> new BucketItem(ModFluids.TAR.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
