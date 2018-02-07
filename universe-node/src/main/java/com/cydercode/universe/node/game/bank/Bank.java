package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.database.Database;
import com.cydercode.universe.node.game.database.PlayerRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Bank {

    private static final String USERNAME = Bank.class.getName();
    private static final String PASSWORD = "ashd8j9qy378y28frey7y43y87";

    private List<Account> accounts = new CopyOnWriteArrayList<>();

    private Account bankAccount;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Database database;

    @PostConstruct
    public void init() {
        accounts.addAll(accountRepository.findAll());

        if (!database.playerNameExists(USERNAME)) {
            database.createUser(USERNAME, PASSWORD);
        }

        bankAccount = createAndGetAccount(database.login(USERNAME, PASSWORD), 100_000);
    }

    public Account createAndGetAccount(PlayerRow login) {
        return createAndGetAccount(login, 0);
    }

    public Account createAndGetAccount(PlayerRow login, double balance) {
        return getAccountOfPlayer(login).orElseGet(() -> createAccount(login, balance));
    }

    public Account createAccount(PlayerRow playerRow, double balance) {
        Optional<Account> existingAccount = getAccountOfPlayer(playerRow);
        existingAccount.ifPresent(account -> {
            throw new IllegalArgumentException("Player " + playerRow.getName() + " has existing account!");
        });

        Account account = new Account(playerRow.getId(), balance);
        account = accountRepository.save(account);

        accounts.add(account);

        return account;
    }

    public Account createAccount(PlayerRow playerRow) {
        return createAccount(playerRow, 0);
    }

    public Optional<Account> getAccountOfPlayer(Player player) {
        return getAccountOfPlayer(player.getPlayerRow());
    }

    public Optional<Account> getAccountOfPlayer(PlayerRow playerRow) {
        return accounts.stream()
                .filter(account -> account.getPlayerID().equals(playerRow.getId()))
                .findFirst();
    }

    public void executeTransfer(Transfer transfer) {
        getAccountOfPlayer(transfer.getSource()).ifPresentOrElse(sourceAccount -> {
            getAccountOfPlayer(transfer.getDestination()).ifPresentOrElse(destinationAccount -> {
                sourceAccount.transfer(destinationAccount, transfer);
                saveAccount(sourceAccount);
                saveAccount(destinationAccount);
            }, () -> {
                throw new IllegalStateException("Destination player has no account!");
            });
        }, () -> {
            throw new IllegalStateException("Player " + transfer.getSource() + " has no account");
        });
    }

    public void executeTransfer(Player player, double ammount, String title) {
        Transfer transfer = new Transfer(null, player.getPlayerRow(), ammount, title);
        getAccountOfPlayer(player.getPlayerRow()).ifPresent(account -> {
            bankAccount.transfer(account, transfer);
            saveAccount(bankAccount);
            saveAccount(account);
        });
    }

    public void executeTransfer(Player source, Account destinationAccount, double ammount, String title) {
        Transfer transfer = new Transfer(source.getPlayerRow(), null, ammount, title);
        getAccountOfPlayer(source.getPlayerRow()).ifPresentOrElse(account -> {
            account.transfer(destinationAccount, transfer);
            saveAccount(destinationAccount);
            saveAccount(account);
        }, () -> {
            throw new IllegalStateException(source + " has no account!");
        });
    }

    private void saveAccount(Account account) {
        accountRepository.save(account);
    }
}
