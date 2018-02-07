package com.cydercode.universe.node.game.bank;

import com.cydercode.universe.node.game.player.PlayerRow;

public class Transfer {

    private final PlayerRow source;
    private final PlayerRow destination;
    private final double ammount;
    private final String title;

    public Transfer(PlayerRow source, PlayerRow destination, double ammount, String title) {
        this.source = source;
        this.destination = destination;
        this.ammount = ammount;
        this.title = title;
    }

    public PlayerRow getSource() {
        return source;
    }

    public PlayerRow getDestination() {
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
