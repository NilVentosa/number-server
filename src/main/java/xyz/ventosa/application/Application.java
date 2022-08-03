package xyz.ventosa.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;

import static xyz.ventosa.util.Constants.*;

public class Application {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private static int port = Integer.parseInt(DEFAULT_PORT);
    private static int maxConcurrentConnections = Integer.parseInt(DEFAULT_MAX_CONCURRENT_CLIENTS);
    private static int reportFrequency = Integer.parseInt(DEFAULT_REPORT_FREQUENCY);

    private Application() {}

    public static void configureInstance(int portArg, int maxConcurrentConnectionsArg, int reportFrequencyArg) {
        port = portArg;
        maxConcurrentConnections = maxConcurrentConnectionsArg;
        reportFrequency = reportFrequencyArg;
    }

    public static int start() {
        try {
            Server.getInstance().run();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            return 1;
        }
        return 0;
    }

    public static int getPort() {
        return port;
    }

    public static int getMaxConcurrentConnections() {
        return maxConcurrentConnections;
    }

    public static int getReportFrequency() {
        return reportFrequency;
    }
}
