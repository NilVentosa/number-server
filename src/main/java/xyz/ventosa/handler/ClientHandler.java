package xyz.ventosa.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.application.Application;
import xyz.ventosa.client.Client;
import xyz.ventosa.server.Server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();
    private static boolean acceptingNewClients = true;

    private ClientHandler() {
    }

    public static synchronized void addAndStartNewClient() {
        try {
            Client client = new Client(Server.getServerSocket().accept());
            activeClientList.put(client.getClientId(), client);
            client.start();
            LOGGER.debug("New client with id: {}.", client.getClientId());
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Exception: {}.", e.getMessage());
        }

    }

    private static void removeFromActiveClients(int id) {
        Client client = activeClientList.remove(id);
        if (client != null) {
            LOGGER.debug("Removing client with id: {} from active clients.", id);
        }
    }

    public static boolean isAcceptingNewClients() {
        return activeClientList.size() < Application.getMaxConcurrentConnections() && acceptingNewClients;
    }

    public static void endAllClients() {
        acceptingNewClients = false;
        for (Client client: activeClientList.values()) {
            endClient(client);
        }
    }

    public static void endClient(Client client) {
        try {
            LOGGER.debug("Closing socket for client with id: {}.", client.getClientId());
            client.getSocket().close();
            removeFromActiveClients(client.getClientId());
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Exception in endClient: {}.", e.getMessage());
        }

    }
}
