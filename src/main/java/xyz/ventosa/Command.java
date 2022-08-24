package xyz.ventosa;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.*;

import static xyz.ventosa.util.Constants.*;

@CommandLine.Command(name = "number-server",
                     mixinStandardHelpOptions = true,
                     version = "1.0",
                     description = "Starts a number server.",
                     showDefaultValues = true)
@Log4j2
public class Command implements Runnable {
    @Option(names = { "-p", "--port" },
            description = "The port the server will listen to.",
            defaultValue = DEFAULT_PORT)
    private int port;

    @Option(names = { "-m", "--max-connections" },
            description = "The maximum number of concurrent connections. Minimum value 1.",
            defaultValue = DEFAULT_MAX_CONCURRENT_CONNECTIONS)
    private int maxConcurrentConnections;

    @Option(names = { "-r", "--report-frequency" },
            description = "How often (in milliseconds) the report will be printed. Minimum value 100.",
            defaultValue = DEFAULT_REPORT_FREQUENCY)
    private int reportFrequency;

    @Option(names = { "-f", "--file-name" },
            description = "File name to print the log of numbers.",
            defaultValue = DEFAULT_FILE_NAME)
    private String fileName;

    public static void main(String[] args) {
        new CommandLine(new Command()).execute(args);
    }

    @Override
    public void run() {
        if (maxConcurrentConnections < 1) {
            log.error("Max concurrent connections cannot be less than 1.");
            System.exit(1);
        }
        if (reportFrequency < 100) {
            log.error("Report frequency cannot be less than 100.");
            System.exit(1);
        }

        new Application(port, maxConcurrentConnections, reportFrequency, fileName).startApplication();
    }
}