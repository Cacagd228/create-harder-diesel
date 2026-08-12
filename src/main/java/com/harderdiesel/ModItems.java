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

    public static final DeferredItem<BucketItem> LOW_OCTANE_GASOLINE_BUCKET = ITEMS.register("low_octane_gasoline_bucket",
            () -> new BucketItem(ModFluids.LOW_OCTANE_GASOLINE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> HIGH_OCTANE_GASOLINE_BUCKET = ITEMS.register("high_octane_gasoline_bucket",
            () -> new BucketItem(ModFluids.HIGH_OCTANE_GASOLINE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> ARTISAN_HIGH_OCTANE_GASOLINE_BUCKET = ITEMS.register("artisan_high_octane_gasoline_bucket",
            () -> new BucketItem(ModFluids.ARTISAN_HIGH_OCTANE_GASOLINE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> NITROMETHANE_BUCKET = ITEMS.register("nitromethane_bucket",
            () -> new BucketItem(ModFluids.NITROMETHANE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> PROPANE_BUCKET = ITEMS.register("propane_bucket",
            () -> new BucketItem(ModFluids.PROPANE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> MIXED_NITROALKANES_BUCKET = ITEMS.register("mixed_nitroalkanes_bucket",
            () -> new BucketItem(ModFluids.MIXED_NITROALKANES.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> NITROETHANE_BUCKET = ITEMS.register("nitroethane_bucket",
            () -> new BucketItem(ModFluids.NITROETHANE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> NITROPROPANE_BUCKET = ITEMS.register("nitropropane_bucket",
            () -> new BucketItem(ModFluids.NITROPROPANE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> LOW_CETANE_DIESEL_BUCKET = ITEMS.register("low_cetane_diesel_bucket",
            () -> new BucketItem(ModFluids.LOW_CETANE_DIESEL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> MEDIUM_CETANE_DIESEL_BUCKET = ITEMS.register("medium_cetane_diesel_bucket",
            () -> new BucketItem(ModFluids.MEDIUM_CETANE_DIESEL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> HIGH_CETANE_DIESEL_BUCKET = ITEMS.register("high_cetane_diesel_bucket",
            () -> new BucketItem(ModFluids.HIGH_CETANE_DIESEL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> MAZUT_BUCKET = ITEMS.register("mazut_bucket",
            () -> new BucketItem(ModFluids.MAZUT.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
