package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.Application;
import xyz.ventosa.task.StoringTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static xyz.ventosa.util.Util.*;

public class ConnectionHandler implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    private final Map<Integer, Socket> activeConnectionList = new ConcurrentHashMap<>();

    private boolean acceptingNewConnections = true;

    private int nextConnectionId = 0;

    private final Application application;

    public ConnectionHandler(Application application) {
        this.application = application;
    }

    public void handleNewConnection() {
        try {
            Socket socket = application.getNumberServer().getServerSocket().accept();
            nextConnectionId++;
            LOGGER.debug("Adding connection with id: {} to active connections.", nextConnectionId);
            activeConnectionList.put(nextConnectionId, socket);
            startConnection(nextConnectionId);
        }
        catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.debug("Exception: {}.", e.getMessage());
        }
    }

    private void startConnection(Integer id) {
        new Thread(() -> {
            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(activeConnectionList.get(id).getInputStream()))) {
                while (!activeConnectionList.get(id).isClosed()) {
                    this.processInput(inputReader.readLine());
                }
            }
            catch (IOException e) {
                LOGGER.debug("{}: {}.", e.getClass().getSimpleName(), e.getMessage());
            }
            finally {
                terminateConnection(id);
            }
        }).start();
        LOGGER.debug("New connection with id: {}.", id);
    }

    protected void terminateConnection(Integer id) {
        try {
            if (!activeConnectionList.get(id).isClosed()) {
                LOGGER.debug("Closing socket for connection with id: {}.", id);
                activeConnectionList.get(id).close();
            }
            if (activeConnectionList.remove(id) != null) {
                LOGGER.debug("Removed connection with id: {} from active connections.", id);
            }
        }
        catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.debug("Exception in endConnection: {}.", e.getMessage());
        }
    }

    public boolean isAcceptingNewConnections(int maxConcurrentConnections) {
        return acceptingNewConnections && activeConnectionList.size() < maxConcurrentConnections;
    }

    public void terminateAllConnections() {
        LOGGER.info("Terminating all connections.");
        for (Integer id : activeConnectionList.keySet()) {
            terminateConnection(id);
        }
    }

    protected void processInput(String input) throws NumberServerException {
        Integer number = getValidNumber(input);
        if (number != null) {
            StoringTask.processNumber(number);
        }
        else if (input.equals("terminate")) {
            application.terminateApplication();
        }
        else {
            throw new NumberServerException(String.format("Invalid input: %s", input));
        }
    }

    @Override
    public void run() {
        LOGGER.info("Starting to handle connections.");
        while (application.getNumberServer().isServerSocketOpen()) {
            if (isAcceptingNewConnections(application.getMaxConcurrentConnections())) {
                handleNewConnection();
            }
        }
    }

    public void setAcceptingNewConnections(boolean acceptingNewConnections) {
        this.acceptingNewConnections = acceptingNewConnections;
    }
}
