package xyz.ventosa.application;

import org.junit.Assert;
import org.junit.Test;
import xyz.ventosa.util.Constants;

public class ApplicationTest {


    @Test
    public void configureInstance() {
        Assert.assertEquals(Integer.parseInt(Constants.DEFAULT_PORT), Application.getPort());
        Assert.assertEquals(Integer.parseInt(Constants.DEFAULT_MAX_CONCURRENT_CLIENTS), Application.getMaxConcurrentConnections());
        Assert.assertEquals(Integer.parseInt(Constants.DEFAULT_REPORT_FREQUENCY), Application.getReportFrequency());

        int newPort = 3000;
        int newMaxConcurrentConnections = 2;
        int newReportFrequency = 200;

        Application.configureInstance(newPort, newMaxConcurrentConnections, newReportFrequency);

        Assert.assertEquals(newPort, Application.getPort());
        Assert.assertEquals(newMaxConcurrentConnections, Application.getMaxConcurrentConnections());
        Assert.assertEquals(newReportFrequency, Application.getReportFrequency());
    }
}