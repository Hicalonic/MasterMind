package academy.mindswap.game;

import academy.mindswap.game.commands.Command;
import academy.mindswap.game.messages.Instructions;
import academy.mindswap.game.messages.Messages;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    private final ServerSocket serverSocket;

    ExecutorService service;

    int numOfPlayers;

    protected final List<ConnectedPlayer> playersList;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        playersList = new CopyOnWriteArrayList<>();
    }

    /**
     *Creates a thread pool based on the number of players when the server starts and wait for two connections.
     **/
    public void start(int numOfPlayers) throws IOException, InterruptedException {
        this.numOfPlayers = numOfPlayers;
        service = Executors.newFixedThreadPool(numOfPlayers);
        System.out.printf(Messages.GAME_STARTED);
        while (playersList.size() < numOfPlayers) {
            acceptConnection();
        }
    }

    /**
     *This method waits to a player to connect and sends him to the thread pool.
     **/
    public void acceptConnection() throws IOException {
        Socket playerSocket = serverSocket.accept(); // blocking method
        ConnectedPlayer connectedPlayer = new ConnectedPlayer(playerSocket);
        service.submit(connectedPlayer);
    }

    /**
     *This method add a player in game, asks for his name and wait for a second player.
     **/
    private synchronized void addPlayer(ConnectedPlayer player) throws IOException, InterruptedException {
        verifyPlayerName(player);
        if (playersList.size() < numOfPlayers) {
            player.send(Messages.WAITING_ALL_PLAYERS.formatted(numOfPlayers - playersList.size()));
            this.wait();
        } else this.notifyAll();
    }

    /**
     * asks the player for his name and verifies if his name already exists, in that case a message saying that
     * name already exist is shown.
     * @param player player currently inserting his name.
     * @throws IOException
     * @throws InterruptedException
     */
    private void verifyPlayerName(ConnectedPlayer player) throws IOException, InterruptedException {
        player.send(Messages.ASK_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(player.playerSocket.getInputStream()));
        String playerName = reader.readLine();
        validateName(player, playerName);
        if (playersList.stream().
                anyMatch(p -> p.getName().
                        equals(playerName))) {
            player.send(Messages.INVALID_NAME);
            verifyPlayerName(player);
        } else {
            player.name = playerName;
            playersList.add(player);
        }
        player.send(Messages.WELCOME.formatted(player.getName()));
    }

    /**
     * checks if player inputs a name that doesn't start with a space character, if it happens, player gets a message
     * that his name is not valid.
     * @param connectedPlayer player currently inserting his name.
     * @param name name of the player inserted.
     * @throws IOException
     * @throws InterruptedException
     */
    private void validateName(ConnectedPlayer connectedPlayer, String name) throws IOException, InterruptedException {
        String regex = "^\\S+$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(name);
        if (!matcher.find()) {
            connectedPlayer.send(Messages.INVALID_FIRST_NAME);
            verifyPlayerName(connectedPlayer);
        }
    }

    /**
    *Remove the player from the player list.
    */
    public void removePlayer(ConnectedPlayer connectedPlayer) {
        playersList.remove(connectedPlayer);
    }


    public class ConnectedPlayer implements Runnable {

        private String name;

        private final Socket playerSocket;

        private final BufferedWriter out;

        private String message;

        Game game;

        public ConnectedPlayer(Socket playerSocket) throws IOException {
            this.playerSocket = playerSocket;
            this.out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
        }

        /**
         *This method run add a player in the game, sends the instruction of the game and then begin the game
         **/
        @Override
        public void run() {
            try {
                game = new Game(this);
                addPlayer(this);
                send(Instructions.readInstruction());
                game.play();
                restart();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                close();
            }
        }


        /**
         *This method asks the player to give 4 letters, representing his guess of colors.
         * Also, it gives the user the change to insert a command previously introduced in the game.
         **/
        public String askForGuess() throws IOException {
            while (!playerSocket.isClosed()) {
                try {
                    Scanner in = new Scanner(playerSocket.getInputStream());
                    message = in.nextLine().toUpperCase();
                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }
                } catch (IOException e) {
                    System.err.println(Messages.PLAYER_ERROR + e.getMessage());
                }
                if (!validInput()) {
                    askForGuess();
                }
                return message;
            }
            return null;
        }

        /**
         * This method doesn't allow the player to input a guess different from the possible choices.
         **/
        private boolean validInput() {
            String regex = "^[OYBPG]{4}$";
            final Pattern pattern = Pattern.compile(regex);
            final Matcher matcher = pattern.matcher(message);
            if (!matcher.find()) {
                send(Messages.INVALID_TRY);
                return false;
            }
            return true;
        }

        /**
         *This method checks if the input is a command and returns a boolean.
         **/
        private boolean isCommand(String message) {
            return message.startsWith("/");
        }

        /**
         *In case that command belongs to the available list, the method executes him.
         *In case the player try insert another command, will give a message that don't have.
         **/
        private void dealWithCommand(String message) throws IOException {
            String description = message.split(" ")[0];
            Command command = Command.getCommandFromDescription(description);
            if (command == null) {
                out.write(Messages.NO_SUCH_COMMAND);
                out.newLine();
                out.flush();
                askForGuess();
            }
            command.getHandler().execute(Server.this, this);
        }

        /**
         *Sends a message to the player terminal.
         **/
        public void send(String message) {
            try {
                out.write(message);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                removePlayer(this);
                e.printStackTrace();
            }
        }

        /**
         *Close the socket.
         **/
        public void close() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        /**
         * sends a message to the current player if he wants to quit or play again, his input will be handled
         * afterwards.
         */
        public void restart() {
            send(Messages.QUIT_OR_NEW_GAME);
            try {
                Scanner in = new Scanner(playerSocket.getInputStream());
                message = in.nextLine();
                dealWithCommand(message.toUpperCase());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}