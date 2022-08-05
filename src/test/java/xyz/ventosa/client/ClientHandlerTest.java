package xyz.ventosa.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {
    ServerSocket serverSocketMock;

    @BeforeEach
    void setup() {
        serverSocketMock = Mockito.mock(ServerSocket.class);
    }

    @Test
    void acceptNewClient() throws IOException {
        ClientHandler clientHandler = new ClientHandler(serverSocketMock);
        Mockito.when(serverSocketMock.accept()).thenReturn(new Socket());
        clientHandler.acceptNewClient();

        Mockito.verify(serverSocketMock).accept();
    }

    @Test
    void isAcceptingNewClients() {
        ClientHandler clientHandler = new ClientHandler(serverSocketMock);

        assertFalse(clientHandler.isAcceptingNewClients(0));
        assertTrue(clientHandler.isAcceptingNewClients(1));
    }

}