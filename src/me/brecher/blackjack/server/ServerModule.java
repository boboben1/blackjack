package me.brecher.blackjack.server;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.server.deckmanager.DeckManagerImpl;
import me.brecher.blackjack.server.gameplay.GameplayModule;
import me.brecher.blackjack.server.handmanager.HandManagerModule;
import me.brecher.blackjack.server.player.PlayerModule;
import me.brecher.blackjack.server.scoring.ScoringModule;

import java.lang.ref.Cleaner;
import java.util.concurrent.Executors;

public class ServerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Integer.class)
                .annotatedWith(Names.named("DeckSize"))
                .toInstance(4);

        bind(DeckManager.class).to(DeckManagerImpl.class).in(Singleton.class);

        bind(ServerToClientEventQueue.class).in(Singleton.class);
        bind(ServerEventRouter.class).in(Singleton.class);

        install(new GameplayModule());
        install(new HandManagerModule());
        install(new ScoringModule());
        install(new PlayerModule());
    }


    @Provides
    @Singleton
    AsyncEventBus getAsyncEventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }

}
