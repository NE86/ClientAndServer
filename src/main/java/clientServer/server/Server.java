package clientServer.server;

import clientServer.message.MessageQueue;
import clientServer.server.databace.ServerDatabase;
import clientServer.server.thread.ClientThread;
import clientServer.server.thread.OutputMessageThread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    public static void main(String[] ar) {
        int port = 8888;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Сервер уже запущен");
            System.exit(0);
        }
        System.out.println("Сервер успешно запущен");
        new ServerDatabase();
        new SocketClientList();
        MessageQueue MessageQueue = new MessageQueue();
        new OutputMessageThread(MessageQueue);
        startListenToLoginPackets(serverSocket, MessageQueue);
    }

    private static void startListenToLoginPackets(ServerSocket serverSocket, MessageQueue MessageQueue) {
        while (true)
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                String name = dataInputStream.readUTF();

                switch (ServerDatabase.checkClientOnlineDB(name)) {
                    case -1: {
                        SocketClientList.add(new SocketClient(socket, dataInputStream, name));
                        new ClientThread(MessageQueue);
                        ServerDatabase.addClientDB(name);
                        System.out.println("Клиент " + name + " получил статус online");
                        break;
                    }
                    case 0: {
                        SocketClientList.add(new SocketClient(socket, dataInputStream, name));
                        new ClientThread(MessageQueue);
                        ServerDatabase.updateClientOnlineDB(name);
                        System.out.println("Клиент " + name + " вновь получил статус online");
                        break;
                    }
                    case 1: {
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("server : Клиент с таким ником уже есть на сервере");
                        dataOutputStream.flush();
                        dataOutputStream.close();
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Во время подключения клиента произошла ошибка");
                ServerDatabase.closeServerDB();
            } catch (NullPointerException e) {
                System.out.println("Сервер уже запущен");
                ServerDatabase.closeServerDB();
                break;
            } catch (SQLException e) {
                System.out.println("Не удалось отправить запрос базе данных");
                ServerDatabase.closeServerDB();
            }
    }
}