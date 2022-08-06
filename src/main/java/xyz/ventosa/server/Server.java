package xyz.ventosa.server;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.client.ClientHandler;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;


@Log4j2
@AllArgsConstructor
public class Server {
    private final int port;

    private final int maxConcurrentConnections;

    private final int reportFrequency;

    private final String fileName;

    public void startServer() {
        ServerSocket serverSocket = startServerSocket();
        startTasks();
        startHandlingClients(serverSocket);
    }

    protected ServerSocket startServerSocket() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server listening on port: {}.", serverSocket.getLocalPort());
            return serverSocket;
        }
        catch (IOException e) {
            log.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            StoringTask.flush();
            System.exit(1);
            return null;
        }
    }

    protected void startTasks() {
        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);
    }

    protected void startHandlingClients(ServerSocket serverSocket) {
        log.info("Starting to handle clients.");
        ClientHandler clientHandler = new ClientHandler(serverSocket);
        while (!serverSocket.isClosed()) {
            if (clientHandler.isAcceptingNewClients(maxConcurrentConnections)) {
                clientHandler.acceptNewClient();
            }
        }
    }

}
