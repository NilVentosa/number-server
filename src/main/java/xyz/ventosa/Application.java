package xyz.ventosa;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.client.ClientHandler;
import xyz.ventosa.server.NumberServer;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Application {

    private static NumberServer numberServer;

    private static ClientHandler clientHandler;

    public static void startApplication(int port, int maxConcurrentConnections, int reportFrequency, String fileName) {

        numberServer = new NumberServer(port);

        startTasks(reportFrequency, fileName);

        clientHandler = new ClientHandler(numberServer);
        startHandlingClients(maxConcurrentConnections);
    }

    public static void terminateApplication() {
        log.info("Terminating task started.");
        clientHandler.setAcceptingNewClients(false);
        clientHandler.terminateAllClients();
        numberServer.terminateServer();
        StoringTask.flush();
        ReportingTask.logReport(true);
        log.info("Terminating task ended successfully.");
        System.exit(0);
    }

    private static void startTasks(int reportFrequency, String fileName) {
        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);
    }

    private static void startHandlingClients(int maxConcurrentConnections) {
        log.info("Starting to handle clients.");
        while (numberServer.isServerSocketOpen()) {
            if (clientHandler.isAcceptingNewClients(maxConcurrentConnections)) {
                clientHandler.handleNewClient();
            }
        }
    }
}
