package com.harderdiesel;

import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfig {
    public static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SERVER_SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> CRACKING_MIN_HEIGHT;
    public static final ModConfigSpec.ConfigValue<Integer> SEPARATOR_MIN_HEIGHT;

    static {
        SERVER_BUILDER.push("cracking_reactor");

        CRACKING_MIN_HEIGHT = SERVER_BUILDER.comment("Minimum height of the Cracking Reactor required to process recipes")
                .defineInRange("Cracking Reactor Minimum Height", 3, 2, 7);

        SERVER_BUILDER.pop();

        SERVER_BUILDER.push("separator");

        SEPARATOR_MIN_HEIGHT = SERVER_BUILDER.comment("Minimum height of the Separator required to process recipes")
                .defineInRange("Separator Minimum Height", 3, 2, 8);

        SERVER_BUILDER.pop();
        SERVER_SPEC = SERVER_BUILDER.build();
    }

    public static void register(ModContainer container) {
        container.registerConfig(net.neoforged.fml.config.ModConfig.Type.SERVER, SERVER_SPEC, HarderDiesel.MODID + "-server.toml");
    }
}
