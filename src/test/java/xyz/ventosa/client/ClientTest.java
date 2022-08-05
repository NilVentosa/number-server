package xyz.ventosa.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    Socket socketMock;
    ClientHandler clientHandlerMock;

    @BeforeEach
    void setup() {
        socketMock = Mockito.mock(Socket.class);
        clientHandlerMock = Mockito.mock(ClientHandler.class);
    }

    @Test
    void run_shortInput() throws IOException {
        Client client = new Client(socketMock, clientHandlerMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("9".getBytes()));
        client.run();
        Mockito.verify(socketMock).close();
    }

    @Test
    void run_longInput() throws IOException {
        Client client = new Client(socketMock, clientHandlerMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("9999999999".getBytes()));
        client.run();
        Mockito.verify(socketMock).close();
    }

    @Test
    void run_emptyInput() throws IOException {
        Client client = new Client(socketMock, clientHandlerMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{}));
        client.run();
        Mockito.verify(socketMock).close();
    }

    @Test
    void run_terminate() throws IOException {
        Client client = new Client(socketMock, clientHandlerMock);
        Mockito.when(socketMock.getInputStream()).thenReturn(new ByteArrayInputStream("terminate".getBytes()));
        client.run();
        Mockito.verify(clientHandlerMock).terminateApplication();
    }

    @Test
    void terminateClient() throws IOException {
        Client client = new Client(socketMock, clientHandlerMock);
        client.terminateClient();
        Mockito.verify(socketMock).close();
        Mockito.verify(clientHandlerMock).removeFromActiveClients(client.getClientId());
    }

    @Test
    void getClientId_incrementsWithInstances() {
        Client client1 = new Client(socketMock, clientHandlerMock);

        Client client2 = new Client(socketMock, clientHandlerMock);
        assertEquals(client1.getClientId()+1, client2.getClientId());

        Client client3 = new Client(socketMock, clientHandlerMock);
        assertEquals(client1.getClientId()+2, client3.getClientId());

    }
}