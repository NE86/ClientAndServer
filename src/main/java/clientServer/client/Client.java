package clientServer.client;

import clientServer.client.gui.ClientFrame;
import clientServer.client.thread.InputServerTread;
import clientServer.client.thread.OutputServerTread;
import clientServer.client.thread.OutputTextAreaThread;
import clientServer.message.Message;
import clientServer.message.MessageQueue;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class Client {
    private static Socket localServerSocket;

    public static Socket getLocalServerSocket() {
        return localServerSocket;
    }
    private static String generateCreateName() {
        final String[] firstName = {"Охотник", "Дима", "Ваня", "Аноним", "Игорь", "Сыщик"};
        final String[] lastName = {"Вездесущий", "Простой", "Опасный", "Быстрый", "Короткий", "Тут"};
        return lastName[(int) (Math.random() * 6)] + firstName[(int) (Math.random() * 6)];
    }

    private static Socket connectServer(InetAddress ipAddress, int serverPort, String address, MessageQueue messageQueueAllText) {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket(ipAddress, serverPort);
                messageQueueAllText.add(new Message(
                        "Соединение с сервером установлено. Адрес соединения " + address + " номер порта " + serverPort, "client"));
            } catch (IOException e) {
                messageQueueAllText.add(new Message("Попытка соединение с сервером...", "client"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return socket;
    }

    public static void main(String[] ar) {
        if (ar.length == 0) {
            System.out.println("Введите параметры запуска address port");
            return;
        }
        String address = ar[0];
        int serverPort = Integer.parseInt(ar[1]);
        String name = new String(generateCreateName());

        MessageQueue messageQueueAllText = new MessageQueue();
        MessageQueue messageQueueOutput = new MessageQueue();

        ClientFrame frame = new ClientFrame(messageQueueOutput, messageQueueAllText);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new OutputTextAreaThread(messageQueueAllText, name);

        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Socket socket = connectServer(ipAddress, serverPort, address, messageQueueAllText);
        localServerSocket = socket;

        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            messageQueueAllText.add(new Message("Ваше имя " + name, "client"));
            messageQueueOutput.add(new Message(name, "client"));

            new InputServerTread(dataInputStream, messageQueueAllText);
            new OutputServerTread(dataOutputStream, messageQueueOutput, messageQueueAllText);

            messageQueueAllText.add(new Message("Для отправки сообщения нажмите enter или нажмите на кнопку 'Отправить'", "client"));
        } catch (IOException e) {
            messageQueueAllText.add(new Message("Сервер был отключен во время подключения", "client"));
        }
    }
}