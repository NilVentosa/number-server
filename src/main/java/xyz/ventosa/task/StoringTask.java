package xyz.ventosa.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.ventosa.server.Server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static xyz.ventosa.Constants.*;

public class StoringTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");

    private static final Set<String> submittedNumbers = new HashSet<>();
    private static int duplicates;

    private static PrintWriter output;

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

    public static void startStoringTask(String filename) {
        LOGGER.info("Starting storing task.");
        try {
            output = new PrintWriter(new FileWriter(filename, false), false);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            Server.exitApplication(1);
        }
        new Timer().schedule(new StoringTask(), FLUSHING_FREQUENCY, FLUSHING_FREQUENCY);
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
