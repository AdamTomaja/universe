package com.cydercode.universe.node.game.player;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRowRepository extends MongoRepository<PlayerRow, String> {

    PlayerRow findByName(String name);

}
