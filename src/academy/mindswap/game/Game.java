package academy.mindswap.game;

import academy.mindswap.game.messages.Messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    Server.ConnectedPlayer player;

    List<String> playerGuess;

    int maxAttempts;

    int attempts;

    Board board;

    List<String> secretCode;

    List<String> turnResult;

    String attempt;

    boolean rightGuess = false;

    public Game(Server.ConnectedPlayer connectedPlayer) {
        this.player = connectedPlayer;
        this.board = new Board();
        this.secretCode = Code.generateCode();
        this.maxAttempts = 12;
    }

    /**
     * This method runs through all the different steps of the game.
     * @throws IOException
     */
    public void play() throws IOException {
        while (!rightGuess) {
            try {
                player.send(Messages.INSERT_TRY);
                attempt = player.askForGuess();
                checkPlayerGuess();
                turnResult = Code.compareCodes(playerGuess, secretCode);
                sendBoard();
                if (playerGuess.equals(secretCode)) {
                    rightGuess = true;
                    player.send(Messages.RIGHT_GUESS.formatted(attempts));
                }
                if (attempts == maxAttempts) {
                    player.send(Messages.OUT_OF_TRIES);
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * transforms player try String into a List of chars
     */

    private void checkPlayerGuess() {
        playerGuess = new ArrayList<>();
        this.attempts++;
        for (int i = 0; i < attempt.length(); i++) {
            playerGuess.add(String.valueOf(attempt.charAt(i)));
        }
    }

    /**
     * sends the legend for the player to know which letter correspondence and the board Updated.
     */

    public void sendBoard() {
        player.send(Messages.LEGEND);
        for (String s : board.updatedBoard(this.playerGuess, this.turnResult))
            player.send(s);
    }

}