package ru.logitechno.whitelistforge;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = WhiteListForge.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class PlayerLoginHandler {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        String playerName = player.getName().getString();

        if (!Config.WHITELIST_ENABLED.get()) return;
        if (!DatabaseManager.isPlayerWhitelisted(playerName)) {
            player.connection.disconnect(Component.literal(Config.kick_reason.replace("&", "§")));
        }
    }
}

