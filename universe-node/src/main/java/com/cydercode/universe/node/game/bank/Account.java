package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    private final Player player;

    private List<Transfer> journal = new CopyOnWriteArrayList<>();

    private double balance = 0;

    public Account(Player player) {
        this.player = player;
    }

    public Account(Player player, double balance) {
        this(player);
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public Player getPlayer() {
        return player;
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

    private Map<String, Object> createJournalRow(Player source, Player destination, double ammount, String title) {
        Map<String, Object> map = new HashMap<>();
        map.put("source", source);
        map.put("destination", destination);
        map.put("ammount", ammount);
        map.put("title", title);
        return map;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}
