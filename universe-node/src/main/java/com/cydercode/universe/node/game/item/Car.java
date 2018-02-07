package com.cydercode.universe.node.game.item;

import com.cydercode.universe.node.game.Vector2D;
import com.cydercode.universe.node.game.command.CommandDescription;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.scenario.Scenario;

public class Car implements Item, Scenario {

    private CommandRegistry registry;

    private final String name;

    public Car(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void initialize(Player player) throws Exception {
        registry = new CommandRegistry();
        registry.addCommand(CommandDescription.newCommand()
                .withName("drive")
                .withParameter("x", Integer.class, false)
                .withParameter("y", Integer.class, false)
                .withExecutor((currentPlayer, command) -> {
                    currentPlayer.setPosition(new Vector2D(Integer.parseInt(command.getArgument(0)), Integer.parseInt(command.getArgument(1))));
                    currentPlayer.trySendMessage("Your current position: " + currentPlayer.getPosition());
                })
                .build());

        player.trySendMessage("Type drive [x] [y] to drive to given coordinates");
    }

    @Override
    public void receiveMessage(Player player, String message) throws Exception {
        registry.execute(player, message);
    }
}
