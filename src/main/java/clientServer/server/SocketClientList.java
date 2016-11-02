package clientServer.server;

import java.util.ArrayList;
import java.util.List;

public class SocketClientList {
    private static List<SocketClient> connectedSocketClients = new ArrayList<SocketClient>();

    public static synchronized void add(SocketClient socketClient) {
        connectedSocketClients.add(socketClient);
    }

    private static synchronized int searchSocketClient(String name) {
        int i = 0;
        while (i < connectedSocketClients.size()) {
            if (connectedSocketClients.get(i).getName().equals(name)) return i;
            i++;
        }
        return -1;
    }

    public static synchronized SocketClient get(String name) {
        int i = searchSocketClient(name);
        if (i != -1) return connectedSocketClients.get(i);
        return null;
    }

    public static synchronized SocketClient get(int index) {
        return connectedSocketClients.get(index);
    }

    public static synchronized int size() {
        return connectedSocketClients.size();
    }

    public static synchronized void remove(String name) {
        connectedSocketClients.remove(searchSocketClient(name));
    }
}
