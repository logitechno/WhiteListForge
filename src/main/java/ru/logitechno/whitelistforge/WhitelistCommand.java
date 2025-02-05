package ru.logitechno.whitelistforge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.List;


public class WhitelistCommand {
    private static final String darkGreen = "§2";
    private static final String darkRed = "§4";
    private static final String gray = "§7";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wl")
                .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(() -> Component.literal(gray + "Whitelist: " + Config.WHITELIST_ENABLED.get()), true);
                            return 1;
                        })
                .then(Commands.literal("add")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(ctx -> {
                                    String playerName = StringArgumentType.getString(ctx, "player");
                                    DatabaseManager.addPlayer(playerName);
                                    ctx.getSource().sendSuccess(() -> Component.literal(darkGreen + playerName + " added to the whitelist"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .executes(ctx -> {
                                    String playerName = StringArgumentType.getString(ctx, "player");

                                    if (!DatabaseManager.isPlayerWhitelisted(playerName)) {
                                        ctx.getSource().sendSuccess(() -> Component.literal(darkRed + playerName + " was not whitelisted"), true);
                                        return 1;
                                    }

                                    DatabaseManager.removePlayer(playerName);
                                    ctx.getSource().sendSuccess(() -> Component.literal(darkRed + playerName + " removed from the whitelist"), true);
                                    return 1;
                                })
                        )
                )
                .then(Commands.literal("list")
                        .executes(ctx -> {
                            List<String> players = DatabaseManager.getAllWhitelistedPlayers();

                            if (players.isEmpty()) {
                                ctx.getSource().sendSuccess(() -> Component.literal(gray + "The whitelist is empty"), false);
                            } else {
                                StringBuilder playerList = new StringBuilder(gray + "Whitelisted players:\n");
                                for (String player : players) {
                                    playerList.append(gray + "- ").append(player).append("\n");
                                }
                                ctx.getSource().sendSuccess(() -> Component.literal(playerList.toString()), false);
                            }
                            return 1;
                        })
                )
                .then(Commands.literal("on")
                        .executes(ctx -> {
                            Config.WHITELIST_ENABLED.set(true);
                            Config.SPEC.save();
                            ctx.getSource().sendSuccess(() -> Component.literal(darkGreen + "Whitelist has been enabled"), true);
                            return 1;
                        })
                )
                .then(Commands.literal("off")
                        .executes(ctx -> {
                            Config.WHITELIST_ENABLED.set(false);
                            Config.SPEC.save();
                            ctx.getSource().sendSuccess(() -> Component.literal(darkRed + "Whitelist has been disabled"), true);
                            return 1;
                        })
                )
        );
    }
}