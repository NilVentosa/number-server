package xyz.ventosa.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class ReportingTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private int iterationNumbers;
    private int iterationDuplicates;

    private ReportingTask() {}

    @Override
    public void run() {
        int tempNumbers = StoringTask.getSubmittedNumbersSize();
        int tempDuplicates = StoringTask.getDuplicates();
        LOGGER.info(
                "Received {} unique numbers, {} duplicates. Unique total: {}",
                tempNumbers - iterationNumbers,
                tempDuplicates - iterationDuplicates,
                tempNumbers);
        iterationNumbers = tempNumbers;
        iterationDuplicates = tempDuplicates;
    }

    public static void startReportingTask(int frequency) {
        LOGGER.info("Starting reporting task.");
        new Timer().schedule(new ReportingTask(), frequency, frequency);
    }
}
