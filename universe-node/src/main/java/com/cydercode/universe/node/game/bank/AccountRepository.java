package com.cydercode.universe.node.game.bank;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
}
