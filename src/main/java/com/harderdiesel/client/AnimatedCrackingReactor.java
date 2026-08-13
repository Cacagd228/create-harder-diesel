package com.harderdiesel.client;

import com.harderdiesel.client.HarderDieselPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.gui.GuiGraphics;

public class AnimatedCrackingReactor extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        draw(graphics, xOffset, yOffset, 3);
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset, int height) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 201);

        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));
        int scale = 23;

        blockElement(HarderDieselPartialModels.JEI_REACTOR_BOTTOM)
                .atLocal(0, 1, 0)
                .rotateBlock(0, 90, 0)
                .scale(scale)
                .render(graphics);
        for (int i = 0; i < height - 1; i++) {
            blockElement(HarderDieselPartialModels.JEI_REACTOR_MIDDLE)
                    .atLocal(0, -i, 0)
                    .rotateBlock(0, 90, 0)
                    .scale(scale)
                    .render(graphics);
        }
        blockElement(HarderDieselPartialModels.JEI_REACTOR_TOP)
                .atLocal(0, -height + 1, 0)
                .rotateBlock(0, 90, 0)
                .scale(scale)
                .render(graphics);
        blockElement(HarderDieselPartialModels.CRACKING_REACTOR_GAUGE).atLocal(1, 1, 0.125).rotate(0, -90, 0).scale(scale).render(graphics);
        blockElement(HarderDieselPartialModels.CRACKING_REACTOR_GAUGE).atLocal(1 - 0.125, 1, 1).rotate(0, 180, 0).scale(scale).render(graphics);
        matrixStack.popPose();
    }
}
