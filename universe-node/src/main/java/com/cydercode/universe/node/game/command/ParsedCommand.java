package com.cydercode.universe.node.game.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParsedCommand {

    private String module;
    private List<String> arguments;

    public ParsedCommand(String module, List<String> arguments) {
        this.module = module;
        this.arguments = arguments;
    }

    public static ParsedCommand parse(String rawCommand) {
        String[] splitted = rawCommand.split(" ");
        return new ParsedCommand(splitted[0], Arrays.asList(splitted).subList(1, splitted.length));
    }

    public String joinArguments() {
        return joinArguments(0);
    }

    public String joinArguments(int startingFrom) {
        return arguments.subList(startingFrom, arguments.size() - 1)
                .stream()
                .collect(Collectors.joining(" "));
    }

    public String getArgument(int index) {
        return arguments.get(index);
    }

    public int getArgumentsCount() {
        return arguments.size();
    }

    public String getModule() {
        return module;
    }
}
