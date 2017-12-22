package com.cydercode.universe.node.game.scenario;

public interface Scenario {

    void initialize() throws Exception;

    void receiveMessage(String message) throws Exception;
}
