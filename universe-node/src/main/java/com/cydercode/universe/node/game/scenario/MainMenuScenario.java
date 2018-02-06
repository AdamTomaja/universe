package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.Vector2D;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.command.ParsedCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class MainMenuScenario implements Scenario {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuScenario.class);
    private final Player player;

    private CommandRegistry registry = new CommandRegistry();

    public MainMenuScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() {
        player.trySendMessage("Welcome in Main Menu");

        registry.addCommand("go", (player, command) -> {
            switch (command.getArgument(0)) {
                case "up":
                    movePlayer(player, Vector2D.UP);
                    break;
                case "down":
                    movePlayer(player, Vector2D.DOWN);
                    break;
                case "left":
                    movePlayer(player, Vector2D.LEFT);
                    break;
                case "right":
                    movePlayer(player, Vector2D.RIGHT);
                    break;
            }
        });

        registry.addCommand("tell", (player, command) -> {
            player.getUniverse().findPlayerByName(command.getArgument(0)).ifPresent(otherPlayer -> {
                try {
                    otherPlayer.sendMessage(player.getName() + ":> " + command.joinArguments(1));
                } catch (IOException e) {
                    LOGGER.error("Unable to send private message", e);
                }
            });
        });

        registry.addCommand("players", (player, command) -> {
            player.sendMessage("Players list: " + player.getUniverse().getPlayersNames().stream().collect(joining(", ")));
        });

        registry.addCommand("player", (player, command) -> {
            String searchedName = command.getArgument(0);
            Optional<Player> otherPlayer = player.getUniverse().findPlayerByName(searchedName);
            if (!otherPlayer.isPresent()) {
                player.sendMessage("There is no player " + searchedName);
                return;
            }

            player.sendMessage("Distance from " + otherPlayer.get().getName() + " is " + player.getPosition().distance(otherPlayer.get().getPosition()));
        });

        registry.addCommand("bank", (player, command) -> {
            player.startScenario(new BankScenario(player));
        });

        registry.addCommand("shop", (player, command) -> {
            player.startScenario(new ShopScenario(player));
        });

        registry.addCommand("admin", (player, command) -> {
            player.startScenario(new AdminScenario(player));
        });

        registry.addCommand("chat", (player, command) -> {
            player.getUniverse().findPlayerByName(command.getArgument(0)).ifPresentOrElse(remotePlayer -> {
                try {
                    player.startScenario(new ChatScenario(player, remotePlayer));
                    remotePlayer.startScenario(new ChatScenario(remotePlayer, player));
                } catch (Exception e) {
                    LOGGER.error("Unable to start scenario", e);
                }
            }, () -> {
                player.trySendMessage("Player not found");
            });
        });

    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
        ParsedCommand parsedCommand = ParsedCommand.parse(message);
    }

    private void movePlayer(Player player, Vector2D movement) throws IOException {
        player.setPosition(player.getPosition().add(movement));
        player.sendMessage("Your current position is " + player.getPosition());
        LOGGER.info("Player {} moved to {}", player, player.getPosition());
    }
}
