package me.brecher.blackjack.server.gameplay;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.server.ServerToClientEventQueue;
import me.brecher.blackjack.shared.events.ClientAckEvent;

public class ClientSync {

    private final ServerToClientEventQueue serverToClientEventQueue;

    private boolean isClientSynced;

    @Inject
    ClientSync(ServerToClientEventQueue serverToClientEventQueue, AsyncEventBus eventBus) {
        this.serverToClientEventQueue = serverToClientEventQueue;
        isClientSynced = false;

        eventBus.register(this);
    }


    public synchronized void syncClient() {
        while (!isClientSynced) {
            try {
                wait();
            } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                System.err.println("ClientSync::syncClient ERROR: Interrupted");
            }
        }

        isClientSynced = false;
    }

    @Subscribe
    public synchronized void clientAck(ClientAckEvent event) {
        isClientSynced = true;
        notifyAll();
    }
}
