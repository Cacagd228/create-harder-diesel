package com.harderdiesel.content.generators;

import com.harderdiesel.client.HarderDieselPartialModels;
import com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

import static com.jesz.createdieselgenerators.content.diesel_engine.modular.ModularDieselEngineBlock.FACING;

public class FueledModularDieselEngineRenderer extends ShaftRenderer<FueledModularDieselEngineBlockEntity> {

    public FueledModularDieselEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(FueledModularDieselEngineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        int angle = (int) (Math.abs(KineticBlockEntityRenderer.getAngleForBe(be, be.getBlockPos(), KineticBlockEntityRenderer.getRotationAxisOf(be)) * 180 / Math.PI) * 3 % 360) / 36;
        FuelCategory category = ((FueledModularDieselEngineBlock) be.getBlockState().getBlock()).category;

        ModularDieselEngineBlockEntity controller = be.getControllerBE();
        (controller != null ? controller : be).getUpgrade().render(be, partialTicks, ms, buffer, light);

        CachedBuffers.partial(HarderDieselPartialModels.modularPiston(category, angle), be.getBlockState())
                .center()
                .rotateYDegrees(be.getBlockState().getValue(FACING).toYRot()).uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.solid()));

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }
}
