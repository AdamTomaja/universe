package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.bank.Account;
import com.cydercode.universe.node.game.bank.Bank;
import com.cydercode.universe.node.game.player.PlayersDatabase;
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
    private PlayersDatabase playersDatabase;

    @Autowired
    private Bank bank;

    @PostConstruct
    public void init() {
        offers.add(new Offer(new Car("BMW"), 500));
        offers.add(new Offer(new Car("AUDI"), 100));

        if (!playersDatabase.playerNameExists(USERNAME)) {
            playersDatabase.createUser(USERNAME, PASSWORD);
        }

        account = bank.createAndGetAccount(playersDatabase.login(USERNAME, PASSWORD));
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
