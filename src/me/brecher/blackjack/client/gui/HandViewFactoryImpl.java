package me.brecher.blackjack.client.gui;


import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import com.google.inject.Provider;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class HandViewFactoryImpl implements HandViewFactory {
    private final Provider<AsyncEventBus> asyncEventBusProvider;
    private final Provider<CardResourceFinder> cardResourceFinderProvider;
    private final Provider<GuiHandManager> guiHandManagerProvider;

    private HandView newHandView;

    @Inject
    HandViewFactoryImpl(Provider<AsyncEventBus> asyncEventBusProvider,
                        Provider<CardResourceFinder> cardResourceFinderProvider,
                        Provider<GuiHandManager> guiHandManagerProvider) {
        this.asyncEventBusProvider = asyncEventBusProvider;
        this.cardResourceFinderProvider = cardResourceFinderProvider;
        this.guiHandManagerProvider = guiHandManagerProvider;
    }


    @Override
    public HandView create(int playerNumber) {

        try {
            EventQueue.invokeAndWait(() -> newHandView =
                    new HandView(playerNumber,
                            this.cardResourceFinderProvider.get(),
                            this.asyncEventBusProvider.get(),
                            this.guiHandManagerProvider.get()
                    ));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return newHandView;
    }
}
