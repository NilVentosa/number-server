package xyz.ventosa.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import xyz.ventosa.task.ReportingTask;
import xyz.ventosa.task.StoringTask;

import static xyz.ventosa.util.Constants.*;

class ServerTest {

    Server server;

    @BeforeEach
    void setup() {
        server = new Server(
                Integer.parseInt(DEFAULT_PORT),
                Integer.parseInt(DEFAULT_MAX_CONCURRENT_CLIENTS),
                Integer.parseInt(DEFAULT_REPORT_FREQUENCY),
                DEFAULT_FILE_NAME);
    }

    @Test
    void startServer() {
        Server server1 = Mockito.spy(server);
        Mockito.doNothing().when(server1).startServerSocket();
        Mockito.doNothing().when(server1).startTasks();
        Mockito.doNothing().when(server1).startHandlingClients();

        server1.startServer();

        Mockito.verify(server1).startTasks();
        Mockito.verify(server1).startServerSocket();
        Mockito.verify(server1).startHandlingClients();
    }

    @Test
    void startTasks() {


    }
}