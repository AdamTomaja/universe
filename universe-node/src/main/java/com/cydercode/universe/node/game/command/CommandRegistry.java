package com.cydercode.universe.node.game.command;

import com.cydercode.universe.node.game.item.Item;
import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.scenario.MainMenuScenario;
import com.cydercode.universe.node.game.scenario.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;
import static java.lang.Integer.parseInt;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

public class CommandRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);

    private Map<String, CommandDescription> descriptions = new HashMap<>();

    public CommandRegistry() {

        addCommand(CommandDescription.newCommand().withName("use")
                .withExecutor((player, command) -> {
                    Item item = player.getItems().get(parseInt(command.getArgument(0)));
                    if (item instanceof Scenario) {
                        player.startScenario((Scenario) item);
                    } else {
                        throw new IllegalArgumentException("This item can`t be used");
                    }
                })
                .withParameter("item-index", String.class, false).build());

        addCommand(newCommand()
                .withName("exit")
                .withExecutor((player, command) -> {
                    player.startScenario(new MainMenuScenario(player));
                })
                .build());

        addCommand(newCommand()
                .withName("help")
                .withExecutor((player, command) -> {
                    if (command.getArgumentsCount() > 0) {
                        String commandName = command.getArgument(0);
                        if (!descriptions.containsKey(commandName)) {
                            throw new IllegalArgumentException("There is no description for this command");
                        }

                        CommandDescription description = descriptions.get(commandName);

                        StringBuilder sb = new StringBuilder();
                        sb.append(description.getCommandName());
                        sb.append(" ");
                        description.getParameters().forEach(commandParameter -> {
                            sb.append(String.format(" [%s:%s:%s]", commandParameter.getName(), commandParameter.getType().getSimpleName(), commandParameter.isOptional() ? "optional" : "required"));
                        });

                        player.trySendMessage(commandName + " help: " + description.getDescription());
                        player.trySendMessage("Syntax: " + sb.toString());

                    } else {
                        player.trySendMessage("Available commands: " + getCommandNames().stream().collect(joining(", ")));
                    }
                })
                .build());

        addCommand(newCommand()
                .withName("me")
                .withExecutor((player, command) -> {
                    AtomicInteger itemIndex = new AtomicInteger(0);

                    player.trySendMessage(Arrays.asList("Your name: " + player,
                            "Bank account: " + player.getUniverse().getBank().getAccountOfPlayer(player),
                            "Items: \n" + player.getItems()
                                    .stream()
                                    .map(item -> String.format("%s. %s", itemIndex.incrementAndGet(), item.toString()))
                                    .collect(joining("\n")),
                            "Current scenario: " + player.getScenario().getClass().getSimpleName()
                    ).stream().collect(joining("\n")));
                })
                .build());

    }

    public void addCommand(CommandDescription description) {
        descriptions.put(description.getCommandName(), description);
    }

    public void execute(Player player, String command) {
        ParsedCommand parsedCommand = ParsedCommand.parse(command);

        ofNullable(descriptions.get(parsedCommand.getModule()))
                .ifPresentOrElse(commandDescription -> {
                    try {
                        if (parsedCommand.getArgumentsCount() < commandDescription.getParameters()
                                .stream()
                                .filter(arg -> !arg.isOptional())
                                .count()) {
                            throw new IllegalArgumentException("Not enough parameters");
                        }

                        commandDescription.getExecutor().execute(player, parsedCommand);
                    } catch (Exception e) {
                        LOGGER.error("Unable to execute command of player {}", player, e);
                        player.trySendMessage("Command execution error: " + e.getMessage());
                    }
                }, () -> {
                    player.trySendMessage("Unknown command");
                });
    }

    public List<String> getCommandNames() {
        return descriptions.keySet().stream().collect(Collectors.toList());
    }
}
