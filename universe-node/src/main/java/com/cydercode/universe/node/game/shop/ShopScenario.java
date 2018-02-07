package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.scenario.Scenario;
import com.cydercode.universe.node.game.shop.Offer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.cydercode.universe.node.game.command.CommandDescription.newCommand;

public class ShopScenario implements Scenario {

    private final Player player;

    private CommandRegistry registry = new CommandRegistry();

    private List<Offer> offers;

    public ShopScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        player.trySendMessage("Welcome in Shop!");

        registry.addCommand(newCommand()
                .withName("buy")
                .withExecutor((player, command) -> {
                    Offer offerToAccept = offers.get(Integer.parseInt(command.getArgument(0)));
                    player.getUniverse().getShop().buy(player, offerToAccept);
                    player.trySendMessage("You have new item: " + offerToAccept.getItem());
                }).build());

        registry.addCommand(newCommand().
                withName("list").
                withExecutor((player, command) -> {
                    offers = player.getUniverse().getShop().getOffers();
                    AtomicInteger atomicInteger = new AtomicInteger(0);

                    String itemsList = offers.stream().map(item -> atomicInteger.getAndIncrement() + ". " + item.toString()).collect(Collectors.joining("\n"));
                    player.trySendMessage("Offers in shop: \n" + itemsList);
                }).build());
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }
}
