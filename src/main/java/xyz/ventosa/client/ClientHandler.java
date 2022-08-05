package xyz.ventosa.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();
    private boolean acceptingNewClients = true;
    private final ServerSocket serverSocket;

    public ClientHandler(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void acceptNewClient() {
        try {
            Client client = new Client(serverSocket.accept(), this);
            activeClientList.put(client.getClientId(), client);
            new Thread(client).start();
            LOGGER.debug("New client with id: {}.", client.getClientId());
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Exception: {}.", e.getMessage());
        }
    }

    void removeFromActiveClients(int id) {
        Client client = activeClientList.remove(id);
        if (client != null) {
            LOGGER.debug("Removing client with id: {} from active clients.", id);
        }
    }

    public boolean isAcceptingNewClients(int maxConcurrentConnections) {
        return acceptingNewClients && activeClientList.size() < maxConcurrentConnections;
    }

    void terminateAllClients() {
        LOGGER.info("Terminating all clients.");
        for (Client client: activeClientList.values()) {
            client.terminateClient();
        }
    }

    void terminateServer() {
        LOGGER.info("Terminating server");
        StoringTask.flush();
        try {
            serverSocket.close();
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            StoringTask.flush();
            System.exit(1);
        }
    }

    public void terminateApplication() {
        LOGGER.info("Terminating task started.");
        acceptingNewClients = false;
        terminateAllClients();
        terminateServer();
        LOGGER.info("Terminating task ended successfully.");
        System.exit(0);
    }
}
