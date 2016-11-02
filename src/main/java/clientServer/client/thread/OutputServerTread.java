package clientServer.client.thread;

import clientServer.client.Client;
import clientServer.client.gui.ClientFrame;
import clientServer.message.Message;
import clientServer.message.MessageQueue;
import clientServer.message.Ping;

import java.io.DataOutputStream;
import java.io.IOException;

public class OutputServerTread extends Thread {
    private DataOutputStream dataOutputStreamt;
    private MessageQueue messageQueueOutput;
    private MessageQueue messageQueueAllText;

    public OutputServerTread(DataOutputStream out, MessageQueue messageQueueOutput, MessageQueue messageQueueAllText) {
        this.dataOutputStreamt = out;
        this.messageQueueOutput = messageQueueOutput;
        this.messageQueueAllText = messageQueueAllText;
        this.start();
    }

    public void run() {
        String line = null;
        while (true)
            try {
                line = messageQueueOutput.get().getText();
                dataOutputStreamt.writeUTF(line);
                dataOutputStreamt.flush();
            } catch (IOException e) {
                messageQueueAllText.add(new Message("Не удалось отправить сообщение на сервер", "server"));
                if (!Ping.send(Client.getLocalServerSocket())) {
                    messageQueueAllText.add(new Message("Связь с сервером потеряна", "server"));
                    ClientFrame.panel.textField.setEditable(false);//отключение поля ввода
                    ClientFrame.panel.button.setEnabled(false);//отключение возможности нажатия кнопки
                    break;
                } else
                    messageQueueAllText.add(new Message("Связь с сервером присутствует, но возможность получения сообщений недоступна", "server"));

            }
    }
}