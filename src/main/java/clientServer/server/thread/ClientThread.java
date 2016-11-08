package clientServer.server.thread;

import clientServer.message.Message;
import clientServer.message.MessageQueue;
import clientServer.message.Ping;
import clientServer.message.SystemMessage;
import clientServer.server.SocketClient;
import clientServer.server.SocketClientList;

import java.io.DataInputStream;
import java.io.IOException;

public class ClientThread extends Thread {
    private clientServer.message.MessageQueue MessageQueue;
    private boolean threadLife = true;
    SocketClient workingSocketClient;

    public ClientThread(MessageQueue MessageQueue) {
        this.MessageQueue = MessageQueue;
        workingSocketClient = SocketClientList.get(SocketClientList.size() - 1);
        this.start();
    }

    private void offlineClient(SystemMessage systemMessage) {
        if (systemMessage.getSystemWord().equals("bye")) {
            MessageQueue.add((Message) systemMessage);
            threadLife = false;
        } else {
            MessageQueue.add((Message) systemMessage);
        }
    }

    public void run() {
        try {
            DataInputStream in = new DataInputStream(workingSocketClient.getSocket().getInputStream());
            while (threadLife) {
                String text = new String(in.readUTF());
                System.out.println("Полученно сообщение '" + text + "' отправитель: " + workingSocketClient.getName());
                SystemMessage message = new SystemMessage(new Message(text, workingSocketClient.getName()));
                if (message.checkSystemMessage()) offlineClient(message);
                else MessageQueue.add((Message) message);
            }
        } catch (IOException e) {
            System.out.println("Не удалось получить сообщение от клиента " + workingSocketClient.getName());
            System.out.println("Клиенту " + workingSocketClient.getName() + " был отправлен запрос 'Ping'");
            if (!Ping.send(workingSocketClient.getSocket())) {
                String text = "bye " + " Ушел в offline";
                MessageQueue.add(new Message(text, workingSocketClient.getName()));
                System.out.println("Связь с клиентом " + workingSocketClient.getName()
                        + " отсутствует, был отправлен запрос на отключение клиента ");
            } else System.out.println("Связь с клиентом " + workingSocketClient.getName()
                    + " присутствует, но возможность получения сообщений недоступна");
        }
        try {
            workingSocketClient.getSocket().close();
        } catch (IOException e) {
            System.out.println("Не удалось закрыть сокет клиента " + workingSocketClient.getName());
        }
    }
}
