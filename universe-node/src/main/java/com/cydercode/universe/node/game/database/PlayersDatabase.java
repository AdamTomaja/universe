package com.cydercode.universe.node.game.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PlayersDatabase {

    @Autowired
    private PlayerRowRepository playerRowRepository;

    public PlayerRow createUser(String name, String password) {
        if (playerNameExists(name)) {
            throw new IllegalArgumentException("Player name already exists");
        }

        PlayerRow playerRow = new PlayerRow();
        playerRow.setName(name);
        playerRow.setPassword(password);
        return playerRowRepository.save(playerRow);
    }

    public boolean playerNameExists(String name) {
        return playerRowRepository.findByName(name) != null;
    }

    public PlayerRow login(String name, String password) {
        PlayerRow row = playerRowRepository.findByName(name);

        if (row != null) {
            if (!Objects.equals(password, row.getPassword())) {
                throw new IllegalArgumentException("Invalid password");
            }

            return row;
        }

        throw new IllegalArgumentException("Player not found");
    }

}
