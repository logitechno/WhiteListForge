package ru.logitechno.whitelistforge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = WhiteListForge.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue WHITELIST_ENABLED = BUILDER
            .define("whitelist_enabled", true);

    public static final ForgeConfigSpec.ConfigValue<String> WHITELIST_KICK_REASON = BUILDER
            .define("kick_reason", "§4You are not whitelist!");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String kick_reason;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        kick_reason = WHITELIST_KICK_REASON.get();
    }
}
