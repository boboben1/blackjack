package me.brecher.blackjack.client;

import com.google.common.eventbus.AsyncEventBus;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientEventBus {
    private final AsyncEventBus clientBound;
    private final AsyncEventBus serverBound;
    public ClientEventBus() {
        Executor executor = Executors.newSingleThreadExecutor();

        clientBound = new AsyncEventBus(executor);
        serverBound = new AsyncEventBus(executor);
    }

    public void register(Object object) {
        this.clientBound.register(object);
    }

    public void registerServer(Object object){
        this.serverBound.register(object);
    }

    public void postClient(Object event) {
        this.clientBound.post(event);
    }

    public void postServer(Object event) {
        this.serverBound.post(event);
    }


}
