package com.harderdiesel.client;

import com.harderdiesel.content.separator.SeparatorBlockEntity;
import com.harderdiesel.client.HarderDieselPartialModels;

public class AnimatedSeparator extends AnimatedMultiblock {

    public AnimatedSeparator() {
        super(HarderDieselPartialModels.JEI_SEPARATOR_BOTTOM,
                HarderDieselPartialModels.JEI_SEPARATOR_MIDDLE,
                HarderDieselPartialModels.JEI_SEPARATOR_TOP,
                HarderDieselPartialModels.SEPARATOR_GAUGE,
                18);
    }
}
