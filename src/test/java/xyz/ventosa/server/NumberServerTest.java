package xyz.ventosa.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;

class NumberServerTest {

    int port = 3000;

    ServerSocketFactory serverSocketFactoryMock;
    ServerSocket serverSocketMock;
    NumberServer numberServer;


    @BeforeEach
    void setup() {
        serverSocketFactoryMock = Mockito.mock(ServerSocketFactory.class);
        serverSocketMock = Mockito.mock(ServerSocket.class);
    }

    @AfterEach
    void tearDown() throws IOException {
        numberServer.getServerSocket().close();
    }

    @Test
    void terminateServer_closesServerSocket() throws IOException {
        Mockito.when(serverSocketFactoryMock.createServerSocket(port)).thenReturn(serverSocketMock);
        numberServer = new NumberServer(port, serverSocketFactoryMock);
        numberServer.terminateServer();
        Mockito.verify(serverSocketMock).close();
    }

    @Test
    void isServerSocketOpen_true() {
        numberServer = new NumberServer(port, new ServerSocketFactory());
        Assertions.assertTrue(numberServer.isServerSocketOpen());
    }

    @Test
    void isServerSocketOpen_false() {
        numberServer = new NumberServer(port, new ServerSocketFactory());
        numberServer.terminateServer();
        Assertions.assertFalse(numberServer.isServerSocketOpen());
    }
}