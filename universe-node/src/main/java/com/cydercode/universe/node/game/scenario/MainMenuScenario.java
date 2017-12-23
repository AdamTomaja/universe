package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.Vector2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class MainMenuScenario implements Scenario {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuScenario.class);
    private final Player player;

    public MainMenuScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {

    }

    @Override
    public void receiveMessage(String message) throws Exception {
        switch (message) {
            case "players":
                player.sendMessage("Players list: " + player.getUniverse().getPlayersNames().stream().collect(joining(", ")));
                break;
            case "go up":
                movePlayer(player, Vector2D.UP);
                break;
            case "go down":
                movePlayer(player, Vector2D.DOWN);
                break;
            case "go left":
                movePlayer(player, Vector2D.LEFT);
                break;
            case "go right":
                movePlayer(player, Vector2D.RIGHT);
                break;
        }

        if (message.startsWith("player")) {
            String searchedName = message.split(" ")[1];
            Optional<Player> otherPlayer = player.getUniverse().findPlayerByName(searchedName);
            if (!otherPlayer.isPresent()) {
                player.sendMessage("There is no player " + searchedName);
                return;
            }

            player.sendMessage("Distance from " + otherPlayer.get().getName() + " is " + player.getPosition().distance(otherPlayer.get().getPosition()));
        }

        if (message.startsWith("tell")) {
            String[] splitted = message.split(" ");
            player.getUniverse().findPlayerByName(splitted[1]).ifPresent(otherPlayer -> {
                try {
                    otherPlayer.sendMessage(player.getName() + ":> " + asList(splitted)
                            .subList(2, splitted.length - 1)
                            .stream()
                            .collect(joining(" ")));
                } catch (IOException e) {
                    LOGGER.error("Unable to send private message", e);
                }
            });
        }


    }

    private void movePlayer(Player player, Vector2D movement) throws IOException {
        player.setPosition(player.getPosition().add(movement));
        player.sendMessage("Your current position is " + player.getPosition());
        LOGGER.info("Player {} moved to {}", player, player.getPosition());
    }
}
