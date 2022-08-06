package xyz.ventosa.client;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@RequiredArgsConstructor
public class ClientHandler {
    private final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();

    private boolean acceptingNewClients = true;

    private int clientCount = 1;

    private final ServerSocket serverSocket;

    public void acceptNewClient() {
        try {
            Client client = new Client(serverSocket.accept(), clientCount, this);
            clientCount++;
            activeClientList.put(client.getClientId(), client);
            new Thread(client).start();
            log.debug("New client with id: {}.", client.getClientId());
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.debug("Exception: {}.", e.getMessage());
        }
    }

    void removeFromActiveClients(int id) {
        Client client = activeClientList.remove(id);
        if (client != null) {
            log.debug("Removing client with id: {} from active clients.", id);
        }
    }

    public boolean isAcceptingNewClients(int maxConcurrentConnections) {
        return acceptingNewClients && activeClientList.size() < maxConcurrentConnections;
    }

    private void terminateAllClients() {
        log.info("Terminating all clients.");
        for (Client client : activeClientList.values()) {
            client.terminateClient();
        }
    }

    private void terminateServer() {
        log.info("Terminating server");
        StoringTask.flush();
        try {
            serverSocket.close();
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.error("Exception in terminate: {}.", e.getMessage());
            StoringTask.flush();
            System.exit(1);
        }
    }

    public void terminateApplication() {
        log.info("Terminating task started.");
        acceptingNewClients = false;
        terminateAllClients();
        terminateServer();
        log.info("Terminating task ended successfully.");
        System.exit(0);
    }
}
