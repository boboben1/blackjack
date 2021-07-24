package me.brecher.blackjack.client;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinderImpl;
import me.brecher.blackjack.client.gameplay.ClientGameplayImpl;
import me.brecher.blackjack.client.gui.GuiModule;
import me.brecher.blackjack.shared.AsyncEventBusProvider;
import me.brecher.blackjack.shared.gameplay.Gameplay;

public class ClientModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(CardResourceFinder.class).to(CardResourceFinderImpl.class).in(Singleton.class);
        //bind(ClientEventBus.class);

        bind(AsyncEventBus.class).toProvider(AsyncEventBusProvider.class);

        bind(ClientToServerEventQueue.class).in(Singleton.class);

        bind(Gameplay.class).to(ClientGameplayImpl.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(Client.class, Client.class).build(ClientFactory.class));
        install(new GuiModule());
    }
}
