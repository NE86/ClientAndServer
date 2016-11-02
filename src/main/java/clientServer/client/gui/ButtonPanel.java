package clientServer.client.gui;

import clientServer.message.Message;
import clientServer.message.MessageQueue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ButtonPanel extends JPanel {
    public JTextArea textArea = new JTextArea(10, 30);
    public JTextField textField = new JTextField("Всем привет", 20);
    public JButton button = new JButton("Отправить");

    public ButtonPanel(final MessageQueue messageQueueOutput, final MessageQueue messageQueueAllText) {
        add(textArea);
        add(new JScrollPane(textArea));
        add(textField);
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    messageQueueAllText.add(new Message(textField.getText(), "server"));
                    messageQueueOutput.add(new Message(textField.getText(), "server"));
                    textField.setText("");
                }
            }
        });
        add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                messageQueueAllText.add(new Message(textField.getText(), "server"));
                messageQueueOutput.add(new Message(textField.getText(), "server"));
                textField.setText("");
            }
        });
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
    }
}
