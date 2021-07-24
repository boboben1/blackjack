package me.brecher.blackjack.server.scoring;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class ScoringModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ScoreKeeper.class).to(ScoreKeeperImpl.class).in(Singleton.class);
        bind(Long.class).annotatedWith(Names.named("MinMoney")).toInstance(100L);
        bind(ScoreSaver.class).to(ScoreSaverImpl.class).in(Singleton.class);

        bind(String.class).annotatedWith(Names.named("SaveFile")).toInstance("scores.bin");

        bind(Integer.class).annotatedWith(Names.named("MinBet")).toInstance(1);

        bind(BetManager.class).to(BetManagerImpl.class).in(Singleton.class);
    }
}
