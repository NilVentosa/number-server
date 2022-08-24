package xyz.ventosa.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xyz.ventosa.Application;
import xyz.ventosa.task.StoringTask;
import xyz.ventosa.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionHandlerTest {

    NumberServer numberServer;

    NumberServer numberServerMock;

    ServerSocket serverSocketMock;

    StoringTask storingTask;

    Socket socketMock;

    ConnectionHandler connectionHandler;

    @BeforeEach
    void setup() {
        numberServer = new NumberServer(Integer.parseInt(Constants.DEFAULT_PORT));
        numberServerMock = Mockito.mock(NumberServer.class);
        serverSocketMock = Mockito.mock(ServerSocket.class);
        storingTask = Mockito.mock(StoringTask.class);
        socketMock = Mockito.mock(Socket.class);
        connectionHandler = new ConnectionHandler(new Application(4000, 5, 10000, "numbers.log"));
    }

    @AfterEach
    void tearDown() throws IOException {
        numberServer.getServerSocket().close();
    }

    @Test
    void isAcceptingNewConnections_true() {
        assertTrue(connectionHandler.isAcceptingNewConnections(1));
    }

    @Test
    void isAcceptingNewConnections_false() {
        assertFalse(connectionHandler.isAcceptingNewConnections(0));
    }

//    @Test
//    void terminateAllConnections_callsRemoveFromActiveConnections() throws IOException {
//        Application applicationMock = Mockito.mock(Application.class);
//        SocketHandler ch = new SocketHandler(applicationMock);
//
//        Socket socketMock = Mockito.mock(Socket.class);
//        SocketHandler connectionHandlerSpy = Mockito.spy(ch);
//
//        Mockito.doNothing().when(storingTask).run();
//        Mockito.when(applicationMock.getNumberServer()).thenReturn(numberServerMock);
//        Mockito.when(numberServerMock.getServerSocket()).thenReturn(serverSocketMock);
//        Mockito.when(serverSocketMock.accept()).thenReturn(socketMock);
//        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("123456789".getBytes()));
//
//        connectionHandlerSpy.handleNewConnection();
//        connectionHandlerSpy.handleNewConnection();
//        connectionHandlerSpy.terminateAllconnections();
//
//        Mockito.verify(connectionHandlerSpy, Mockito.atLeast(1)).removeFromActiveconnections(1);
//        Mockito.verify(connectionHandlerSpy, Mockito.atLeast(1)).removeFromActiveconnections(2);
//
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = { "9999999999", "", "1", "carbonara", "d", "TERminate", "terminates" })
//    void processconnectionInput_invalidInputsThrowException(String connectionInput) {
//        assertThrows(NumberServerException.class, () -> connectionHandler.processInput(connectionInput));
//
//    }
//
//    @Test
//    void processconnectionInput_nullInputThrowsException() {
//        assertThrows(NumberServerException.class, () -> connectionHandler.processInput(null));
//    }
//
//    @Test
//    void processconnectionInput_terminate() throws IOException {
//        Application applicationMock = Mockito.mock(Application.class);
//        SocketHandler ch = new SocketHandler(applicationMock);
//        ch.processInput("terminate");
//        Mockito.verify(applicationMock).terminateApplication();
//    }
//
//    @Test
//    void processconnectionInput_correctInput() throws IOException {
//        try (MockedStatic<StoringTask> mockedStatic = Mockito.mockStatic(StoringTask.class)) {
//            connectionHandler.processInput("123456789");
//            mockedStatic.verify(() -> StoringTask.processNumber(123456789));
//        }
//    }

}