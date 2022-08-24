package xyz.ventosa.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import static xyz.ventosa.util.Constants.*;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoringTask extends TimerTask {
    private static final boolean[] submittedNumbers = new boolean[1000000000];

    private static int duplicates;
    private static int submitted;

    private static PrintWriter output;

    @Override
    public void run() {
        flush();
    }

    public static synchronized void processNumber(int number) {
        if (output != null) {
            if (!submittedNumbers[number]) {
                submittedNumbers[number] = true;
                submitted++;
                output.println(number);
            }
            else {
                duplicates++;
            }
        }

    }

    public static void startStoringTask(String filename) {
        log.info("Starting storing task.");
        try {
            output = new PrintWriter(new FileWriter(filename, false), false);
        }
        catch (IOException e) {
            log.error(String.format("Log file %s could not be created: %s", filename, e.getMessage()));
            e.printStackTrace();
            System.exit(1);
        }
        new Timer().schedule(new StoringTask(), FLUSHING_FREQUENCY, FLUSHING_FREQUENCY);
    }

    protected static int getSubmitted() {
        return submitted;
    }

    protected static int getDuplicates() {
        return duplicates;
    }

    private static void flush() {
        log.trace("Storing");
        output.flush();
    }

    public static void stopStoringTask() {
        output.flush();
        output.close();
    }
}
