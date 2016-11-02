package clientServer.client.gui;

import clientServer.message.MessageQueue;

import javax.swing.*;

public class ClientFrame extends JFrame {
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHE = 400;
    public static ButtonPanel panel;

    public ClientFrame(MessageQueue messageQueueOutput, MessageQueue messageQueueAllText) {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHE);
        setTitle("client");
        panel = new ButtonPanel(messageQueueOutput, messageQueueAllText);
        add(panel);
    }
}
