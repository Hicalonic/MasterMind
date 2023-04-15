package academy.mindswap.game.commands;

import academy.mindswap.game.Server;

public class QuitHandler implements CommandHandler {

    /**
     * removes the player from the list of players and closes the connection with him.
     * @param server
     * @param connectedPlayer player that actually push the command.
     */
    @Override
    public void execute(Server server, Server.ConnectedPlayer connectedPlayer) {
        server.removePlayer(connectedPlayer);
        connectedPlayer.close();
    }
}
