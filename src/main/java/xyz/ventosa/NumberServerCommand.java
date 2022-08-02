package xyz.ventosa;

import picocli.CommandLine;
import picocli.CommandLine.*;

import java.util.concurrent.Callable;

@Command(name = "number-server", mixinStandardHelpOptions = true,
    version = "number-server 1.0", description = "Starts a number server.", showDefaultValues = true)
public class NumberServerCommand implements Callable<Integer> {

    @Option(names = {"-p", "--port"}, description = "The port the server will listen to.", defaultValue = "4000")
    private int port;

    @Option(names = {"-m", "--max-connections"}, description = "The maximum number of concurrent clients.", defaultValue = "5")
    private int maxConcurrentConnections;

    @Option(names = {"-r", "--report-frequency"}, description = "How often (in milliseconds) the report will be printed.", defaultValue = "10000")
    private int reportFrequency;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NumberServerCommand()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() {
        Server server = new Server(port, maxConcurrentConnections, reportFrequency);
        server.run();
        return  0;
    }
}