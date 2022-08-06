package xyz.ventosa.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xyz.ventosa.server.Server;
import xyz.ventosa.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

class ClientTest {
    Socket socketMock;
    Server server;
    ClientHandler clientHandler;
    ClientHandler clientHandlerSpy;
    Client client;

    int clientId = 1;

    @BeforeEach
    void setup() {
        server = new Server(Integer.parseInt(Constants.DEFAULT_PORT));
        socketMock = Mockito.mock(Socket.class);
        clientHandler = new ClientHandler(server);
        clientHandlerSpy = Mockito.spy(clientHandler);
        client = new Client(socketMock, clientId, clientHandlerSpy);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.getServerSocket().close();
    }

    @Test
    void run_callsProcessClientInput() throws IOException {
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("88888888".getBytes()));
        client.run();
        Mockito.verify(clientHandlerSpy).processClientInput(Mockito.any());
    }

    @Test
    void terminateClient_callsSocketClose() throws IOException {
        client.terminateClient();
        Mockito.verify(socketMock).close();
    }

    @Test
    void terminateClient_callsRemoveFromActiveClients() {
        client.terminateClient();
        Mockito.verify(clientHandlerSpy).removeFromActiveClients(clientId);
    }

    @Test
    void getClientId_returnsCorrectId() {
        client.terminateClient();
        Assertions.assertEquals(clientId, client.getClientId());
    }
}