package xyz.ventosa.task;

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

    static {
        try {
            output = new PrintWriter(new FileWriter(DEFAULT_FILE_NAME, false), false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private StoringTask() { }

    @Override
    public void run() {
        flush();
    }

    public static synchronized void processCorrectInput(String number) {
        if (submittedNumbers.add(number)) {
            output.println(number);
        } else {
            duplicates++;
        }
    }

    public static StoringTask getInstance() {
        return singleInstance;
    }

    public void startStoringTask(int flushingFrequency) {
        LOGGER.info("Starting storing task.");
        new Timer().schedule(this, flushingFrequency, flushingFrequency);
    }

    public static int getSubmittedNumbersSize() {
        return submittedNumbers.size();
    }

    public static int getDuplicates() {
        return duplicates;
    }

    public static void flush(){
        LOGGER.trace("Storing");
        output.flush();
    }
}
