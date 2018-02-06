package com.cydercode.universe.node.game.item;

public class Car implements Item {

    private final String name;

    public Car(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }
}
