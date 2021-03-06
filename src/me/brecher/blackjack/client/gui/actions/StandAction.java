package me.brecher.blackjack.client.gui.actions;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.shared.events.PlayerStandEvent;
import me.brecher.blackjack.shared.events.StartRoundEvent;
import me.brecher.blackjack.shared.gameplay.Gameplay;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StandAction extends AbstractAction {
    @Inject
    Gameplay gameplay;

    @Inject
    AsyncEventBus eventBus;

    @Inject
    ClientToServerEventQueue clientToServerEventQueue;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.gameplay.isPlaying()) {
            this.eventBus.post(new PlayerStandEvent());
            this.clientToServerEventQueue.sendToServer(new PlayerStandEvent());
        } else {
            this.eventBus.post(new StartRoundEvent());
            this.clientToServerEventQueue.sendToServer(new StartRoundEvent());
        }
    }
}
