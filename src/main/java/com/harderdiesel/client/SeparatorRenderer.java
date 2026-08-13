package com.harderdiesel.client;

import com.harderdiesel.content.separator.SeparatorBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.platform.NeoForgeCatnipServices;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class SeparatorRenderer extends SafeBlockEntityRenderer<SeparatorBlockEntity> {
    public SeparatorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(SeparatorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        if (be.isController() && be.isBottom()) {
            renderAsBoiler(be, partialTicks, ms, buffer, light, overlay);
            return;
        }

        if (be.isBottom())
            return;

        // Render the fluid of an output layer as a single box spanning the whole
        // layer footprint. Only the layer's representative block (the block right
        // above the controller) draws the fluid, so a 2x2/3x3 layer looks like one
        // continuous tank instead of separate per-block pools.
        if (be.getLevel() == null)
            return;
        if (!be.isOutputLayerRepresentative())
            return;

        IFluidHandler fluids = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
        if (!(fluids instanceof SeparatorBlockEntity.SeparatorFluidHandler handler))
            return;

        FluidStack fluidStack = firstNonEmpty(handler);
        if (fluidStack.isEmpty())
            return;

        LerpedFloat fluidLevel = be.getFluidLevel();
        float level = fluidLevel != null ? fluidLevel.getValue(partialTicks) : 0;
        level = Mth.clamp(level, 0, 1);

        // Hollow block interior: x 1-15, z 1-15, y 4-12, scaled to the layer width
        SeparatorBlockEntity controllerBE = be.getControllerBE();
        int width = controllerBE != null ? controllerBE.getWidth() : 1;
        float xMin = 1 / 16f;
        float zMin = 1 / 16f;
        float xMax = width - 1 / 16f;
        float zMax = width - 1 / 16f;
        float yMin = 4 / 16f;
        float yMax = yMin + level * (8 / 16f);

        if (yMax <= yMin)
            return;

        ms.pushPose();
        NeoForgeCatnipServices.FLUID_RENDERER.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, buffer,
                ms, light, false, true);
        ms.popPose();
    }

    private FluidStack firstNonEmpty(SeparatorBlockEntity.SeparatorFluidHandler handler) {
        for (int i = 0; i < handler.getTanks(); i++) {
            FluidStack stack = handler.getFluidInTank(i);
            if (!stack.isEmpty())
                return stack;
        }
        return FluidStack.EMPTY;
    }

    protected void renderAsBoiler(SeparatorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                                  int light, int overlay) {
        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutout());
        ms.pushPose();
        TransformStack msr = TransformStack.of(ms);
        msr.translate(be.getWidth() / 2f, 0.5, be.getWidth() / 2f);

        float dialPivotY = 6f / 16;
        float dialPivotZ = 8f / 16;
        float progress = Mth.clamp(be.currentRecipe == null ? be.progress : (be.processingTime - partialTicks) / be.currentRecipe.getProcessingDuration(), 0, 1);

        for (Direction d : Iterate.horizontalDirections) {
            ms.pushPose();
            CachedBuffers.partial(HarderDieselPartialModels.SEPARATOR_GAUGE, blockState)
                    .rotateYDegrees(d.toYRot())
                    .uncenter()
                    .translate(be.getWidth() / 2f - 6 / 16f, 0, 0)
                    .light(light)
                    .renderInto(ms, vb);
            CachedBuffers.partial(AllPartialModels.BOILER_GAUGE_DIAL, blockState)
                    .rotateYDegrees(d.toYRot())
                    .uncenter()
                    .translate(be.width / 2f - 6 / 16f, 0, 0)
                    .translate(0, dialPivotY, dialPivotZ)
                    .rotateXDegrees(-145 * progress + 90)
                    .translate(0, -dialPivotY, -dialPivotZ)
                    .light(light)
                    .renderInto(ms, vb);
            ms.popPose();
        }

        ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(SeparatorBlockEntity be) {
        return be.isController() || be.isOutputLayerRepresentative();
    }
}
