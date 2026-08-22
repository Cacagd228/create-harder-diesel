package com.harderdiesel.client;

import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CrackingReactorRenderer extends AbstractReactorRenderer<CrackingReactorBlockEntity> {

    public CrackingReactorRenderer(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected PartialModel gauge() {
        return HarderDieselPartialModels.CRACKING_REACTOR_GAUGE;
    }
}
