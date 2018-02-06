package com.cydercode.universe.node.game;

import com.cydercode.universe.node.game.item.Item;
import com.cydercode.universe.node.game.scenario.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {

    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final WebSocketSession session;

    private Universe universe;
    private Optional<String> name = Optional.empty();
    private Scenario currentScenario;
    private Vector2D position = new Vector2D(0, 0);
    private List<Item> items = new CopyOnWriteArrayList<>();

    public Player(WebSocketSession session) {
        this.session = session;
    }

    public Universe getUniverse() {
        return universe;
    }

    public void setUniverse(Universe universe) {
        this.universe = universe;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void trySendMessage(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
            LOGGER.warn("Unable to send message to player {}", this);
        }
    }

    public void sendMessage(String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }


    public void setName(String name) {
        this.name = Optional.of(name);
    }

    public void startScenario(Scenario scenario) throws Exception {
        this.currentScenario = scenario;
        scenario.initialize();
    }

    public void giveItem(Item item) {
        items.add(item);
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void receiveMessage(String payload) throws Exception {
        currentScenario.receiveMessage(payload);
    }

    public String getName() {
        return name.orElse("PL" + this.hashCode());
    }
}
