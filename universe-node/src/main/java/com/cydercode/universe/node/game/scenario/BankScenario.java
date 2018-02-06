package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.bank.Account;
import com.cydercode.universe.node.game.bank.Bank;
import com.cydercode.universe.node.game.bank.Transfer;
import com.cydercode.universe.node.game.command.CommandRegistry;

import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class BankScenario implements Scenario {

    private final Player player;

    private CommandRegistry commandRegistry = new CommandRegistry();

    public BankScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        commandRegistry.addCommand("create", (player, command) -> {
            player.getUniverse().getBank().createAccount(player);
            player.sendMessage("Bank account created!");
        });

        commandRegistry.addCommand("balance", (player, command) -> {
            Optional<Account> accountOfPlayer = player.getUniverse().getBank().getAccountOfPlayer(player);
            accountOfPlayer.ifPresentOrElse(account -> {
                player.trySendMessage("Account balance: " + account.getBalance() + " UC");
            }, () -> {
                player.trySendMessage("You have no account in this bank!");
            });
        });

        commandRegistry.addCommand("transfer", (player, command) -> {
            Bank bank = player.getUniverse().getBank();
            String destinationPlayerName = command.getArgument(0);
            player.getUniverse()
                    .findPlayerByName(destinationPlayerName)
                    .ifPresentOrElse(destinationPlayer -> {
                        double ammount = Double.parseDouble(command.getArgument(1));
                        Transfer transfer = new Transfer(player, destinationPlayer, ammount, "");
                        bank.executeTransfer(transfer);
                        player.trySendMessage("Transfer OK");
                        destinationPlayer.trySendMessage("You`have got " + ammount + " UC from " + player);
                    }, () -> {
                        player.trySendMessage("No player with name " + destinationPlayerName);
                    });

        });

        commandRegistry.addCommand("history", (player, command) -> {
            player.getUniverse().getBank().getAccountOfPlayer(player).ifPresentOrElse(account -> {
                player.trySendMessage(account.getHistory()
                        .stream()
                        .map(Transfer::toString)
                        .collect(joining("\n")));
            }, () -> {
                player.trySendMessage("You have no account in this bank!");
            });
        });

        player.sendMessage("Welcome in Bank!");
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        commandRegistry.execute(player, message);
    }
}
