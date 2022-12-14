package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.task.StoringTask;
import xyz.ventosa.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectionHandler implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    private final Map<Integer, Socket> activeConnectionList = new ConcurrentHashMap<>();

    private boolean acceptingNewConnections = true;

    private int nextConnectionId = 0;

    private final NumberServer numberServer;

    private final Pattern pattern = Pattern.compile(Constants.VALID_INPUT_PATTERN);

    public ConnectionHandler(NumberServer numberServer) {
        this.numberServer = numberServer;
    }

    public void handleNewConnection() {
        try {
            Socket socket = numberServer.getServerSocket().accept();
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
        if (input == null) {
            throw new NumberServerException("Null input");
        }

        Matcher matcher = pattern.matcher(input);
        boolean matchFound = matcher.find();

        if (matchFound) {
            StoringTask.processNumber(Integer.parseInt(input));
        }
        else if (input.equals("terminate")) {
            numberServer.terminate();
        }
        else {
            throw new NumberServerException(String.format("Invalid input: %s", input));
        }
    }

    @Override
    public void run() {
        LOGGER.info("Starting to handle connections.");
        while (numberServer.isServerSocketOpen()) {
            if (isAcceptingNewConnections(numberServer.getMaxConcurrentConnections())) {
                handleNewConnection();
            }
        }
    }

    public void setAcceptingNewConnections(boolean acceptingNewConnections) {
        this.acceptingNewConnections = acceptingNewConnections;
    }
}
