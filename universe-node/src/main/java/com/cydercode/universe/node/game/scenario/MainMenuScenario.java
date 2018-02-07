package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.Vector2D;
import com.cydercode.universe.node.game.command.CommandDescription;
import com.cydercode.universe.node.game.command.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.cydercode.universe.node.game.command.CommandDescription.forScenario;
import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;
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
        player.trySendMessage("Type \"help\" to get more informations");

        registry.addCommand(newCommand()
                .withName("go")
                .withExecutor((player, command) -> {
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
                })
                .build());

        registry.addCommand(newCommand()
                .withName("tell")
                .withExecutor((player, command) -> {
                    player.getUniverse().findPlayerByName(command.getArgument(0)).ifPresent(otherPlayer -> {
                        try {
                            otherPlayer.sendMessage(player.getName() + ":> " + command.joinArguments(1));
                        } catch (IOException e) {
                            LOGGER.error("Unable to send private message", e);
                        }
                    });
                }).build());


        registry.addCommand(CommandDescription.newCommand()
                .withName("players")
                .withExecutor((player, command) -> player.sendMessage("Players list: " + player.getUniverse()
                        .getPlayersNames()
                        .stream()
                        .collect(joining(", "))))
                .build());


        registry.addCommand(forScenario("bank", new BankScenario(player)));
        registry.addCommand(forScenario("shop", new ShopScenario(player)));
        registry.addCommand(forScenario("admin", new AdminScenario(player)));
        registry.addCommand(forScenario("account", new AccountManagementScenario(player)));
        
        registry.addCommand(CommandDescription.newCommand().withName("chat").withExecutor((player, command) -> {
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
        }).build());
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }

    private void movePlayer(Player player, Vector2D movement) throws IOException {
        player.setPosition(player.getPosition().add(movement));
        player.sendMessage("Your current position is " + player.getPosition());
        LOGGER.info("Player {} moved to {}", player, player.getPosition());
    }
}
