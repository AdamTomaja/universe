package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;

public class Account {

    private final Player player;

    private double money = 0;

    public Account(Player player) {
        this.player = player;
    }

    public Account(Player player, double balance) {
        this(player);
        money = balance;
    }

    public double getBalance() {
        return money;
    }

    public Player getPlayer() {
        return player;
    }

    public void transferTo(Account destinationAccount, double ammount) {
        money -= ammount;
        destinationAccount.money += ammount;
    }
}
