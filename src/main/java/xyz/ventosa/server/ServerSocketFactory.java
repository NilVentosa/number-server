package xyz.ventosa.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketFactory {

    public ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

}
