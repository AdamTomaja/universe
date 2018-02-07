package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.command.CommandRegistry;

public class AbstractScenario implements Scenario {

    protected final CommandRegistry registry = new CommandRegistry();
    protected final Player player;
    protected final String scenarioName;

    public AbstractScenario(Player player, String scenarioName) {
        this.player = player;
        this.scenarioName = scenarioName;
    }

    @Override
    public void initialize() throws Exception {
        player.trySendMessage("Welcome in " + scenarioName);
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }
}
