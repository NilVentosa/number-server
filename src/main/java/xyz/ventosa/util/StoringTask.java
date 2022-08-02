package xyz.ventosa.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import static xyz.ventosa.util.Constants.*;

public class StoringTask extends TimerTask {
    private static final Logger LOGGER = LogManager.getLogger("number-server");
    private static final StoringTask singleInstance = new StoringTask();

    private static PrintWriter output;

    private StoringTask() {
        try {
            output = new PrintWriter(new FileWriter(DEFAULT_FILE_NAME, false), false);
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    @Override
    public void run() {
        LOGGER.trace("Storing");
        output.println("hello");
        output.println("hello");
        output.flush();
    }

    public static StoringTask getInstance() {
        return singleInstance;
    }

    public void startTask() {
        new Timer().schedule(this, FLUSHING_FREQUENCY, FLUSHING_FREQUENCY);
    }
}
