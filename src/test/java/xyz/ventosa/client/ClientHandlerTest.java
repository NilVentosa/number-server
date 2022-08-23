package xyz.ventosa.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import xyz.ventosa.Application;
import xyz.ventosa.server.NumberServerException;
import xyz.ventosa.server.NumberServer;
import xyz.ventosa.server.SocketHandler;
import xyz.ventosa.task.StoringTask;
import xyz.ventosa.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    NumberServer numberServer;

    NumberServer numberServerMock;

    ServerSocket serverSocketMock;

    StoringTask storingTask;

    Socket socketMock;

    SocketHandler clientHandler;

    @BeforeEach
    void setup() {
        numberServer = new NumberServer(Integer.parseInt(Constants.DEFAULT_PORT));
        numberServerMock = Mockito.mock(NumberServer.class);
        serverSocketMock = Mockito.mock(ServerSocket.class);
        storingTask = Mockito.mock(StoringTask.class);
        socketMock = Mockito.mock(Socket.class);
        clientHandler = new SocketHandler(new Application(4000, 5, 10000, "numbers.log"));
    }

    @AfterEach
    void tearDown() throws IOException {
        numberServer.getServerSocket().close();
    }

    @Test
    void isAcceptingNewClients_true() {
        assertTrue(clientHandler.isAcceptingNewClients(1));
    }

    @Test
    void isAcceptingNewClients_false() {
        assertFalse(clientHandler.isAcceptingNewClients(0));
    }

//    @Test
//    void terminateAllClients_callsRemoveFromActiveClients() throws IOException {
//        Application applicationMock = Mockito.mock(Application.class);
//        SocketHandler ch = new SocketHandler(applicationMock);
//
//        Socket socketMock = Mockito.mock(Socket.class);
//        SocketHandler clientHandlerSpy = Mockito.spy(ch);
//
//        Mockito.doNothing().when(storingTask).run();
//        Mockito.when(applicationMock.getNumberServer()).thenReturn(numberServerMock);
//        Mockito.when(numberServerMock.getServerSocket()).thenReturn(serverSocketMock);
//        Mockito.when(serverSocketMock.accept()).thenReturn(socketMock);
//        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("123456789".getBytes()));
//
//        clientHandlerSpy.handleNewConnection();
//        clientHandlerSpy.handleNewConnection();
//        clientHandlerSpy.terminateAllClients();
//
//        Mockito.verify(clientHandlerSpy, Mockito.atLeast(1)).removeFromActiveClients(1);
//        Mockito.verify(clientHandlerSpy, Mockito.atLeast(1)).removeFromActiveClients(2);
//
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = { "9999999999", "", "1", "carbonara", "d", "TERminate", "terminates" })
//    void processClientInput_invalidInputsThrowException(String clientInput) {
//        assertThrows(NumberServerException.class, () -> clientHandler.processInput(clientInput));
//
//    }
//
//    @Test
//    void processClientInput_nullInputThrowsException() {
//        assertThrows(NumberServerException.class, () -> clientHandler.processInput(null));
//    }
//
//    @Test
//    void processClientInput_terminate() throws IOException {
//        Application applicationMock = Mockito.mock(Application.class);
//        SocketHandler ch = new SocketHandler(applicationMock);
//        ch.processInput("terminate");
//        Mockito.verify(applicationMock).terminateApplication();
//    }
//
//    @Test
//    void processClientInput_correctInput() throws IOException {
//        try (MockedStatic<StoringTask> mockedStatic = Mockito.mockStatic(StoringTask.class)) {
//            clientHandler.processInput("123456789");
//            mockedStatic.verify(() -> StoringTask.processNumber(123456789));
//        }
//    }

}