package xyz.ventosa.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;

class ServerTest {

    int port = 3000;

    ServerSocketFactory serverSocketFactoryMock;
    ServerSocket serverSocketMock;
    Server server;


    @BeforeEach
    void setup() {
        serverSocketFactoryMock = Mockito.mock(ServerSocketFactory.class);
        serverSocketMock = Mockito.mock(ServerSocket.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        server.getServerSocket().close();
    }

    @Test
    void terminateServer_closesServerSocket() throws IOException {
        Mockito.when(serverSocketFactoryMock.createServerSocket(port)).thenReturn(serverSocketMock);
        server = new Server(port, serverSocketFactoryMock);
        server.terminateServer();
        Mockito.verify(serverSocketMock).close();
    }

    @Test
    void isServerSocketOpen_true() {
        server = new Server(port, new ServerSocketFactory());
        Assertions.assertTrue(server.isServerSocketOpen());
    }

    @Test
    void isServerSocketOpen_false() {
        server = new Server(port, new ServerSocketFactory());
        server.terminateServer();
        Assertions.assertFalse(server.isServerSocketOpen());
    }
}