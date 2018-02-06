package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Bank {

    private List<Account> accounts = new CopyOnWriteArrayList<>();

    private final Account bankAccount = new Account(null, 10000);

    public void createAccount(Player player) {
        Optional<Account> existingAccount = getAccountOfPlayer(player);
        existingAccount.ifPresent(account -> {
            throw new IllegalArgumentException("Player " + account.getPlayer() + " has existing account!");
        });

        accounts.add(new Account(player));
    }

    public Optional<Account> getAccountOfPlayer(Player player) {
        return accounts.stream()
                .filter(account -> account.getPlayer().equals(player))
                .findFirst();
    }

    public void executeTransfer(Transfer transfer) {
        getAccountOfPlayer(transfer.getSource()).ifPresentOrElse(sourceAccount -> {
            getAccountOfPlayer(transfer.getDestination()).ifPresentOrElse(destinationAccount -> {
                sourceAccount.transfer(destinationAccount, transfer);
            }, () -> {
                throw new IllegalStateException("Destination player has no account!");
            });
        }, () -> {
            throw new IllegalStateException("Player " + transfer.getSource() + " has no account");
        });
    }

    public void executeTransfer(Player player, double ammount, String title) {
        Transfer transfer = new Transfer(null, player, ammount, title);
        getAccountOfPlayer(player).ifPresent(account -> {
            bankAccount.transfer(account, transfer);
        });
    }

    public void executeTransfer(Player source, Account destinationAccount, double ammount, String title) {
        Transfer transfer = new Transfer(source, null, ammount, title);
        getAccountOfPlayer(source).ifPresentOrElse(account -> {
            account.transfer(destinationAccount, transfer);
        }, () -> {
            throw new IllegalStateException(source + " has no account!");
        });
    }
}
