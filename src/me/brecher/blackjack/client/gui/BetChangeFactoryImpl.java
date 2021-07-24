package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import me.brecher.blackjack.client.Client;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.client.gui.actions.BetChangeAction;

import javax.swing.*;

public class BetChangeFactoryImpl implements BetChangeFactory {

    private final Provider<AsyncEventBus> asyncEventBusProvider;
    private final Provider<ClientToServerEventQueue> clientToServerEventQueueProvider;

    @Inject
    BetChangeFactoryImpl(Provider<AsyncEventBus> asyncEventBusProvider, Provider<ClientToServerEventQueue> clientToServerEventQueueProvider) {
        this.asyncEventBusProvider = asyncEventBusProvider;
        this.clientToServerEventQueueProvider = clientToServerEventQueueProvider;
    }

    @Override
    public AbstractAction create(int amount) {
        return new BetChangeAction(asyncEventBusProvider.get(), clientToServerEventQueueProvider.get(), amount);
    }
}
