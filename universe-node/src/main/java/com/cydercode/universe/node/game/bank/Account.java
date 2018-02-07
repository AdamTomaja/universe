package com.cydercode.universe.node.game.bank;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    @Id
    private String id;

    private  String playerID;

    private List<Transfer> journal = new CopyOnWriteArrayList<>();

    private double balance = 0;

    public Account() {
    }

    public Account(String playerID) {
        this.playerID = playerID;
    }

    public Account(String playerID, double balance) {
        this(playerID);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void transfer(Account destinationAccount, Transfer transfer) {
        if (balance < transfer.getAmmount()) {
            throw new IllegalStateException("Not enough funds");
        }

        balance -= transfer.getAmmount();
        destinationAccount.acceptTransfer(transfer);
        journal.add(transfer);
    }

    private void acceptTransfer(Transfer transfer) {
        balance += transfer.getAmmount();
        journal.add(transfer);
    }

    public List<Transfer> getHistory() {
        return new ArrayList<>(journal);
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}
