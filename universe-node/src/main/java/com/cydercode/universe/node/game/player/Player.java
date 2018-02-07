package com.cydercode.universe.node.game.player;

import com.cydercode.universe.node.game.Named;
import com.cydercode.universe.node.game.Universe;
import com.cydercode.universe.node.game.Vector2D;
import com.cydercode.universe.node.game.item.Item;
import com.cydercode.universe.node.game.scenario.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Player implements Named {

    private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

    private final WebSocketSession session;

    private Universe universe;

    private Optional<PlayerRow> playerRow = Optional.empty();

    private Scenario currentScenario;
    private Vector2D position = new Vector2D(0, 0);

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


    public void startScenario(Scenario scenario) throws Exception {
        this.currentScenario = scenario;
        scenario.initialize();
    }

    public void giveItem(Item item) {
        if (!playerRow.isPresent()) {
            throw new IllegalStateException("User must be logged in to receive items!");
        }

        getUniverse().getPlayersDatabase().giveItem(getPlayerRow(), item);
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

    public void setPlayerRow(Optional<PlayerRow> playerRow) {
        this.playerRow = playerRow;
    }

    public PlayerRow getPlayerRow() {
        return playerRow.get();
    }

    public boolean isLoggedIn() {
        return playerRow.isPresent();
    }

    public List<Item> getItems() {
        return getPlayerRow().getItems();
    }

    @Override
    public Optional<String> getRawName() {
        return playerRow.map(row -> Optional.of(row.getName()))
                .orElse(Optional.empty());
    }
}
