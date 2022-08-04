package xyz.ventosa;

import picocli.CommandLine;
import picocli.CommandLine.*;
import xyz.ventosa.application.Application;

import java.util.concurrent.Callable;

import static xyz.ventosa.application.Constants.*;

@CommandLine.Command(name = "number-server", mixinStandardHelpOptions = true,
    version = "number-server 1.0", description = "Starts a number server.", showDefaultValues = true)
public class Command implements Callable<Integer> {

    @Option(
            names = {"-p", "--port"},
            description = "The port the server will listen to.",
            defaultValue = DEFAULT_PORT)
    private int port;

    @Option(
            names = {"-m", "--max-connections"},
            description = "The maximum number of concurrent clients.",
            defaultValue = DEFAULT_MAX_CONCURRENT_CLIENTS)
    private int maxConcurrentConnections;

    @Option(
            names = {"-r", "--report-frequency"},
            description = "How often (in milliseconds) the report will be printed.",
            defaultValue = DEFAULT_REPORT_FREQUENCY)
    private int reportFrequency;

    @Option(
            names = {"-f", "--file-name"},
            description = "File name to print the log of numbers.",
            defaultValue = DEFAULT_FILE_NAME)
    private String fileName;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Command()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() {
        Application.configureInstance(port, maxConcurrentConnections, reportFrequency, fileName);
        return Application.start();
    }
}