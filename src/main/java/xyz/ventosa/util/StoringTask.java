package xyz.ventosa.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static xyz.ventosa.util.Constants.*;

public class StoringTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final StoringTask singleInstance = new StoringTask();

    private static final Set<String> submittedNumbers = new HashSet<>();
    private static int duplicates;

    private static PrintWriter output;

    private StoringTask() {
        try {
            output = new PrintWriter(new FileWriter(DEFAULT_FILE_NAME, false), false);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public static synchronized void processCorrectInput(String number) {
        if (submittedNumbers.add(number)) {
            output.println(number);
        } else {
            duplicates++;
        }
    }

    @Override
    public void run() {
        LOGGER.trace("Storing");
        output.flush();
    }

    public static StoringTask getInstance() {
        return singleInstance;
    }

    public void startTask() {
        new Timer().schedule(this, FLUSHING_FREQUENCY, FLUSHING_FREQUENCY);
    }

    public static int getSubmittedNumbersSize() {
        return submittedNumbers.size();
    }

    public static int getDuplicates() {
        return duplicates;
    }
}
