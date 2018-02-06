package com.cydercode.universe.node.game;

import com.cydercode.universe.node.game.bank.Bank;
import com.cydercode.universe.node.game.scenario.MainMenuScenario;
import com.cydercode.universe.node.game.shop.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class Universe implements Named {

    private static final Logger LOGGER = LoggerFactory.getLogger(Universe.class);

    @Autowired
    private Bank bank;

    @Autowired
    private Shop shop;

    private Optional<String> name = Optional.empty();

    private Map<WebSocketSession, Player> playersIndex = new ConcurrentHashMap<>();

    public void initializeNewPlayer(Player player) {
        try {
            player.setUniverse(this);
            playersIndex.put(player.getSession(), player);
            player.trySendMessage("Welcome in " + getName());
            player.startScenario(new MainMenuScenario(player));
        } catch (Exception e) {
            LOGGER.error("Unable to initialize new player", e);
        }
    }

    public void receiveMessageFromSession(WebSocketSession session, String payload) {
        findPlayerBySession(session).ifPresent(player -> {
            try {
                player.receiveMessage(payload);
            } catch (Exception e) {
                LOGGER.error("Player {} unable to receive message", player, e);
            }
        });
    }

    public Optional<Player> findPlayerBySession(WebSocketSession session) {
        return Optional.ofNullable(playersIndex.get(session));
    }

    public List<String> getPlayersNames() {
        return playersIndex.values().stream().map(player -> player.toString()).collect(Collectors.toList());
    }

    public Optional<Player> findPlayerByName(String name) {
        return playersIndex.values().stream().filter(player -> name.equals(player.getName())).findFirst();
    }

    public void removePlayer(Player player) {
        findSessionOfPlayer(player)
                .ifPresent(session -> playersIndex.remove(session));
    }

    public Bank getBank() {
        return bank;
    }

    public Shop getShop() {
        return shop;
    }

    public Optional<WebSocketSession> findSessionOfPlayer(Player player) {
        return playersIndex.entrySet()
                .stream()
                .filter(entry -> player.equals(entry.getValue()))
                .map(entry -> entry.getKey())
                .findFirst();
    }

    public void setName(String name) {
        this.name = Optional.of(name);
    }

    @Override
    public Optional<String> getRawName() {
        return name;
    }
}
