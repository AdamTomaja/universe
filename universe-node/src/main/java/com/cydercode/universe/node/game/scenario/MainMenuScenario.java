package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;

import static java.util.stream.Collectors.joining;

public class MainMenuScenario implements Scenario {

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
        }
    }
}
