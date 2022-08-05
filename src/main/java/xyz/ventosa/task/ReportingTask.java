package xyz.ventosa.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class ReportingTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private int accumulatedNumbers;

    private int accumulatedDuplicates;

    private ReportingTask() {
    }

    @Override
    public void run() {
        int totalNumbers = StoringTask.getSubmittedNumbersSize();
        int totalDuplicates = StoringTask.getDuplicates();

        int iterationNumbers = totalNumbers - accumulatedNumbers;
        int iterationDuplicates = totalDuplicates - accumulatedDuplicates;

        LOGGER.info("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates, totalNumbers);

        accumulatedNumbers = totalNumbers;
        accumulatedDuplicates = totalDuplicates;
    }

    public static void startReportingTask(int frequency) {
        LOGGER.info("Starting reporting task.");
        new Timer().schedule(new ReportingTask(), frequency, frequency);
    }
}
