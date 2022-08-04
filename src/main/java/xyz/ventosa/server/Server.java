package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.application.Application;
import xyz.ventosa.client.Client;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.ventosa.application.Constants.FLUSHING_FREQUENCY;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Server instance = new Server();
    private static final ServerSocket serverSocket;
    private static final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();
    private static boolean acceptingNewClients = true;

    private Server() {}

    static {
        try {
            serverSocket = new ServerSocket(Application.getPort());
            LOGGER.info("Server listening on port {}.", serverSocket.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Server getInstance() {
        return instance;
    }

    @Override
    public void run() {
        ReportingTask.getInstance().startReportingTask(Application.getReportFrequency());
        StoringTask.getInstance().startStoringTask(FLUSHING_FREQUENCY);
        while (!serverSocket.isClosed()) {
            if (isAcceptingNewClients()) {
                addAndStartNewClient();
            }
        }
    }

    public static synchronized void terminate() {
        try {
            LOGGER.info("Terminating task started.");
            acceptingNewClients = false;
            endAllClients();
            StoringTask.flush();
            serverSocket.close();
            LOGGER.info("Terminating task finalized.");
            System.exit(0);
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            System.exit(1);
        }

    }

    private synchronized void addAndStartNewClient() {
        try {
            Client client = new Client(serverSocket.accept());
            activeClientList.put(client.getClientId(), client);
            client.start();
            LOGGER.debug("New client with id: {}.", client.getClientId());
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Exception: {}.", e.getMessage());
        }

    }

    public void removeFromActiveClients(int id) {
        Client client = activeClientList.remove(id);
        if (client != null) {
            LOGGER.debug("Removing client with id: {} from active clients.", id);
        }
    }

    private static boolean isAcceptingNewClients() {
        return activeClientList.size() < Application.getMaxConcurrentConnections() && acceptingNewClients;
    }

    private static void endAllClients() {
        for (Client client: activeClientList.values()) {
            client.endClient();
        }
    }
}
