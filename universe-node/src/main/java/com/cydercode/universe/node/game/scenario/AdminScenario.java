package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.bank.Transfer;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.player.Player;

import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;

public class AdminScenario implements Scenario {

    private final Player player;

    private final CommandRegistry registry = new CommandRegistry();

    public AdminScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        player.trySendMessage("Walcome in Admin mode!");

        registry.addCommand(newCommand()
                .withName("earn")
                .withExecutor((player, command) -> {
                    player.getUniverse().getBank().executeTransfer(new Transfer(player.getUniverse().getBank().getBankUser(),
                            player.getPlayerRow(),
                            Double.parseDouble(command.getArgument(0)),
                            "Admin Transfer"));
                    player.sendMessage("You earned!");
                })
                .build());
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }
}
