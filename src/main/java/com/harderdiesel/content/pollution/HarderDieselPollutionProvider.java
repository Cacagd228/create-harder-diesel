package com.harderdiesel.content.pollution;

import com.slavav.xaeroszones.api.pollution.IPollutionProvider;
import com.slavav.xaeroszones.api.pollution.PollutionColorGradient;
import com.slavav.xaeroszones.client.pollution.ClientPollutionData;

/**
 * Клиентский провайдер для XaerosZones. Читает кэш ClientPollutionData, который заполняется пакетами SyncPollution*.
 */
public class HarderDieselPollutionProvider implements IPollutionProvider {

    private static final PollutionColorGradient GRADIENT = PollutionColorGradient.FACTORIO_RED;

    @Override
    public float getPollution(String dimensionId, int chunkX, int chunkZ) {
        try {
            return ClientPollutionData.getPollution(dimensionId, chunkX, chunkZ);
        } catch (Throwable t) {
            return 0F;
        }
    }

    @Override
    public PollutionColorGradient getGradient() {
        return GRADIENT;
    }

    @Override
    public float getVisibilityThreshold() {
        return 1.0F;
    }

    @Override
    public float getMaxPollutionReference() {
        return 1000.0F;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
