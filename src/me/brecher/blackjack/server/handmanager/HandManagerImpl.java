package me.brecher.blackjack.server.handmanager;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.Hand;

import java.util.List;

public class HandManagerImpl implements HandManager {

    private final AsyncEventBus eventBus;
    private final int playerID;
    private Hand playerHand;


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

//
//    @Subscribe
//    public void addCardEvent(AddCardEvent event) {
//        if (event.getPlayerID() == this.playerID) {
//
//            this.activeHand().addCard(event.getCard());
//            this.eventBus.post(new GuiAddCardEvent(event.getPlayerID(), event.getCard()));
//
//
//
//            if (this.activeHand().value() >= 21 && this.playerID == 1 || this.playerID == 1 && event.isForceFinish()) {
//                this.eventBus.post(new PlayerStandEvent());
//            }
//        }
//    }

//    @Subscribe
//    public void roundBegan(RoundBeganEvent event) {
//        this.playerHand = new Hand();
//    }

    @Override
    public void reset() {
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

    @Override
    public boolean canDouble() {
        return this.activeHand().canDouble();
    }

    @Override
    public void addCard(Card card) {
        this.activeHand().addCard(card);

        this.eventBus.post(new GuiAddCardEvent(playerID, card, this.handValue()));
    }

    @Override
    public void addCards(List<Card> cards) {
        for (Card card : cards)
            this.activeHand().addCard(card);

        this.eventBus.post(new GuiAddCardsEvent(playerID, cards, this.handValue()));
    }
}
