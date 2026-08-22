package com.harderdiesel.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Общая JEI-анимация мультиблочного реактора.
 */
public abstract class AnimatedMultiblock extends AnimatedKinetics {

    private final PartialModel bottom;
    private final PartialModel middle;
    private final PartialModel top;
    private final PartialModel gauge;
    private final int scale;

    protected AnimatedMultiblock(PartialModel bottom, PartialModel middle, PartialModel top,
                                 PartialModel gauge, int scale) {
        this.bottom = bottom;
        this.middle = middle;
        this.top = top;
        this.gauge = gauge;
        this.scale = scale;
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        draw(graphics, xOffset, yOffset, 3);
    }

    public void draw(GuiGraphics graphics, int xOffset, int yOffset, int height) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 201);

        matrixStack.mulPose(Axis.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Axis.YP.rotationDegrees(22.5f));

        blockElement(bottom)
                .atLocal(0, 1, 0)
                .rotateBlock(0, 90, 0)
                .scale(scale)
                .render(graphics);
        for (int i = 0; i < height - 1; i++) {
            blockElement(middle)
                    .atLocal(0, -i, 0)
                    .rotateBlock(0, 90, 0)
                    .scale(scale)
                    .render(graphics);
        }
        blockElement(top)
                .atLocal(0, -height + 1, 0)
                .rotateBlock(0, 90, 0)
                .scale(scale)
                .render(graphics);
        blockElement(gauge).atLocal(1, 1, 0.125).rotate(0, -90, 0).scale(scale).render(graphics);
        blockElement(gauge).atLocal(1 - 0.125, 1, 1).rotate(0, 180, 0).scale(scale).render(graphics);
        matrixStack.popPose();
    }
}
