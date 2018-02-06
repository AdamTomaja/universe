package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.Player;

public class Transfer {

    private final Player source;
    private final Player destination;
    private final double ammount;
    private final String title;

    public Transfer(Player source, Player destination, double ammount, String title) {
        this.source = source;
        this.destination = destination;
        this.ammount = ammount;
        this.title = title;
    }

    public Player getSource() {
        return source;
    }

    public Player getDestination() {
        return destination;
    }

    public double getAmmount() {
        return ammount;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "source=" + source +
                ", destination=" + destination +
                ", ammount=" + ammount +
                ", title='" + title + '\'' +
                '}';
    }
}
