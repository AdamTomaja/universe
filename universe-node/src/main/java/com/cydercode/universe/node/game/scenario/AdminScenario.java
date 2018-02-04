package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.command.CommandRegistry;

public class AdminScenario implements Scenario {

    private final Player player;

    private final CommandRegistry registry = new CommandRegistry();

    public AdminScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        player.trySendMessage("Walcome in Admin mode!");

        registry.addCommand("earn", (player, command) -> {
            player.getUniverse().getBank().transfer(player, Double.parseDouble(command.getArgument(0)));
            player.sendMessage("You earned!");
        });
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }
}
