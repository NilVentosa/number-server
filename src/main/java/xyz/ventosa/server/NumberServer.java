package xyz.ventosa.server;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class NumberServer {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    @Getter
    private ServerSocket serverSocket;

    public NumberServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server listening on port: {}.", serverSocket.getLocalPort());
        }
        catch (IOException e) {
            LOGGER.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void terminateServer() {
        LOGGER.info("Terminating server");
        try {
            serverSocket.close();
        }
        catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            System.exit(1);
        }
    }

    public boolean isServerSocketOpen() {
        if (serverSocket != null) {
            return !serverSocket.isClosed();
        }
        return false;
    }
}
