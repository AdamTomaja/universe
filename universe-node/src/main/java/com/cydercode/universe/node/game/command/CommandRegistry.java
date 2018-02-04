package com.cydercode.universe.node.game.command;

import com.cydercode.universe.node.game.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

public class CommandRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);

    private Map<String, CommandExecutor> commands = new HashMap<>();

    public void addCommand(String command, CommandExecutor executor) {
        commands.put(command, executor);
    }

    public void execute(Player player, String command) {
        ParsedCommand parsedCommand = ParsedCommand.parse(command);

        ofNullable(commands.get(parsedCommand.getModule()))
                .ifPresentOrElse(executor -> {
                    try {
                        executor.execute(player, parsedCommand);
                    } catch (Exception e) {
                        LOGGER.error("Unable to execute command of player {}", player, e);
                        player.trySendMessage("Comamnd error: " + e.getMessage());
                    }
                }, () -> {
                    player.trySendMessage("Unknown command");
                });
    }
}
