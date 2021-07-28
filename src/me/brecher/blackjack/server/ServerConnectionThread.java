package me.brecher.blackjack.server;

import com.google.common.eventbus.AsyncEventBus;
import me.brecher.blackjack.server.ServerToClientEventQueue;
import me.brecher.blackjack.shared.events.BetResetEvent;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerConnectionThread extends Thread {
    private final Socket socket;
    private final AsyncEventBus eventBus;
    private final ServerToClientEventQueue serverToClientEventQueue;
    private final ScheduledExecutorService scheduledExecutorService;

    public ServerConnectionThread(Socket socket, AsyncEventBus eventBus, ServerToClientEventQueue serverToClientEventQueue, ScheduledExecutorService scheduledExecutorService) {
        this.socket = socket;
        this.eventBus = eventBus;
        this.serverToClientEventQueue = serverToClientEventQueue;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public void run() {
        try (
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())
        ) {
            eventBus.post(new BetResetEvent());

            scheduledExecutorService.execute(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Object obj = ois.readObject();
                        //System.out.println(obj);
                        this.eventBus.post(obj);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                while (serverToClientEventQueue.hasNext()) {
                    try {
                        oos.writeObject(serverToClientEventQueue.poll());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS);

            try {
                while(!scheduledExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS));
            } catch (InterruptedException ignored) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
