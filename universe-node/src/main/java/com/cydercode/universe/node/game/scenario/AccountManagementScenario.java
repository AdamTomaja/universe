package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.command.CommandDescription;
import com.cydercode.universe.node.game.database.PlayerRow;

import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;
import static java.util.Optional.of;

public class AccountManagementScenario extends AbstractScenario {
    public AccountManagementScenario(Player scenarioPlayer) {
        super(scenarioPlayer, "Account Management");

        registry.addCommand(newCommand()
                .withName("register")
                .withExecutor((player, command) -> {
                    PlayerRow row = player.getUniverse().getPlayersDatabase().createUser(command.getArgument(0), command.getArgument(1));
                    player.trySendMessage("Your id: " + row.getId());
                })
                .withParameter("username", String.class, false)
                .withParameter("password", String.class, false)
                .build());

        registry.addCommand(CommandDescription.newCommand()
                .withName("login")
                .withExecutor((player, command) -> {
                    PlayerRow row = player.getUniverse().getPlayersDatabase().login(command.getArgument(0), command.getArgument(1));
                    player.trySendMessage("You logged in with id " + row.getId());
                    player.setPlayerRow(of(row));
                })
                .build());
    }
}
