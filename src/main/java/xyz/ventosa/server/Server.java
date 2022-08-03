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

import static xyz.ventosa.util.Constants.FLUSHING_FREQUENCY;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Server instance = new Server();
    private static final ServerSocket serverSocket;
    private static final Map<Integer, Client> activeClients = new ConcurrentHashMap<>();
    private static boolean acceptingNewClients = true;

    private Server() {}

    static {
        try {
            serverSocket = new ServerSocket(Application.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Server getInstance() {
        return instance;
    }

    @Override
    public void run() {
        LOGGER.info("Server listening on port {}.", serverSocket.getLocalPort());
        ReportingTask.getInstance().startReportingTask(Application.getReportFrequency());
        StoringTask.getInstance().startStoringTask(FLUSHING_FREQUENCY);
        while (!serverSocket.isClosed()) {
            if (isAcceptingNewClients()) {
                addAndStartNewClient();
            }
        }
    }

    public static synchronized void terminate() throws IOException {
        LOGGER.info("Terminating task started.");
        acceptingNewClients = false;
        for (Client client: activeClients.values()) {
            client.closeSocket();
        }
        StoringTask.getInstance().run();
        serverSocket.close();
        LOGGER.info("Terminating task finalized.");
    }

    private void addAndStartNewClient() {
        try {
            Client client = new Client(serverSocket.accept());
            activeClients.put(client.getClientId(), client);
            client.start();
            LOGGER.debug("New client with id: {}.", client.getClientId());
        } catch (SocketException ignore) {
        } catch (IOException e) {
            LOGGER.debug("Exception: {}.", e.getMessage());
        }

    }

    public synchronized void removeFromActiveClients(int id) {
        Client client = activeClients.remove(id);
        if (client != null) {
            LOGGER.debug("Removing client with id: {} from active clients.", id);
            LOGGER.debug("Current active client count: {}.", activeClients.size());
        }
    }

    private static boolean isAcceptingNewClients() {
        return activeClients.size() < Application.getMaxConcurrentConnections() && acceptingNewClients;
    }
}
