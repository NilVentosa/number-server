package xyz.ventosa.task;

import lombok.extern.log4j.Log4j2;

import java.util.Timer;
import java.util.TimerTask;

@Log4j2
public class ReportingTask extends TimerTask {
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

        log.info("Received {} unique numbers, {} duplicates. Unique total: {}", iterationNumbers, iterationDuplicates, totalNumbers);

        accumulatedNumbers = totalNumbers;
        accumulatedDuplicates = totalDuplicates;
    }

    public static void startReportingTask(int frequency) {
        log.info("Starting reporting task.");
        new Timer().schedule(new ReportingTask(), frequency, frequency);
    }
}
