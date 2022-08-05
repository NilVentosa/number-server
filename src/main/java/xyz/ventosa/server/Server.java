package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.handler.ClientHandler;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private ServerSocket serverSocket;
    private final int port;
    private final int maxConcurrentConnections;
    private final int reportFrequency;
    private final String fileName;

    public Server(int port, int maxConcurrentConnections, int reportFrequency, String fileName) {
        this.port = port;
        this.maxConcurrentConnections = maxConcurrentConnections;
        this.reportFrequency = reportFrequency;
        this.fileName = fileName;
    }

    public void startServer() {
        try{
            serverSocket = new ServerSocket(port);
            LOGGER.info("Server listening on port: {}.", serverSocket.getLocalPort());
        } catch (IOException e) {
            LOGGER.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            exitApplication(1);
        }

        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);
        while (!serverSocket.isClosed()) {
            if (ClientHandler.isAcceptingNewClients(maxConcurrentConnections)) {
                ClientHandler.acceptNewClient(serverSocket);
            }
        }
    }

    public static void exitApplication(int exitCode) {
        StoringTask.flush();
        System.exit(exitCode);
    }
}
