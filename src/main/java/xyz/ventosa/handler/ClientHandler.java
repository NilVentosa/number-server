package xyz.ventosa.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.NumberServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Map<Integer, Client> activeClientList = new ConcurrentHashMap<>();
    private static boolean acceptingNewClients = true;
    private static int instanceCount;
    private static ServerSocket serverSocket;

    private ClientHandler() {
    }

    public static synchronized void acceptNewClient(ServerSocket serverSocketArg) {
        serverSocket = serverSocketArg;
        try {
            Client client = new Client(serverSocket.accept());
            activeClientList.put(client.getClientId(), client);
            new Thread(client).start();
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

    public static boolean isAcceptingNewClients(int maxConcurrentConnections) {
        return activeClientList.size() < maxConcurrentConnections && acceptingNewClients;
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

    static class Client implements Runnable {

        private final Socket socket;
        private final int clientId;

        public Client(Socket socket) {
            instanceCount++;
            this.clientId = instanceCount;
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
                while (!this.socket.isClosed()) {
                    InputHandler.processLine(inputReader.readLine(), serverSocket);
                }
            } catch (NumberServerException e) {
                LOGGER.debug("Processing exception: {}.", e.getMessage());
            } catch (IOException e) {
                LOGGER.debug("Client exception: {}", e.getMessage());
            } finally {
                ClientHandler.endClient(this);
            }
        }

        public int getClientId() {
            return clientId;
        }

        public Socket getSocket() {
            return socket;
        }

    }
}
