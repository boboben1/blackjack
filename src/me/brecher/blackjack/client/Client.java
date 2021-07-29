package me.brecher.blackjack.client;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.client.gui.GameForm;
import me.brecher.blackjack.shared.events.ClientAckEvent;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Client extends Thread {
    private final int port;
    private final AsyncEventBus asyncEventBus;
    private final ClientToServerEventQueue clientToServerEventQueue;
    private final GameForm gameForm;
    private final ScheduledExecutorService scheduledExecutorService;



    @Inject
    public Client(AsyncEventBus asyncEventBus, ClientToServerEventQueue clientToServerEventQueue, GameForm gameForm,
                  ScheduledExecutorService scheduledExecutorService, @Assisted int port) {
        this.port = port;
        this.asyncEventBus = asyncEventBus;
        this.clientToServerEventQueue = clientToServerEventQueue;
        this.gameForm = gameForm;
        this.scheduledExecutorService = scheduledExecutorService;
    }


    @Override
    public void run() {
        try (Socket socket = new Socket(InetAddress.getLoopbackAddress(), port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()))
        {
            startUI();

            clientToServerEventQueue.sendToServer(new ClientAckEvent());

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                try {
                    Object obj = ois.readObject();
                    this.asyncEventBus.post(obj);

                    System.out.println(obj);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }, 0, 100, TimeUnit.MILLISECONDS);


            scheduledExecutorService.scheduleAtFixedRate(() -> {
                while (clientToServerEventQueue.hasNext()) {
                    try {
                        oos.writeObject(clientToServerEventQueue.poll());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },0, 100, TimeUnit.MILLISECONDS);

            try {
                while (!scheduledExecutorService.awaitTermination(5000, TimeUnit.MILLISECONDS)
                        && !Thread.currentThread().isInterrupted()) {
                }
            } catch (InterruptedException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void startUI() {
        JFrame frame = new JFrame("Game");
        this.gameForm.addToFrame(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
