package me.brecher.blackjack.client.gui;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import me.brecher.blackjack.client.gui.actions.BetAllInAction;
import me.brecher.blackjack.client.gui.actions.BetResetAction;
import me.brecher.blackjack.client.gui.actions.HitAction;
import me.brecher.blackjack.client.gui.actions.StandAction;

import javax.swing.*;

public class GuiModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(AbstractAction.class).annotatedWith(Names.named("HitAction")).to(HitAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("StandAction")).to(StandAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("BetResetAction")).to(BetResetAction.class);
        bind(AbstractAction.class).annotatedWith(Names.named("BetAllInAction")).to(BetAllInAction.class);

        bind(GameForm.class).in(Singleton.class);

        bind(HandViewFactory.class).to(HandViewFactoryImpl.class);
        bind(BetChangeFactory.class).to(BetChangeFactoryImpl.class);
    }
}
