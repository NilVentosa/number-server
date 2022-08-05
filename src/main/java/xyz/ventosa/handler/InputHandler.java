package xyz.ventosa.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.NumberServerException;
import xyz.ventosa.server.Server;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;


public class InputHandler {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private InputHandler() {}

    public static void processLine(String input, ServerSocket serverSocket) throws NumberServerException {
        if (input == null) {
            throw  new NumberServerException("The client closed the connection.");
        }
        if (input.equals("terminate")){
            terminate(serverSocket);
        }
        if (!isNineDigits(input)) {
            throw  new NumberServerException(String.format("Invalid input: %s.", input));
        }
        StoringTask.processCorrectInput(input);
    }

    private static void terminate(ServerSocket serverSocket) {
        LOGGER.info("Terminating task started.");
        ClientHandler.endAllClients();
        StoringTask.flush();
        try {
            serverSocket.close();
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            Server.exitApplication(1);
        }
        LOGGER.info("Terminating task finalized.");
        Server.exitApplication(0);
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
