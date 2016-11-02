package clientServer.client.thread;

import clientServer.client.Client;
import clientServer.client.gui.ClientFrame;
import clientServer.message.Message;
import clientServer.message.MessageQueue;
import clientServer.message.Ping;

import java.io.DataInputStream;
import java.io.IOException;


public class InputServerTread extends Thread {
    private DataInputStream in;
    private MessageQueue messageQueueInput;

    public InputServerTread(DataInputStream in, MessageQueue messageQueueInput) {
        this.in = in;
        this.messageQueueInput = messageQueueInput;
        this.start();
    }

    public void run() {
        String line = null;
        while (true)
            try {
                line = in.readUTF();
                messageQueueInput.add(new Message(line, "client"));
            } catch (IOException e) {
                messageQueueInput.add(new Message("Не удалось получить сообщение от сервера", "server"));
                if (!Ping.send(Client.getLocalServerSocket())) {
                    messageQueueInput.add(new Message("Связь с сервером потеряна", "server"));
                    ClientFrame.panel.textField.setEditable(false);//отключение поля ввода
                    ClientFrame.panel.button.setEnabled(false);//отключение возможности нажатия кнопки
                    break;
                } else
                    messageQueueInput.add(new Message("Связь с сервером присутствует, но отправка сообщения недоступна", "server"));
            }
    }
}