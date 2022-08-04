package xyz.ventosa.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;
import xyz.ventosa.task.StoringTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputProcessor {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private InputProcessor() {}

    public static void processClientInput(Client client) {
        try (BufferedReader inputReader = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()))) {
            while (!client.getSocket().isClosed()) {
                processLine(inputReader.readLine());
            }
        } catch (ProcessingException e) {
            LOGGER.debug("Processing exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Client exception: {}", e.getMessage());
        } finally {
            client.endClient();
            Server.getInstance().removeFromActiveClients(client.getClientId());
        }
    }

    private static void processLine(String input) throws ProcessingException {
        if (input == null) {
            throw  new ProcessingException("The client closed the connection.");
        }
        if (input.equals("terminate")){
            Server.terminate();
        }
        if (!isNineDigits(input)) {
            throw  new ProcessingException(String.format("Invalid input: %s.", input));
        }
        StoringTask.processCorrectInput(input);
    }

    private static boolean isNineDigits(String input) {
        if (input.length() != 9) {
            return false;
        }
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }
}
