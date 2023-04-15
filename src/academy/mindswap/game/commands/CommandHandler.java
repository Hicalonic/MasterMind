package academy.mindswap.game.commands;

import academy.mindswap.game.Server;

public interface CommandHandler {
    void execute(Server server, Server.ConnectedPlayer connectedPlayer);
}
