package academy.mindswap.game.commands;

import academy.mindswap.game.Code;
import academy.mindswap.game.Server;
import academy.mindswap.game.messages.Messages;

public class ShowHandler implements CommandHandler {

    /**
     * sends the secret code to the player in case he wants to end the game suddenly and Game over message will be shown.
     * connection with the player will be closed.
     * @param server
     * @param connectedPlayer player that actually push the command.
     */
    @Override
    public void execute(Server server, Server.ConnectedPlayer connectedPlayer) {
        Code.showCode(connectedPlayer);
        connectedPlayer.send(Messages.CONNECTION_CLOSED);
        connectedPlayer.close();
    }
}
