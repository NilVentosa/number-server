package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.client.ClientHandler;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private final int port;
    private final int maxConcurrentConnections;
    private final int reportFrequency;
    private final String fileName;
    private ServerSocket serverSocket;

    public Server(int port, int maxConcurrentConnections, int reportFrequency, String fileName) {
        this.port = port;
        this.maxConcurrentConnections = maxConcurrentConnections;
        this.reportFrequency = reportFrequency;
        this.fileName = fileName;
    }

    public void startServer() {
        startServerSocket();
        startTasks();
        startHandlingClients();
    }

    void startServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server listening on port: {}.", serverSocket.getLocalPort());
        } catch (IOException e) {
            LOGGER.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            StoringTask.flush();
            System.exit(1);
        }
    }

    void startTasks() {
        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);
    }

    void startHandlingClients() {
        LOGGER.info("Starting to handle clients.");
        ClientHandler clientHandler = new ClientHandler(serverSocket);
        while (!serverSocket.isClosed()) {
            if (clientHandler.isAcceptingNewClients(maxConcurrentConnections)) {
                clientHandler.acceptNewClient();
            }
        }
    }

}
