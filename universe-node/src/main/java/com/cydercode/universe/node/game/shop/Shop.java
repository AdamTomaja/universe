package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.item.Car;
import com.cydercode.universe.node.game.item.Item;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Shop {

    private final List<Item> items = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        items.add(new Car("BMW"));
        items.add(new Car("AUDI"));
    }

    public List<Item> getItems() {
        return items;
    }

    public void buy(Player player, Item item) {
        if (items.remove(item)) {
            player.giveItem(item);
        } else {
            throw new IllegalStateException("Item not found");
        }
    }
}
