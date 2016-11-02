package clientServer.message;

public class SystemMessage extends Message {
    private String systemWord;

    private void searchSystemWord(String text) {
        systemWord = new String();
        if (text.startsWith("bye")) systemWord = "bye";
        if (text.startsWith("list")) systemWord = "list";
        if (text.startsWith("Ping")) systemWord = "Ping";
    }

    public SystemMessage(Message message) {
        super(message.getText(), message.getName());
        searchSystemWord(this.getText());
    }

    public String toString() {
        return this.getText().substring(systemWord.length());
    }

    public boolean checkSystemMessage() {
        if (this.systemWord.equals("bye")) return true;
        else if (this.systemWord.equals("list")) return true;
        else if (this.systemWord.equals("Ping")) return true;
        else return false;

    }

    public String getSystemWord() {
        return systemWord;
    }
}