package com.harderdiesel.client;

import com.harderdiesel.client.HarderDieselPartialModels;
import com.harderdiesel.content.cracking.CrackingReactorBlockEntity;

public class AnimatedCrackingReactor extends AnimatedMultiblock {

    public AnimatedCrackingReactor() {
        super(HarderDieselPartialModels.JEI_REACTOR_BOTTOM,
                HarderDieselPartialModels.JEI_REACTOR_MIDDLE,
                HarderDieselPartialModels.JEI_REACTOR_TOP,
                HarderDieselPartialModels.CRACKING_REACTOR_GAUGE,
                23);
    }
}
