package academy.mindswap.game.commands;

import academy.mindswap.game.Server;

public class StartNewGameHandler implements CommandHandler {
    /**
     * restarts the game in case player wants to play again.
     * @param server
     * @param connectedPlayer player that actually push  the command.
     */
    @Override
    public void execute(Server server, Server.ConnectedPlayer connectedPlayer) {
        connectedPlayer.run();
    }
}