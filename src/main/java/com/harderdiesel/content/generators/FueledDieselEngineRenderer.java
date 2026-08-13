package com.harderdiesel.content.generators;

import com.harderdiesel.client.HarderDieselPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;

import static com.jesz.createdieselgenerators.content.diesel_engine.normal.DieselEngineBlock.FACING;

public class FueledDieselEngineRenderer extends ShaftRenderer<FueledDieselEngineBlockEntity> {

    public FueledDieselEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(FueledDieselEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        int angle = (int) (Math.abs(KineticBlockEntityRenderer.getAngleForBe(be, be.getBlockPos(), KineticBlockEntityRenderer.getRotationAxisOf(be)) * 180 / Math.PI) * 3 % 360) / 36;
        FuelCategory category = ((FueledDieselEngineBlock) be.getBlockState().getBlock()).category;

        be.getUpgrade().render(be, partialTicks, ms, buffer, light);

        if (be.getBlockState().getValue(FACING).getAxis().isHorizontal()) {
            CachedBuffers.partial(HarderDieselPartialModels.enginePiston(category, angle, false), be.getBlockState())
                    .center()
                    .rotateYDegrees(be.getBlockState().getValue(FACING).toYRot()).uncenter()
                    .light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        } else {
            CachedBuffers.partial(HarderDieselPartialModels.enginePiston(category, angle, true), be.getBlockState())
                    .center().rotateYDegrees(be.getBlockState().getValue(FACING) == Direction.DOWN ? 180 : 270).rotateZDegrees(be.getBlockState().getValue(FACING) == Direction.DOWN ? 180 : 0).uncenter()
                    .light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }
}
