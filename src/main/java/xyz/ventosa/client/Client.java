package xyz.ventosa.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;
import xyz.ventosa.task.StoringTask;
import xyz.ventosa.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class Client extends Thread {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private final Socket socket;
    private static int instanceCount;
    private final int clientId;

    public Client(Socket socket) {
        instanceCount++;
        this.clientId = instanceCount;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                processInput(inputReader);
            }
        } catch (IOException e) {
            LOGGER.debug("Client exception: {}", e.getMessage());
        } finally {
            if (Server.getInstance().getActiveClients().containsKey(clientId)) {
                Server.getInstance().removeFromActiveClients(clientId);
            }
        }
    }

    private void processInput(BufferedReader inputReader) throws IOException {
        String input = null;
        try {
           input = inputReader.readLine();
        } catch (SocketException e) {
            LOGGER.debug("Error reading line from inputReader: {}.", e.getMessage());
        }

        if (input == null) {
            LOGGER.debug("Invalid input: null.");
            return;
        }
        if (input.equals("terminate")){
            Server.terminate();
            return;
        }
        if (!Util.isNineDigits(input)) {
            LOGGER.debug("Invalid input: {}.", input);
            closeSocket();
            return;
        }
        StoringTask.processCorrectInput(input);
    }

    public int getClientId() {
        return clientId;
    }

    public void closeSocket() throws IOException {
        LOGGER.debug("Closing socket for client with id: {}.", clientId);
        this.socket.close();
        Server.getInstance().removeFromActiveClients(clientId);
    }

}
