package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.bank.Account;
import com.cydercode.universe.node.game.bank.Bank;
import com.cydercode.universe.node.game.bank.Transfer;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.player.Player;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;

public class BankScenario implements Scenario {

    private final Player player;

    private CommandRegistry commandRegistry = new CommandRegistry();

    public BankScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        commandRegistry.addCommand(newCommand()
                .withName("create")
                .withExecutor((player, command) -> {
                    player.getUniverse().getBank().createAccount(player.getPlayerRow());
                    player.sendMessage("Bank account created!");
                })
                .build());

        commandRegistry.addCommand(newCommand()
                .withName("balance")
                .withExecutor((player, command) -> {
                    Optional<Account> accountOfPlayer = player.getUniverse().getBank().getAccountOfPlayer(player);
                    accountOfPlayer.ifPresentOrElse(account -> {
                        player.trySendMessage("Account balance: " + account.getBalance() + " UC");
                    }, () -> {
                        player.trySendMessage("You have no account in this bank!");
                    });
                })
                .build());

        commandRegistry.addCommand(newCommand()
                .withName("transfer")
                .withExecutor((player, command) -> {
                    Bank bank = player.getUniverse().getBank();
                    String destinationPlayerName = command.getArgument(0);
                    player.getUniverse()
                            .findPlayerByName(destinationPlayerName)
                            .ifPresentOrElse(destinationPlayer -> {
                                double ammount = Double.parseDouble(command.getArgument(1));
                                Transfer transfer = new Transfer(player.getPlayerRow(), destinationPlayer.getPlayerRow(), ammount, "");
                                bank.executeTransfer(transfer);
                                player.trySendMessage("Transfer OK");
                                destinationPlayer.trySendMessage("You`have got " + ammount + " UC from " + player);
                            }, () -> {
                                player.trySendMessage("No player with name " + destinationPlayerName);
                            });

                })
                .build());

        commandRegistry.addCommand(newCommand()
                .withName("history")
                .withExecutor((player, command) -> {
                    player.getUniverse().getBank().getAccountOfPlayer(player.getPlayerRow()).ifPresentOrElse(account -> {
                        player.trySendMessage("source,destination,ammount,title");
                        player.trySendMessage(account.getHistory()
                                .stream()
                                .map(transfer -> String.format("%s,%s,%s,%s",
                                        transfer.getSource(),
                                        transfer.getDestination(),
                                        transfer.getAmmount(),
                                        transfer.getTitle()))
                                .collect(Collectors.joining("\n")));
                    }, () -> {
                        player.trySendMessage("You have no account in this bank!");
                    });
                }).build());

        player.sendMessage("Welcome in Bank!");
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        commandRegistry.execute(player, message);
    }
}
