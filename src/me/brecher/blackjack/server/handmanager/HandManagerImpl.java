package me.brecher.blackjack.server.handmanager;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.Hand;

public class HandManagerImpl implements HandManager {

    final AsyncEventBus eventBus;
    final int playerID;
    Hand playerHand;


    @Override
    public boolean hasBlackjack() {
        return activeHand().hasBlackjack();
    }

    @Inject
    public HandManagerImpl(AsyncEventBus eventBus, @Assisted int playerID) {
        this.playerHand = new Hand();

        this.playerID = playerID;

        this.eventBus = eventBus;
        this.eventBus.register(this);
    }


    @Subscribe
    public void addCard(AddCardEvent event) {
        if (event.getPlayerID() == this.playerID) {

            this.activeHand().addCard(event.getCard());
            this.eventBus.post(new GuiAddCardEvent(event.getPlayerID(), event.getCard()));



            if (this.activeHand().value() >= 21 && this.playerID == 1) {
                this.eventBus.post(new PlayerStandEvent());
            }
        }
    }

    @Subscribe
    public void roundBegan(RoundBeganEvent event) {
        this.playerHand = new Hand();
    }

    @Override
    public int handValue() {
        return this.activeHand().value();
    }

    @Override
    public Hand activeHand() {
        return playerHand;
    }

}
