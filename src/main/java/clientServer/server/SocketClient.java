package clientServer.server;

import java.io.DataInputStream;
import java.net.Socket;


public class SocketClient {
    private Socket socket;
    private String name;

    public SocketClient(Socket socket, DataInputStream in, String name) {
        this.socket = socket;
        this.name = name;
    }

    public synchronized Socket getSocket() {
        return socket;
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized void setName(String name) {
        this.name = name;
    }

    public synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }
}