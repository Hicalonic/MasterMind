package academy.mindswap.game.commands;

import academy.mindswap.game.Server;
import academy.mindswap.game.messages.Messages;

public class HelpHandler implements CommandHandler {


    /**
     * sends a message to the player with all the commands available on the game.
     * @param server
     * @param connectedPlayer player that actually push the command.
     */
    @Override
    public void execute(Server server, Server.ConnectedPlayer connectedPlayer) {
        connectedPlayer.send(Messages.COMMANDS_LIST);
    }
}