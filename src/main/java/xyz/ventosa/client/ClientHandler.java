package xyz.ventosa.client;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.Application;
import xyz.ventosa.server.NumberServerException;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.ventosa.util.Util.*;

@Log4j2
@RequiredArgsConstructor
public class ClientHandler implements Runnable {

    private final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();

    @Setter
    private boolean acceptingNewClients = true;

    private int nextClientId = 1;

    private final Application application;

    public void handleNewClient() {
        Client client = acceptClient();
        if (client != null) {
            addToActiveClients(client);
            startClient(client);
        }
    }

    private Client acceptClient() {
        Client client = null;
        try {
            client = new Client(application.getNumberServer().getServerSocket().accept(), nextClientId, this);
            nextClientId++;
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.debug("Exception: {}.", e.getMessage());
        }
        return client;
    }

    private void addToActiveClients(Client client) {
        log.debug("Adding client with id: {} to active clients.", client.getClientId());
        activeClientList.put(client.getClientId(), client);
    }

    private void startClient(Client client) {
        new Thread(client).start();
        log.debug("New client with id: {}.", client.getClientId());
    }

    protected void removeFromActiveClients(int id) {
        Client client = activeClientList.remove(id);
        if (client != null) {
            log.debug("Removed client with id: {} from active clients.", id);
        }
    }

    public boolean isAcceptingNewClients(int maxConcurrentClients) {
        return acceptingNewClients && activeClientList.size() < maxConcurrentClients;
    }

    public void terminateAllClients() {
        log.info("Terminating all clients.");
        for (Client client : activeClientList.values()) {
            client.terminateClient();
        }
    }

    protected void processClientInput(String input) throws NumberServerException {
        if (isValidNumber(input)) {
            StoringTask.processNumber(Integer.parseInt(input));
        }
        else if (isTerminate(input)) {
            application.terminateApplication();
        }
        else if (input == null) {
            throw new NumberServerException("Connection closed by the client");
        }
        else {
            throw new NumberServerException(String.format("Invalid input: %s", input));
        }
    }

    @Override
    public void run() {
        log.info("Starting to handle clients.");
        while (application.getNumberServer().isServerSocketOpen()) {
            if (isAcceptingNewClients(application.getMaxConcurrentClients())) {
                handleNewClient();
            }
        }
    }
}
