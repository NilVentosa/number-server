package xyz.ventosa.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;
import xyz.ventosa.task.StoringTask;

import static xyz.ventosa.application.Constants.*;

public class Application {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private static int port = Integer.parseInt(DEFAULT_PORT);
    private static int maxConcurrentConnections = Integer.parseInt(DEFAULT_MAX_CONCURRENT_CLIENTS);
    private static int reportFrequency = Integer.parseInt(DEFAULT_REPORT_FREQUENCY);
    private static String fileName = DEFAULT_FILE_NAME;

    private Application() {}

    public static void configureInstance(int portArg, int maxConcurrentConnectionsArg, int reportFrequencyArg, String fileNameArg) {
        port = portArg;
        maxConcurrentConnections = maxConcurrentConnectionsArg;
        reportFrequency = reportFrequencyArg;
        fileName = fileNameArg;
    }

    public static int start() {
        try {
            Server.getInstance().run();
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return 1;
        } catch (ExceptionInInitializerError e) {
            LOGGER.error(e.getException().getMessage());
            e.printStackTrace();
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

    public static String getFileName() {
        return fileName;
    }

    public static void exit(int code) {
        StoringTask.flush();
        System.exit(code);
    }
}
