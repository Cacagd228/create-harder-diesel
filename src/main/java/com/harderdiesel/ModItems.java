package com.harderdiesel;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HarderDiesel.MODID);

    public static final DeferredItem<Item> COKE_COAL = ITEMS.register("coke_coal",
            () -> new Item(new Item.Properties()));

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

    public static final DeferredItem<BucketItem> BUTANE_GAS_BUCKET = ITEMS.register("butane_gas_bucket",
            () -> new BucketItem(ModFluids.BUTANE_GAS.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> BUTANE_LIQUID_BUCKET = ITEMS.register("butane_liquid_bucket",
            () -> new BucketItem(ModFluids.BUTANE_LIQUID.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> LPG_GAS_BUCKET = ITEMS.register("lpg_gas_bucket",
            () -> new BucketItem(ModFluids.LPG_GAS.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> LPG_LIQUID_BUCKET = ITEMS.register("lpg_liquid_bucket",
            () -> new BucketItem(ModFluids.LPG_LIQUID.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> WET_GAS_BUCKET = ITEMS.register("wet_gas_bucket",
            () -> new BucketItem(ModFluids.WET_GAS.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> PROPANE_LIQUID_BUCKET = ITEMS.register("propane_liquid_bucket",
            () -> new BucketItem(ModFluids.PROPANE_LIQUID.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> VACUUM_GAS_OIL_BUCKET = ITEMS.register("vacuum_gas_oil_bucket",
            () -> new BucketItem(ModFluids.VACUUM_GAS_OIL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> VACUUM_RESIDUE_BUCKET = ITEMS.register("vacuum_residue_bucket",
            () -> new BucketItem(ModFluids.VACUUM_RESIDUE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> CRACKED_NAPHTHA_BUCKET = ITEMS.register("cracked_naphtha_bucket",
            () -> new BucketItem(ModFluids.CRACKED_NAPHTHA.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> LIGHT_CYCLE_OIL_BUCKET = ITEMS.register("light_cycle_oil_bucket",
            () -> new BucketItem(ModFluids.LIGHT_CYCLE_OIL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> FCC_GAS_BUCKET = ITEMS.register("fcc_gas_bucket",
            () -> new BucketItem(ModFluids.FCC_GAS.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> ALKYLATE_BUCKET = ITEMS.register("alkylate_bucket",
            () -> new BucketItem(ModFluids.ALKYLATE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> GLYCEROL_BUCKET = ITEMS.register("glycerol_bucket",
            () -> new BucketItem(ModFluids.GLYCEROL.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> NITROGLYCERIN_BUCKET = ITEMS.register("nitroglycerin_bucket",
            () -> new BucketItem(ModFluids.NITROGLYCERIN.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> REFORMATE_BUCKET = ITEMS.register("reformate_bucket",
            () -> new BucketItem(ModFluids.REFORMATE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> BENZENE_BUCKET = ITEMS.register("benzene_bucket",
            () -> new BucketItem(ModFluids.BENZENE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> TOLUENE_BUCKET = ITEMS.register("toluene_bucket",
            () -> new BucketItem(ModFluids.TOLUENE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static final DeferredItem<BucketItem> XYLENE_BUCKET = ITEMS.register("xylene_bucket",
            () -> new BucketItem(ModFluids.XYLENE.get(),
                    new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
