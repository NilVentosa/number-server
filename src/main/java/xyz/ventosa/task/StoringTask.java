package xyz.ventosa.task;

import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static xyz.ventosa.util.Constants.*;

@Log4j2
public class StoringTask extends TimerTask {
    private static final Set<String> submittedNumbers = new HashSet<>();

    private static int duplicates;

    private static PrintWriter output;

    private StoringTask() {
    }

    @Override
    public void run() {
        flush();
    }

    public static synchronized void processNumber(String number) {
        if (submittedNumbers.add(number)) {
            output.println(number);
        }
        else {
            duplicates++;
        }
    }

    public static void startStoringTask(String filename) {
        log.info("Starting storing task.");
        try {
            output = new PrintWriter(new FileWriter(filename, false), false);
        }
        catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            flush();
        }
        new Timer().schedule(new StoringTask(), FLUSHING_FREQUENCY, FLUSHING_FREQUENCY);
    }

    public static int getSubmittedNumbersSize() {
        return submittedNumbers.size();
    }

    public static int getDuplicates() {
        return duplicates;
    }

    public static void flush() {
        log.trace("Storing");
        output.flush();
    }
}
