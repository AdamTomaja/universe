package com.cydercode.universe.node.game.shop;

import com.cydercode.universe.node.game.Player;
import com.cydercode.universe.node.game.bank.Account;
import com.cydercode.universe.node.game.bank.Transfer;
import com.cydercode.universe.node.game.item.Car;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Shop {

    private final List<Offer> offers = new CopyOnWriteArrayList<>();

    private Account account = new Account(null, 0);

    @PostConstruct
    public void init() {
        offers.add(new Offer(new Car("BMW"), 500));
        offers.add(new Offer(new Car("AUDI"), 100));
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
