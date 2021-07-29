package me.brecher.blackjack.server.gameplay;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import me.brecher.blackjack.shared.gameplay.Gameplay;

public class GameplayModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Gameplay.class).to(GameplayImpl.class).in(Singleton.class);
        bind(ClientSync.class).in(Singleton.class);
    }
}
