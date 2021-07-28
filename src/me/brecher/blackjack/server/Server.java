package me.brecher.blackjack.server;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.server.ServerToClientEventQueue;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.server.handmanager.HandManager;
import me.brecher.blackjack.server.handmanager.HandManagerFactory;
import me.brecher.blackjack.server.scoring.BetManager;
import me.brecher.blackjack.server.scoring.ScoreKeeper;
import me.brecher.blackjack.shared.gameplay.Gameplay;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

public class Server {

    final HandManager playerHandManager;
    final HandManager dealerHandManager;
    @Inject
    DeckManager deckManager;
    @Inject
    Gameplay gameplay;


    @Inject
    private final AsyncEventBus eventBus;

    @Inject
    BetManager betManager;

    @Inject
    ScoreKeeper scoreKeeper;

    @Inject
    ServerToClientEventQueue serverToClientEventQueue;

    @Inject
    ServerEventRouter serverEventRouter;

    private ServerSocket serverSocket;

    private final ScheduledExecutorService scheduledExecutorService;

    @Inject
    public Server(HandManagerFactory handManagerFactory, AsyncEventBus eventBus, ScheduledExecutorService scheduledExecutorService) {
        this.playerHandManager = handManagerFactory.create(1);
        this.dealerHandManager = handManagerFactory.create(0);
        this.eventBus = eventBus;
        this.scheduledExecutorService = scheduledExecutorService;
    }



    public HandManager getPlayerHandManager() {
        return playerHandManager;
    }

    public HandManager getDealerHandManager() {
        return dealerHandManager;
    }

    public int start() {
        try {
            serverSocket = new ServerSocket(0, 0, InetAddress.getLoopbackAddress());
        } catch (IOException e) {
            e.printStackTrace();

            return -1;
        }

        scheduledExecutorService.execute(() -> {
            while (!Thread.currentThread().isInterrupted()) {

                try {
                    Socket socket = this.serverSocket.accept();

                    System.out.println(socket.getLocalPort());

                    new ServerConnectionThread(socket, eventBus, serverToClientEventQueue, scheduledExecutorService).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return serverSocket.getLocalPort();
    }
}
