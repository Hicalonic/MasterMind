package academy.mindswap.player;

import academy.mindswap.game.messages.Messages;

import java.io.*;
import java.net.Socket;

public class Player {

    public static void main(String[] args) {
        Player player = new Player();
        try {
            player.start();
        } catch (IOException e) {
            System.out.println(Messages.CONNECTION_CLOSED);
        }
    }

    private void start() throws IOException {
        Socket socket = new Socket("localhost", 8082);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        new Thread(new KeyboardHandler(out, socket)).start();
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
        }
        socket.close();
    }

    private static class KeyboardHandler implements Runnable {
        private final BufferedWriter out;
        private final Socket socket;
        private final BufferedReader in;

        public KeyboardHandler(BufferedWriter out, Socket socket) {
            this.out = out;
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {

            while (!socket.isClosed()) {
                try {
                    String line = in.readLine();

                    out.write(line);
                    out.newLine();
                    out.flush();

                    if (line.equals("/quit")) {
                        socket.close();
                        System.exit(0);
                    }
                } catch (IOException e) {
                    System.out.println(Messages.SERVER_ERROR);
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
