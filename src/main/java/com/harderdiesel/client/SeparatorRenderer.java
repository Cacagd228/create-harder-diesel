package com.harderdiesel.client;

import com.harderdiesel.content.separator.SeparatorBlockEntity;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class SeparatorRenderer extends AbstractReactorRenderer<SeparatorBlockEntity> {

    public SeparatorRenderer(net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected PartialModel gauge() {
        return HarderDieselPartialModels.SEPARATOR_GAUGE;
    }
}
