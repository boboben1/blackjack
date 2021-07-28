package me.brecher.blackjack.server;

import com.google.common.eventbus.AsyncEventBus;
import me.brecher.blackjack.server.ServerToClientEventQueue;
import me.brecher.blackjack.shared.events.BetResetEvent;
import me.brecher.blackjack.shared.events.StartRoundEvent;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Executors;

public class ServerConnectionThread extends Thread {
    private final Socket socket;
    private final AsyncEventBus eventBus;
    private final ServerToClientEventQueue serverToClientEventQueue;

    public ServerConnectionThread(Socket socket, AsyncEventBus eventBus, ServerToClientEventQueue serverToClientEventQueue) {
        this.socket = socket;
        this.eventBus = eventBus;
        this.serverToClientEventQueue = serverToClientEventQueue;
    }

    @Override
    public void run() {
        try (
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ) {
            eventBus.post(new BetResetEvent());

            new Thread(() -> {
                while (true) {
                    try {
                        Object obj = ois.readObject();
                        System.out.println(obj);
                        this.eventBus.post(obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }


            }).start();


            while (true) {

                if (serverToClientEventQueue.hasNext())
                {
                    try {
                        oos.writeObject(serverToClientEventQueue.poll());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
         catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
}
