package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloScenario implements Scenario {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloScenario.class);
    private volatile boolean waiting = false;

    private final Player player;

    public HelloScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        player.sendMessage("Hello ! Whats Your name ? ");
        waiting = true;
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        if (waiting) {
            player.setName(message);
            LOGGER.info("Player {} set name to {}", player.hashCode(), message);
            player.sendMessage("Ok, your name is " + message);
            player.startScenario(new MainMenuScenario(player));
        }
    }
}
