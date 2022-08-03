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

public class Client extends Thread {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private final Socket socket;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (!socket.isClosed()) {
                processInput(inputReader.readLine());
            }
        } catch (IOException e) {
            LOGGER.debug("Exception: {}", e.getMessage());
        }
    }

    private void processInput(String input) throws IOException {
        if (input == null) {
            return;
        }
        if (input.equals("terminate")){
            Server.getInstance().terminate();
            return;
        }
        if (!Util.isNineDigits(input)) {
            LOGGER.debug("Invalid input: {}.", input);
            socket.close();
            return;
        }
        StoringTask.processCorrectInput(input);
    }

}
