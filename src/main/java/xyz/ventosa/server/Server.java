package xyz.ventosa.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

@Log4j2
@NoArgsConstructor
public class Server {

    @Getter
    private ServerSocket serverSocket;

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            log.info("Server listening on port: {}.", serverSocket.getLocalPort());
        }
        catch (IOException e) {
            log.error("Problem starting server: {}.", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void terminateServer() {
        log.info("Terminating server");
        try {
            serverSocket.close();
        }
        catch (SocketException e) {
            log.debug("Socket exception: {}.", e.getMessage());
        }
        catch (IOException e) {
            log.error("Exception in terminate: {}.", e.getMessage());
            System.exit(1);
        }
    }

    public boolean isServerSocketOpen() {
        if (serverSocket != null) {
            return !serverSocket.isClosed();
        }
        return false;
    }

}
