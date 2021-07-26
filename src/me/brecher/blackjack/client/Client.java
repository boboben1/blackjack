package me.brecher.blackjack.client;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.client.gui.GameForm;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private final int port;
    private final AsyncEventBus asyncEventBus;
    private final ClientToServerEventQueue clientToServerEventQueue;
    private final GameForm gameForm;


    private Socket socket;
    private InputStream is;
    private ObjectOutputStream oos;


    @Inject
    public Client(AsyncEventBus asyncEventBus, ClientToServerEventQueue clientToServerEventQueue, GameForm gameForm, @Assisted int port) {
        this.port = port;
        this.asyncEventBus = asyncEventBus;
        this.clientToServerEventQueue = clientToServerEventQueue;
        this.gameForm = gameForm;
    }


    @Override
    public void run() {

        try {
            socket = new Socket(InetAddress.getLoopbackAddress(), port);


            is = socket.getInputStream();


            oos = new ObjectOutputStream(socket.getOutputStream());

            startUI();


            new Thread(() -> {

                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Object obj = ois.readObject();
                        this.asyncEventBus.post(obj);

                        System.out.println(obj);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (true) {
                if (clientToServerEventQueue.hasNext())
                {
                    oos.writeObject(clientToServerEventQueue.poll());
                }

            }

        } catch (IOException e) {
            return;
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
