package me.brecher.blackjack.shared;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import java.util.concurrent.Executors;


@Singleton
public class AsyncEventBusProvider implements Provider<AsyncEventBus> {

    final AsyncEventBus eventBus;

    public AsyncEventBusProvider() {
        this.eventBus = new AsyncEventBus(Executors.newSingleThreadExecutor());
    }

    @Override
    public AsyncEventBus get() {
        return eventBus;
    }
}
