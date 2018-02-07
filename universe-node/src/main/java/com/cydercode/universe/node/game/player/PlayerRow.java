package com.cydercode.universe.node.game.player;

import com.cydercode.universe.node.game.item.Item;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerRow {

    @Id
    private String id;

    private String name;

    private String password;

    private List<Item> items = new CopyOnWriteArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "PlayerRow{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
