package me.brecher.blackjack.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class ClientToServerEventQueue {

    private Queue<Serializable> eventQueue;

    public ClientToServerEventQueue() {
        this.eventQueue = new LinkedList<>();
    }

    public synchronized void sendToServer(Serializable serializable) {
        this.eventQueue.add(serializable);
    }

    public synchronized boolean hasNext() {
        return !this.eventQueue.isEmpty();
    }

    public synchronized Serializable poll() {
        return this.eventQueue.poll();
    }
}
