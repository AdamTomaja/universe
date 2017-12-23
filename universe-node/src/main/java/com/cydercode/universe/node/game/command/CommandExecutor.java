package com.cydercode.universe.node.game.command;

import com.cydercode.universe.node.game.Player;

import java.io.IOException;

public interface CommandExecutor {

    void execute(Player player, ParsedCommand parsedCommand) throws Exception;

}
