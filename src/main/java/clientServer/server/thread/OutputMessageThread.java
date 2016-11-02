package clientServer.server.thread;


import clientServer.message.Message;
import clientServer.message.MessageQueue;
import clientServer.message.SystemMessage;
import clientServer.server.SocketClientList;
import clientServer.server.databace.ServerDatabase;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OutputMessageThread extends Thread {
    private clientServer.message.MessageQueue MessageQueue;

    public OutputMessageThread(MessageQueue MessageQueue) {
        this.MessageQueue = MessageQueue;
        this.start();
    }

    private void outputSystemMessage(SystemMessage systemMessage) {
        System.out.println("Клиентом " + SocketClientList.get(systemMessage.getName()).getName()
                + " был отправлен запрос '" + systemMessage.getSystemWord() + "'");
        if (systemMessage.getSystemWord().equals("bye")) {
            try {
                ServerDatabase.updateClientOfflineDB(systemMessage.getName());
            } catch (SQLException e) {
                System.out.println("Не удалось удалить клиента из баззы данных");
            }
            String text = systemMessage.toString();
            String name = systemMessage.getName();
            MessageQueue.add(new Message(text, name));
            System.out.println("Клиент " + name + " получил статус offline");
            SocketClientList.remove(systemMessage.getName());
        } else if (systemMessage.getSystemWord().equals("list")) {
            try {
                DataOutputStream out = new DataOutputStream(
                        SocketClientList.get(systemMessage.getName()).getSocket().getOutputStream());
                out.writeUTF("Список клиентов имеющие статус online:");
                out.flush();
                try {
                    ResultSet rs = ServerDatabase.onlineListClientDB();
                    while (rs.next()) {
                        String name = rs.getString("client_name");
                        out.writeUTF("---" + SocketClientList.get(name).getName() + "---");
                        out.flush();
                    }
                    if (rs != null) rs.close();
                } catch (SQLException e) {
                    System.out.println("Во время отправки запроса onlineListClientDB возникла ошибка");
                }
                System.out.println("Клиент " + SocketClientList.get(systemMessage.getName()).getName()
                        + " получил ответ на запрос 'list'");
            } catch (IOException e) {
                System.out.println("Клиенту " + SocketClientList.get(systemMessage.getName()).getName()
                        + " не получил ответ на запрос 'list', из за ошибки");
            }
        } else if (systemMessage.getSystemWord().equals("Ping")) {
            try {
                DataOutputStream out = new DataOutputStream(
                        SocketClientList.get(systemMessage.getName()).getSocket().getOutputStream());
                out.writeUTF("Ping");
                System.out.println("Клиент " + SocketClientList.get(systemMessage.getName()).getName()
                        + " получил ответ на запрос 'Ping'");
            } catch (IOException e) {
                System.out.println("Клиент " + SocketClientList.get(systemMessage.getName()).getName()
                        + " не получил ответ на запрос 'Ping', из за ошибки");
            }
        }
    }

    public void run() {
        while (true)
            try {
                Message message = MessageQueue.get();//получать онлайн спсок всех людей
                if (new SystemMessage(message).checkSystemMessage()) outputSystemMessage(new SystemMessage(message));
                else
                    try {
                        ResultSet rs = ServerDatabase.onlineListClientDB();
                        while (rs.next()) {
                            String name = rs.getString("client_name");
                            if (!name.equals(message.getName())) {
                                DataOutputStream out = new DataOutputStream(SocketClientList.get(name).getSocket().getOutputStream());
                                out.writeUTF("[" + message.getName() + "]: " + message.getText());
                                out.flush();
                            }
                        }
                        if (rs != null) rs.close();
                    } catch (SQLException e) {
                        System.out.println("Во время отправки запроса onlineListClientDB возникла ошибка");
                    }
            } catch (IOException e) {
                System.out.println("Не удалось отправить сообщение одному из клиентов");
            }
    }
}