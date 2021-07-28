package me.brecher.blackjack.client.gui.actions;

import com.google.common.eventbus.AsyncEventBus;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.shared.events.BetChangeEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BetChangeAction extends AbstractAction {
    private final AsyncEventBus eventBus;
    private final ClientToServerEventQueue clientToServerEventQueue;

    private final int betChangeAmount;

    public BetChangeAction(AsyncEventBus eventBus, ClientToServerEventQueue clientToServerEventQueue, int betChangeAmount) {
        this.eventBus = eventBus;
        this.betChangeAmount = betChangeAmount;
        this.clientToServerEventQueue = clientToServerEventQueue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.eventBus.post(new BetChangeEvent(betChangeAmount));
        this.clientToServerEventQueue.sendToServer(new BetChangeEvent(betChangeAmount));
    }
}
