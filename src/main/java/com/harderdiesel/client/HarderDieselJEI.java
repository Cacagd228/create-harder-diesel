package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.ModItems;
import com.harderdiesel.ModRecipeTypes;
import com.harderdiesel.content.cracking.CrackingRecipe;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class HarderDieselJEI implements IModPlugin {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HarderDiesel.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    private final List<CreateRecipeCategory<?>> allCategories = new ArrayList<>();

    private void loadCategories() {
        allCategories.clear();

        CreateRecipeCategory<CrackingRecipe> cracking = new CreateRecipeCategory.Builder<>(CrackingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.CRACKING)
                .catalyst(ModItems.CRACKING_CONTROLLER::get)
                .doubleItemIcon(com.jesz.createdieselgenerators.CDGBlocks.OIL_BARREL.get(), ModItems.CRACKING_CONTROLLER.get())
                .emptyBackground(177, 200)
                .build("cracking", CrackingCategory::new);

        allCategories.add(cracking);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(allCategories.toArray(CreateRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        allCategories.forEach(c -> c.registerRecipes(registration));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        allCategories.forEach(c -> c.registerCatalysts(registration));
    }
}
