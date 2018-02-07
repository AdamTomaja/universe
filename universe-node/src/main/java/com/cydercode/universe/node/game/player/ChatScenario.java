package com.cydercode.universe.node.game.player;

import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.scenario.Scenario;

public class ChatScenario implements Scenario {

    private final CommandRegistry registry = new CommandRegistry();

    private final Player currentPlayer;
    private final Player remotePlayer;

    public ChatScenario(Player currentPlayer, Player remotePlayer) {
        this.currentPlayer = currentPlayer;
        this.remotePlayer = remotePlayer;
    }

    @Override
    public void initialize() throws Exception {
        currentPlayer.trySendMessage("You have chat with " + remotePlayer);
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        if (message.startsWith("#")) {
            registry.execute(currentPlayer, message.substring(1));
        } else {
            remotePlayer.trySendMessage(currentPlayer + ": " + message);
        }
    }
}
