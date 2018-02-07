package com.cydercode.universe.node.game.command;

import com.cydercode.universe.node.game.player.Player;

public interface CommandExecutor {

    void execute(Player player, ParsedCommand parsedCommand) throws Exception;

}
