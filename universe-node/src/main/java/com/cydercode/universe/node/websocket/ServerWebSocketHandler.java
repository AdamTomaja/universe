package com.cydercode.universe.node.websocket;

import com.cydercode.universe.node.game.player.Player;
import com.cydercode.universe.node.game.Universe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class ServerWebSocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerWebSocketHandler.class);

    @Autowired
    private Universe universe;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOGGER.info("New client connected: {}", session.getRemoteAddress().getAddress());
        universe.initializeNewPlayer(new Player(session));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        universe.receiveMessageFromSession(session, message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOGGER.info("Connection closed ");
        Optional<Player> playerOptional = universe.findPlayerBySession(session);
        playerOptional.ifPresent(player -> {
            LOGGER.info("Removing player {} associated to session", player);
            universe.removePlayer(player);
        });
    }
}
