package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.application.Application;
import xyz.ventosa.handler.ClientHandler;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import static xyz.ventosa.application.Constants.FLUSHING_FREQUENCY;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Server instance = new Server();
    private static final ServerSocket serverSocket;

    private Server() {}

    static {
        try {
            serverSocket = new ServerSocket(Application.getPort());
            LOGGER.info("Server listening on port {}.", serverSocket.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Server getInstance() {
        return instance;
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        ReportingTask.getInstance().startReportingTask(Application.getReportFrequency());
        StoringTask.getInstance().startStoringTask(FLUSHING_FREQUENCY);
        while (!serverSocket.isClosed()) {
            if (ClientHandler.isAcceptingNewClients()) {
                ClientHandler.addAndStartNewClient();
            }
        }
    }

    public static void terminate() {
        LOGGER.info("Terminating task started.");
        ClientHandler.endAllClients();
        StoringTask.flush();
        try {
            serverSocket.close();
        } catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        } catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            System.exit(1);
        }
        LOGGER.info("Terminating task finalized.");
        System.exit(0);
    }

}
