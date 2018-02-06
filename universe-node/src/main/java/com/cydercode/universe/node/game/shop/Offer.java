package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.item.Item;

public class Offer {

    private final Item item;
    private final double price;

    public Offer(Item item, double price) {
        this.item = item;
        this.price = price;
    }

    public Item getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "item=" + item +
                ", price=" + price +
                '}';
    }
}
