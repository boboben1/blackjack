package me.brecher.blackjack.client;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinderImpl;
import me.brecher.blackjack.client.gameplay.ClientGameplayImpl;
import me.brecher.blackjack.client.gui.GuiModule;
import me.brecher.blackjack.shared.gameplay.Gameplay;

import java.util.concurrent.Executors;

public class ClientModule extends PrivateModule {
    @Override
    protected void configure() {
        bind(CardResourceFinder.class).to(CardResourceFinderImpl.class).in(Singleton.class);

        bind(ClientToServerEventQueue.class).in(Singleton.class);

        bind(Gameplay.class).to(ClientGameplayImpl.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(Client.class, Client.class).build(ClientFactory.class));
        install(new GuiModule());

        expose(ClientFactory.class);
    }

    @Provides
    @Singleton
    AsyncEventBus getAsyncEventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }

}
