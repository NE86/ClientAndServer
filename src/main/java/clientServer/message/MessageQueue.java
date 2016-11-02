package clientServer.message;

import java.util.PriorityQueue;
import java.util.Queue;

public class MessageQueue {
    private Queue<Message> queue = new PriorityQueue<>();

    public synchronized void add(Message message) {
        queue.offer(message);
        notify();
    }

    public synchronized Message get() {
        try {
            while (queue.peek() == null) wait();
        } catch (InterruptedException e) {
            return null;
        }
        return queue.poll();
    }
}
