package com.cydercode.universe.node.game.scenario;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.command.CommandRegistry;
import com.cydercode.universe.node.game.item.Item;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ShopScenario implements Scenario {

    private final Player player;

    private CommandRegistry registry = new CommandRegistry();

    private List<Item> items;

    public ShopScenario(Player player) {
        this.player = player;
    }

    @Override
    public void initialize() throws Exception {
        player.trySendMessage("Welcome in Shop!");
        
        registry.addCommand("buy", (player, command) -> {
            Item itemToBuy = items.get(Integer.parseInt(command.getArgument(0)));
            player.getUniverse().getShop().buy(player, itemToBuy);
            player.trySendMessage("You have new item: " + itemToBuy);
        });

        registry.addCommand("list", (player, command) -> {
            items = player.getUniverse().getShop().getItems();
            AtomicInteger atomicInteger = new AtomicInteger(0);

            String itemsList = items.stream().map(item -> atomicInteger.getAndIncrement() + ". " + item.toString()).collect(Collectors.joining("\n"));
            player.trySendMessage("Items in shop: \n" + itemsList);
        });
    }

    @Override
    public void receiveMessage(String message) throws Exception {
        registry.execute(player, message);
    }
}
