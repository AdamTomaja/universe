package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.bank.Account;
import com.cydercode.universe.node.game.bank.Bank;
import com.cydercode.universe.node.game.database.Database;
import com.cydercode.universe.node.game.database.PlayerRow;
import com.cydercode.universe.node.game.item.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Shop {

    private final String USERNAME = Shop.class.getName();
    private final String PASSWORD = "ajsdia7sdy8y31t786std67tf";

    private final List<Offer> offers = new CopyOnWriteArrayList<>();

    private Account account;

    @Autowired
    private Database database;

    @Autowired
    private Bank bank;

    @PostConstruct
    public void init() {
        offers.add(new Offer(new Car("BMW"), 500));
        offers.add(new Offer(new Car("AUDI"), 100));

        if (!database.playerNameExists(USERNAME)) {
            database.createUser(USERNAME, PASSWORD);
        }

        account = bank.createAndGetAccount(database.login(USERNAME, PASSWORD));
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void buy(Player player, Offer offer) {
        player.getUniverse().getBank().executeTransfer(player, account, offer.getPrice(), "Shop offer accepted: " + offer);
        if (offers.remove(offer)) {
            player.giveItem(offer.getItem());
        } else {
            throw new IllegalStateException("Offer not found");
        }
    }
}
