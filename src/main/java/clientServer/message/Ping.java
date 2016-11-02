package clientServer.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Ping {
    public static synchronized boolean send(Socket socket) {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return false;
        }
        String line = new String();
        try {
            out.writeUTF("Ping");
            out.flush();
            line = in.readUTF();
        } catch (IOException e) {
            return false;
        }
        if (line.equals("Ping")) return true;
        else return false;
    }
}