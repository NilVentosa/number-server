package xyz.ventosa;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import xyz.ventosa.server.SocketHandler;
import xyz.ventosa.server.NumberServer;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

@Log4j2
@RequiredArgsConstructor
public class Application {

    @Getter
    private NumberServer numberServer = null;

    @Getter
    private SocketHandler socketHandler = null;

    private final int port;

    @Getter
    private final int maxConcurrentClients;

    private final int reportFrequency;

    private final String fileName;

    public void startApplication() {

        numberServer = new NumberServer(port);

        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);

        socketHandler = new SocketHandler(this);
        new Thread(socketHandler).start();

    }

    public void terminateApplication() {
        log.info("Terminating task started.");
        socketHandler.setAcceptingNewClients(false);
        socketHandler.terminateAllClients();
        numberServer.terminateServer();
        StoringTask.stopStoringTask();
        ReportingTask.logReport(true);
        log.info("Terminating task ended successfully.");
        System.exit(0);
    }
}
