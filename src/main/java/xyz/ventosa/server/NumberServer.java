package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

public class NumberServer {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    private final int maxConcurrentConnections;

    private ServerSocket serverSocket;

    private final ConnectionHandler connectionHandler;

    public NumberServer(int port, int maxConcurrentConnections, int reportFrequency, String fileName) {
        this.maxConcurrentConnections = maxConcurrentConnections;

        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server listening on port: {}.", serverSocket.getLocalPort());
        }
        catch (IOException e) {
            LOGGER.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);

        connectionHandler = new ConnectionHandler(this);
        new Thread(connectionHandler).start();
    }

    public void terminate() {
        LOGGER.info("Terminating server");
        LOGGER.info("Terminating task started.");
        connectionHandler.setAcceptingNewConnections(false);
        connectionHandler.terminateAllConnections();
        try {
            serverSocket.close();
        }
        catch (SocketException e) {
            LOGGER.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.error("Exception in terminate: {}.", e.getMessage());
            System.exit(1);
        }
        StoringTask.stopStoringTask();
        ReportingTask.logReport(true);
        LOGGER.info("Terminating task ended successfully.");
        System.exit(0);
    }

    public boolean isServerSocketOpen() {
        if (serverSocket != null) {
            return !serverSocket.isClosed();
        }
        return false;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public int getMaxConcurrentConnections() {
        return maxConcurrentConnections;
    }
}
