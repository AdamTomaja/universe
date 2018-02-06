package com.cydercode.universe.node.game;

import java.util.Optional;

public interface Named {

    Optional<String> getRawName();

    default String getName() {
        Optional<String> name = getRawName();

        if(name.isPresent()) {
            return name.get();
        }

        return this.getClass().getSimpleName() + this.hashCode();
    }
}
