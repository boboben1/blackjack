package me.brecher.blackjack.server.player;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import me.brecher.blackjack.annotations.Dealer;
import me.brecher.blackjack.annotations.Human;
import me.brecher.blackjack.server.player.ai.DealerAI;
import me.brecher.blackjack.server.player.ai.SimpleDealerAI;

public class PlayerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Player.class).annotatedWith(Human.class).to(LocalPlayerImpl.class).in(Singleton.class);
        bind(Player.class).annotatedWith(Dealer.class).to(DealerImpl.class).in(Singleton.class);

        bind(DealerAI.class).to(SimpleDealerAI.class);
    }
}
