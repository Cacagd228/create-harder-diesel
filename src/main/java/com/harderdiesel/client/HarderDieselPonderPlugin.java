package com.harderdiesel.client;

import com.harderdiesel.HarderDiesel;
import com.harderdiesel.ModItems;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class HarderDieselPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return HarderDiesel.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderPlugin.super.registerScenes(helper);

        helper.forComponents(ModItems.CRACKING_CONTROLLER.getId())
                .addStoryBoard("cracking_reactor", CrackingReactorScene::scene);

        helper.forComponents(ModItems.SEPARATOR_CONTROLLER.getId())
                .addStoryBoard("separator", SeparatorScene::scene);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderPlugin.super.registerTags(helper);
    }
}
