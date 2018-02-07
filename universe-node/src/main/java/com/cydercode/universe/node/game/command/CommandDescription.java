package com.cydercode.universe.node.game.command;

import com.cydercode.universe.node.game.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;

public class CommandDescription {

    private final String commandName;
    private final List<CommandParameter> parameters;
    private final String description;
    private final CommandExecutor executor;

    public CommandDescription(String commandName, List<CommandParameter> parameters, String description, CommandExecutor executor) {
        this.commandName = commandName;
        this.parameters = parameters;
        this.description = description;
        this.executor = executor;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<CommandParameter> getParameters() {
        return parameters;
    }

    public String getDescription() {
        return description;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public static class Builder {
        private String commandName;
        private List<CommandParameter> parameters = new ArrayList<>();
        private String description = "";
        private CommandExecutor executor;

        public Builder withName(String name) {
            commandName = name;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withParameter(String name, Class<?> type, boolean isOptional) {
            parameters.add(new CommandParameter(name, type, isOptional));
            return this;
        }

        public Builder withExecutor(CommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        public CommandDescription build() {
            return new CommandDescription(commandName, parameters, description, executor);
        }
    }

    public static Builder newCommand() {
        return new Builder();
    }

    public static CommandDescription forScenario(String name, Scenario scenario) {
        return CommandDescription.newCommand()
                .withName(name)
                .withExecutor(((player, parsedCommand) -> player.startScenario(scenario)))
                .withDescription("Open " + name + " scenario")
                .build();
    }
}
