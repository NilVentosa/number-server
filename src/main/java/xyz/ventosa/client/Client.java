package xyz.ventosa.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private final Socket socket;
    private static int instanceCount;
    private final int clientId;

    public Client(Socket socket) {
        instanceCount++;
        this.clientId = instanceCount;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputProcessor.processClientInput(this);
    }

    public int getClientId() {
        return clientId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void endClient() {
        try {
            LOGGER.debug("Closing socket for client with id: {}.", clientId);
            this.socket.close();
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Exception in endClient: {}.", e.getMessage());
        }

    }

}
