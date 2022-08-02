package xyz.ventosa.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class Reporter extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final Reporter singleInstance = new Reporter();

    private Reporter() {}

    @Override
    public void run() {
        LOGGER.info("Reporting: {}", StoringTask.getSubmittedNumbersSize());
    }

    public static Reporter getInstance() {
        return singleInstance;
    }

    public void startReporting(int frequency) {
        new Timer().schedule(this, frequency, frequency);
    }
}
