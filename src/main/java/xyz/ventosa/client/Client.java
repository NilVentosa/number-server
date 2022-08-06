package xyz.ventosa.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

@Log4j2
@AllArgsConstructor
public class Client implements Runnable {
    private final Socket socket;

    @Getter
    private final int clientId;

    private final ClientHandler clientHandler;

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            while (!this.socket.isClosed()) {
                clientHandler.processClientInput(inputReader.readLine());
            }
        }
        catch (IOException e) {
            log.debug("{}: {}.", e.getClass().getSimpleName(), e.getMessage());
        }
        finally {
            terminateClient();
        }
    }

    protected void terminateClient() {
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