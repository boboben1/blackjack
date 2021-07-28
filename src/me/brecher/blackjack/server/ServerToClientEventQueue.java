package me.brecher.blackjack.server;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class ServerToClientEventQueue {

    private final Queue<Serializable> eventQueue;

    public ServerToClientEventQueue() {
        this.eventQueue = new LinkedList<>();
    }

    public synchronized void sendToClient(Serializable serializable) {
        this.eventQueue.add(serializable);
    }

    public synchronized boolean hasNext() {
        return !this.eventQueue.isEmpty();
    }

    public synchronized Serializable poll() {
        return this.eventQueue.poll();
    }
}
