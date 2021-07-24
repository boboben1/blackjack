package me.brecher.blackjack.client.gui.actions;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.shared.events.BetResetEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BetResetAction extends AbstractAction {

    @Inject
    AsyncEventBus eventBus;

    @Inject
    ClientToServerEventQueue clientToServerEventQueue;

    @Override
    public void actionPerformed(ActionEvent e) {
        this.eventBus.post(new BetResetEvent());
        this.clientToServerEventQueue.sendToServer(new BetResetEvent());
    }
}
