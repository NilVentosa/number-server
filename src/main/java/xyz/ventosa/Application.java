package xyz.ventosa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.ConnectionHandler;
import xyz.ventosa.server.NumberServer;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

public class Application {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    private NumberServer numberServer = null;

    private ConnectionHandler connectionHandler = null;

    private final int port;

    private final int maxConcurrentConnections;

    private final int reportFrequency;

    private final String fileName;

    public Application(int port, int maxConcurrentConnections, int reportFrequency, String fileName) {
        this.port = port;
        this.maxConcurrentConnections = maxConcurrentConnections;
        this.reportFrequency = reportFrequency;
        this.fileName = fileName;
    }

    public void startApplication() {

        numberServer = new NumberServer(port);

        ReportingTask.startReportingTask(reportFrequency);
        StoringTask.startStoringTask(fileName);

        connectionHandler = new ConnectionHandler(this);
        new Thread(connectionHandler).start();

    }

    public void terminateApplication() {
        LOGGER.info("Terminating task started.");
        connectionHandler.setAcceptingNewConnections(false);
        connectionHandler.terminateAllConnections();
        numberServer.terminateServer();
        StoringTask.stopStoringTask();
        ReportingTask.logReport(true);
        LOGGER.info("Terminating task ended successfully.");
        System.exit(0);
    }

    public NumberServer getNumberServer() {
        return this.numberServer;
    }

    public int getMaxConcurrentConnections() {
        return this.maxConcurrentConnections;
    }
}
