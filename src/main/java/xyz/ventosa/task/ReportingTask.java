package xyz.ventosa.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class ReportingTask extends TimerTask {

    private static final Logger LOGGER = LogManager.getLogger("xyz.ventosa");

    private static int accumulatedNumbers;

    private static int accumulatedDuplicates;

    private ReportingTask() {
    }

    @Override
    public void run() {
        logReport(false);
    }

    public static void startReportingTask(int frequency) {
        LOGGER.info("Starting reporting task.");
        new Timer().schedule(new ReportingTask(), frequency, frequency);
    }

    public static void logReport(boolean debug) {
        int totalNumbers = StoringTask.getSubmitted();
        int totalDuplicates = StoringTask.getDuplicates();

        int iterationNumbers = totalNumbers - accumulatedNumbers;
        int iterationDuplicates = totalDuplicates - accumulatedDuplicates;

        // When debug is true it will log a final report before terminating
        if (debug) {
            LOGGER.debug("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates,
                    totalNumbers);
        }
        else {
            LOGGER.info("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates, totalNumbers);
        }

        accumulatedNumbers = totalNumbers;
        accumulatedDuplicates = totalDuplicates;
    }
}
