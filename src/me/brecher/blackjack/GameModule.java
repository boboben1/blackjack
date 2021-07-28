package me.brecher.blackjack;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import me.brecher.blackjack.client.ClientModule;
import me.brecher.blackjack.server.ServerModule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ClientModule());
        install(new ServerModule());
    }

    @Provides @Singleton
    ScheduledExecutorService getExecutorService() {
        //return Executors.newCachedThreadPool();
        return Executors.newScheduledThreadPool(10);
    }
}
