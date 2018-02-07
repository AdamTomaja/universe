package com.cydercode.universe.node.game.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRowRepository extends MongoRepository<PlayerRow, String> {

    PlayerRow findByName(String name);

}
