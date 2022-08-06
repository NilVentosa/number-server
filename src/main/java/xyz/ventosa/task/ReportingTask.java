package xyz.ventosa.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Timer;
import java.util.TimerTask;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportingTask extends TimerTask {

    private static int accumulatedNumbers;

    private static int accumulatedDuplicates;

    @Override
    public void run() {
        logReport(false);
    }

    public static void startReportingTask(int frequency) {
        log.info("Starting reporting task.");
        new Timer().schedule(new ReportingTask(), frequency, frequency);
    }

    public static void logReport(boolean debug) {
        int totalNumbers = StoringTask.getSubmittedNumbersSize();
        int totalDuplicates = StoringTask.getDuplicates();

        int iterationNumbers = totalNumbers - accumulatedNumbers;
        int iterationDuplicates = totalDuplicates - accumulatedDuplicates;

        if (debug) {
            log.debug("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates, totalNumbers);
        } else {
            log.info("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates, totalNumbers);
        }


        accumulatedNumbers = totalNumbers;
        accumulatedDuplicates = totalDuplicates;
    }
}
