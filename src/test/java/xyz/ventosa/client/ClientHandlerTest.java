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
import xyz.ventosa.server.Server;
import xyz.ventosa.task.StoringTask;
import xyz.ventosa.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {

    Server server;

    Server serverMock;

    ServerSocket serverSocketMock;

    StoringTask storingTask;

    Socket socketMock;

    @BeforeEach
    void setup() {
        server = new Server(Integer.parseInt(Constants.DEFAULT_PORT));
        serverMock = Mockito.mock(Server.class);
        serverSocketMock = Mockito.mock(ServerSocket.class);
        storingTask = Mockito.mock(StoringTask.class);
        socketMock = Mockito.mock(Socket.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.getServerSocket().close();
    }

    @Test
    void isAcceptingNewClients_true() {
        ClientHandler clientHandler = new ClientHandler(server);
        assertTrue(clientHandler.isAcceptingNewClients(1));
    }

    @Test
    void isAcceptingNewClients_false() {
        ClientHandler clientHandler = new ClientHandler(server);
        assertFalse(clientHandler.isAcceptingNewClients(0));
    }

    @Test
    void terminateAllClients_callsRemoveFromActiveClients() throws IOException {

        ClientHandler clientHandler = new ClientHandler(serverMock);
        Socket socketMock = Mockito.mock(Socket.class);
        ClientHandler clientHandlerSpy = Mockito.spy(clientHandler);

        Mockito.doNothing().when(storingTask).run();
        Mockito.when(serverMock.getServerSocket()).thenReturn(serverSocketMock);
        Mockito.when(serverSocketMock.accept()).thenReturn(socketMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("123456789".getBytes()));

        clientHandlerSpy.handleNewClient();
        clientHandlerSpy.handleNewClient();
        clientHandlerSpy.terminateAllClients();

        Mockito.verify(clientHandlerSpy, Mockito.atLeast(1)).removeFromActiveClients(1);
        Mockito.verify(clientHandlerSpy, Mockito.atLeast(1)).removeFromActiveClients(2);

    }

    @ParameterizedTest
    @ValueSource(strings = { "9999999999", "", "1", "carbonara", "d", "TERminate", "terminates" })
    void processClientInput_invalidInputsThrowException(String clientInput) {
        ClientHandler clientHandler = new ClientHandler(server);
        assertThrows(NumberServerException.class, () -> clientHandler.processClientInput(clientInput));

    }

    @Test
    void processClientInput_nullInputThrowsException() {
        ClientHandler clientHandler = new ClientHandler(server);
        assertThrows(NumberServerException.class, () -> clientHandler.processClientInput(null));
    }

    @Test
    void processClientInput_terminate() throws IOException {
        ClientHandler clientHandler = new ClientHandler(server);

        try (MockedStatic<Application> mockedStatic = Mockito.mockStatic(Application.class)) {
            clientHandler.processClientInput("terminate");
            mockedStatic.verify(Application::terminateApplication);
        }
    }

    @Test
    void processClientInput_correctInput() throws IOException {
        ClientHandler clientHandler = new ClientHandler(server);

        try (MockedStatic<StoringTask> mockedStatic = Mockito.mockStatic(StoringTask.class)) {
            clientHandler.processClientInput("123456789");
            mockedStatic.verify(() -> StoringTask.processNumber("123456789"));
        }
    }

}