package com.cydercode.universe.node.game.command;

public class CommandParameter {

    private final String name;
    private final Class<?> type;
    private final boolean optional;

    public CommandParameter(String name, Class<?> type, boolean optional) {
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public boolean isOptional() {
        return optional;
    }
}
