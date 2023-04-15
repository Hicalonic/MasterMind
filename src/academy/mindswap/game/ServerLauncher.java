package academy.mindswap.game;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) {
        try {
            Server server = new Server(8082);
            server.start(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}