package com.harderdiesel;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SERVER_SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> CRACKING_MIN_HEIGHT;
    public static final ModConfigSpec.ConfigValue<Integer> SEPARATOR_MIN_HEIGHT;

    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_CRACKING_EMIT;
    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_SEPARATOR_EMIT;
    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_DISTILLATION_EMIT;
    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_DECAY_RATE;
    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_DIFFUSION_RATE;
    public static final ModConfigSpec.ConfigValue<Double> POLLUTION_GLOBAL_MULTIPLIER;

    static {
        SERVER_BUILDER.push("cracking_reactor");

        CRACKING_MIN_HEIGHT = SERVER_BUILDER.comment("Minimum height of the Cracking Reactor required to process recipes")
                .defineInRange("Cracking Reactor Minimum Height", 3, 2, 7);

        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("separator");

        SEPARATOR_MIN_HEIGHT = SERVER_BUILDER.comment("Minimum height of the Separator required to process recipes")
                .defineInRange("Separator Minimum Height", 3, 2, 8);

        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("pollution");
        // B: 0.02/tick=0.4/с → x1 1 машина T1~8мин T5~40мин; x100 2 машины T5 ~12с центр, ~2мин r=5; распад 2× медленнее
        POLLUTION_CRACKING_EMIT = SERVER_BUILDER.comment("Pollution emitted per tick by active Cracking Reactor (0..10). B: 0.02 = T1~8мин x1, T5~12с x100")
                .defineInRange("CrackingPollutionPerTick", 0.02, 0.0, 10.0);
        POLLUTION_SEPARATOR_EMIT = SERVER_BUILDER.comment("Pollution emitted per tick by active Separator")
                .defineInRange("SeparatorPollutionPerTick", 0.016, 0.0, 10.0);
        POLLUTION_DISTILLATION_EMIT = SERVER_BUILDER.comment("Pollution emitted per tick by active Distillation Tower (CDG)")
                .defineInRange("DistillationPollutionPerTick", 0.018, 0.0, 10.0);
        POLLUTION_DECAY_RATE = SERVER_BUILDER.comment("Pollution decay per diffusion tick (0..0.2). B: 0.0004 non-linear (0.5+val/1000*0.5) → 1000→0 ~80мин, 2× медленнее набора")
                .defineInRange("PollutionDecayRate", 0.0004, 0.0, 0.2);
        POLLUTION_DIFFUSION_RATE = SERVER_BUILDER.comment("Pollution diffusion per gradient (0..1). Conservative flux=diffusion*(val-neighbor)/4, no val scaling → 0.08/0.081 smooth")
                .defineInRange("PollutionDiffusionRate", 0.25, 0.0, 1.0);
        POLLUTION_GLOBAL_MULTIPLIER = SERVER_BUILDER.comment("Global multiplier for all pollution emissions (0..100). Use command /harderdiesel pollution multiplier")
                .defineInRange("GlobalPollutionMultiplier", 1.0, 0.0, 100.0);
        SERVER_BUILDER.pop();

        SERVER_SPEC = SERVER_BUILDER.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, SERVER_SPEC, HarderDiesel.MODID + "-server.toml");
    }
}
