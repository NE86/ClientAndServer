package clientServer.client.thread;

import clientServer.client.gui.ClientFrame;
import clientServer.message.Message;
import clientServer.message.MessageQueue;

public class OutputTextAreaThread extends Thread {
    private MessageQueue messageQueue;
    String name;

    public OutputTextAreaThread(MessageQueue messageQueueOutput, String name) {
        this.messageQueue = messageQueueOutput;
        this.name = new String(name);
        this.start();
    }

    public void run() {
        while (true) {
            Message message = messageQueue.get();
            if (message.getName().equals("server")) {
                ClientFrame.panel.textArea.append("[" + name + "]: " + message.getText() + "\n");
                ClientFrame.panel.textArea.setCaretPosition(ClientFrame.panel.textArea.getDocument().getLength());
            } else {
                ClientFrame.panel.textArea.append(message.getText() + "\n");
                ClientFrame.panel.textArea.setCaretPosition(ClientFrame.panel.textArea.getDocument().getLength());
            }
        }
    }
}
