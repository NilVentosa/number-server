package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.client.Client;

import java.io.IOException;
import java.net.ServerSocket;

public class NumberServer {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private static final NumberServer instance = new NumberServer();

    private NumberServer() {}

    public static NumberServer getInstance() {
        return instance;
    }

    public void start(int port, int maxConcurrentConnections, int reportFrequency) {
        try (ServerSocket serverSocket = new ServerSocket(port);){
            LOGGER.debug("Server listening on port {}.", port);
            while (!serverSocket.isClosed()) {
                new Client(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            LOGGER.debug("Server exception: {}", e.getMessage());
        }
    }
}
