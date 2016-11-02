package clientServer.message;

import java.util.Date;


public class Message implements Comparable<Message> {
    private String text;
    private String name;
    private Date messageDate;
    private Long messageMillisecond;

    public Message(String text, String name) {
        this.text = new String(text);
        this.name = name;
        messageDate = new Date();
        messageMillisecond = System.currentTimeMillis();
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    private Date getMessageDate() {
        return messageDate;
    }

    private Long getMessageMillisecond() {
        return messageMillisecond;
    }

    public String toString() {
        return "[" + name + "]: " + text;
    }

    public int compareTo(Message o) {
        int testDate = messageDate.compareTo(o.getMessageDate());
        if (testDate == 0) return messageMillisecond.compareTo(o.getMessageMillisecond());
        else return testDate;
    }
}