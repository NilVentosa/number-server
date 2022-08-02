package xyz.ventosa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private final int port;
    private final int maxConcurrentConnections;
    private final int reportFrequency;

    public Server(int port, int maxConcurrentConnections, int reportFrequency) {
        this.port = port;
        this.maxConcurrentConnections = maxConcurrentConnections;
        this.reportFrequency = reportFrequency;
    }

    @Override
    public void run() {
        LOGGER.info(port);
        LOGGER.info(maxConcurrentConnections);
        LOGGER.debug(reportFrequency);
    }
}
