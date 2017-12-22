package com.cydercode.universe.node.game;

import com.cydercode.universe.node.game.scenario.Scenario;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Optional;

public class Player {

    private final WebSocketSession session;

    private Universe universe;
    private Optional<String> name;
    private Scenario currentScenario;

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

    @Override
    public String toString() {
        return name.orElse("PL" + this.hashCode());
    }

    public void receiveMessage(String payload) throws Exception {
        currentScenario.receiveMessage(payload);
    }
}
