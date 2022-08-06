package xyz.ventosa.client;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.server.NumberServerException;
import xyz.ventosa.task.StoringTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import static xyz.ventosa.util.Util.isTerminate;
import static xyz.ventosa.util.Util.isValidNumber;

@Log4j2
public class Client implements Runnable {
    private final Socket socket;

    @Getter
    private final int clientId;

    private static int clientInstanceCount;

    private final ClientHandler clientHandler;

    public Client(Socket socket, ClientHandler clientHandler) {
        clientInstanceCount++;
        this.clientId = clientInstanceCount;
        this.socket = socket;
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            while (!this.socket.isClosed()) {
                processInput(inputReader.readLine());
            }
        }
        catch (IOException e) {
            log.debug("{}: {}.", e.getClass().getSimpleName(), e.getMessage());
        }
        finally {
            terminateClient();
        }
    }

    private void processInput(String input) throws NumberServerException {
        if (isValidNumber(input)) {
            StoringTask.processNumber(input);
        }
        else if (isTerminate(input)) {
            clientHandler.terminateApplication();
        }
        else {
            throw new NumberServerException(String.format("Invalid input: %s", input));
        }
    }

    void terminateClient() {
        try {
            if (!socket.isClosed()) {
                log.debug("Closing socket for client with id: {}.", clientId);
                socket.close();
            }
            clientHandler.removeFromActiveClients(clientId);
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.debug("Exception in endClient: {}.", e.getMessage());
        }
    }
}