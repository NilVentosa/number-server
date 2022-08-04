package xyz.ventosa.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.handler.InputHandler;

import java.net.Socket;

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
        InputHandler.processClientInput(this);
    }

    public int getClientId() {
        return clientId;
    }

    public Socket getSocket() {
        return socket;
    }

}
