package xyz.ventosa.server;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@RequiredArgsConstructor
public class SocketHandler implements Runnable {

    private final Map<Integer, Socket> activeClientList = new ConcurrentHashMap<>();

    @Setter
    private boolean acceptingNewClients = true;

    private int nextClientId = 0;

    private final Application application;

    public void handleNewConnection() {
        try {
            Socket socket = application.getNumberServer().getServerSocket().accept();
            nextClientId++;
            log.debug("Adding client with id: {} to active clients.", nextClientId);
            activeClientList.put(nextClientId, socket);
            startConnection(nextClientId);
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.debug("Exception: {}.", e.getMessage());
        }
    }

    private void startConnection(Integer id) {
        new Thread(() -> {
            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(activeClientList.get(id).getInputStream()))) {
                while (!activeClientList.get(id).isClosed()) {
                    this.processInput(inputReader.readLine());
                }
            }
            catch (IOException e) {
                log.debug("{}: {}.", e.getClass().getSimpleName(), e.getMessage());
            }
            finally {
                terminateConnection(id);
            }
        }).start();
        log.debug("New client with id: {}.", id);
    }

    protected void terminateConnection(Integer id) {
        try {
            if (!activeClientList.get(id).isClosed()) {
                log.debug("Closing socket for client with id: {}.", id);
                activeClientList.get(id).close();
            }
            if (activeClientList.remove(id) != null) {
                log.debug("Removed client with id: {} from active clients.", id);
            }
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.debug("Exception in endClient: {}.", e.getMessage());
        }
    }

    public boolean isAcceptingNewClients(int maxConcurrentClients) {
        return acceptingNewClients && activeClientList.size() < maxConcurrentClients;
    }

    public void terminateAllClients() {
        log.info("Terminating all clients.");
        for (Integer id : activeClientList.keySet()) {
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
        log.info("Starting to handle clients.");
        while (application.getNumberServer().isServerSocketOpen()) {
            if (isAcceptingNewClients(application.getMaxConcurrentClients())) {
                handleNewConnection();
            }
        }
    }
}
