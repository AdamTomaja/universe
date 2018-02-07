package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.player.Player;

public interface Scenario {

    default void initialize(Player player) throws Exception {
        initialize();
    }

    default void initialize() throws Exception {

    }

    default void receiveMessage(Player player, String message) throws Exception {
        receiveMessage(message);
    }

    default void receiveMessage(String message) throws Exception {

    }
}
