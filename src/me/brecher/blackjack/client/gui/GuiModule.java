package me.brecher.blackjack.client.gui;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import me.brecher.blackjack.client.gui.actions.*;

import javax.swing.*;

public class GuiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(AbstractAction.class).annotatedWith(Names.named("HitAction")).to(HitAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("StandAction")).to(StandAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("BetResetAction")).to(BetResetAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("BetAllInAction")).to(BetAllInAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("DoubleAction")).to(DoubleAction.class);

        bind(GameForm.class).in(Singleton.class);

        bind(HandViewFactory.class).to(HandViewFactoryImpl.class);
        bind(BetChangeFactory.class).to(BetChangeFactoryImpl.class);
    }
}
