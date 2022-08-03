package xyz.ventosa.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.application.Application;
import xyz.ventosa.client.Client;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import java.io.IOException;
import java.net.ServerSocket;

import static xyz.ventosa.util.Constants.FLUSHING_FREQUENCY;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Server instance = new Server();
    private static final ServerSocket serverSocket;

    private Server() {}

    static {
        try {
            serverSocket = new ServerSocket(Application.getPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Server getInstance() {
        return instance;
    }

    @Override
    public void run() {
        try {
            LOGGER.info("Server listening on port {}.", serverSocket.getLocalPort());
            ReportingTask.getInstance().startReportingTask(Application.getReportFrequency());
            StoringTask.getInstance().startStoringTask(FLUSHING_FREQUENCY);
            while (!serverSocket.isClosed()) {
                new Client(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            LOGGER.debug("Server exception: {}", e.getMessage());
        }
    }

    public void terminate() throws IOException {
        serverSocket.close();
    }
}
