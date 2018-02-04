package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Bank {

    private List<Account> accounts = new CopyOnWriteArrayList<>();

    private Account bankAccount = new Account(null, 9999);

    public void createAccount(Player player) {
        Optional<Account> existingAccount = getAccountOfPlayer(player);
        existingAccount.ifPresent(account -> {
            throw new IllegalArgumentException("Player " + account.getPlayer() + " has existing account!");
        });

        accounts.add(new Account(player));
    }

    public void transfer(Player source, Player destination, double ammount) {
        getAccountOfPlayer(source).ifPresentOrElse(sourceAccount -> {
            getAccountOfPlayer(destination).ifPresentOrElse(destinationAccount -> {
                if (sourceAccount.getBalance() >= ammount) {
                    sourceAccount.transferTo(destinationAccount, ammount);
                } else {
                    throw new IllegalArgumentException("No found on account");
                }
            }, () -> {
                throw new IllegalStateException("Destination player has no account!");
            });
        }, () -> {
            throw new IllegalStateException("Player " + source + " has no account");
        });
    }

    public void transfer(Player destination, double ammount) {
        getAccountOfPlayer(destination).ifPresent(destinationAccount -> {
            bankAccount.transferTo(destinationAccount, ammount);
        });
    }

    public Optional<Account> getAccountOfPlayer(Player player) {
        return accounts.stream()
                .filter(account -> account.getPlayer().equals(player))
                .findFirst();
    }

}
