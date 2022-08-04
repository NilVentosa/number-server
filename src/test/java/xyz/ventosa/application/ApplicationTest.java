package xyz.ventosa.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ApplicationTest {


    @Test
    public void configureInstance() {
        Assertions.assertEquals(Integer.parseInt(Constants.DEFAULT_PORT), Application.getPort());
        Assertions.assertEquals(Integer.parseInt(Constants.DEFAULT_MAX_CONCURRENT_CLIENTS), Application.getMaxConcurrentConnections());
        Assertions.assertEquals(Integer.parseInt(Constants.DEFAULT_REPORT_FREQUENCY), Application.getReportFrequency());

        int newPort = 3000;
        int newMaxConcurrentConnections = 2;
        int newReportFrequency = 200;

        Application.configureInstance(newPort, newMaxConcurrentConnections, newReportFrequency);

        Assertions.assertEquals(newPort, Application.getPort());
        Assertions.assertEquals(newMaxConcurrentConnections, Application.getMaxConcurrentConnections());
        Assertions.assertEquals(newReportFrequency, Application.getReportFrequency());
    }
}