package me.brecher.blackjack.server.handmanager;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.Hand;

import java.util.ArrayList;
import java.util.List;

public class HandManagerImpl implements HandManager {

    private final AsyncEventBus eventBus;
    private final int playerID;
    //private Hand playerHand;

    private final List<Hand> hands;

    private int activeHand;

    private boolean didSplit;


    @Inject
    public HandManagerImpl(AsyncEventBus eventBus, @Assisted int playerID) {
        //this.playerHand = new Hand();
        this.hands = new ArrayList<>();
        reset();

        this.playerID = playerID;

        this.eventBus = eventBus;
        this.eventBus.register(this);

    }



    @Override
    public boolean hasBlackjack() {
        return activeHand().hasBlackjack();
    }

    @Override
    public void reset() {
        //this.playerHand = new Hand();
        this.hands.clear();
        this.hands.add(new Hand());

        this.hands.add(new Hand());
        this.activeHand = 0;

        this.didSplit = false;
    }

    @Override
    public int handValue() {
        return this.activeHand().value();
    }

    @Override
    public Hand activeHand() {
        return this.hands.size() > activeHand ? this.hands.get(activeHand) : null;
    }

    @Override
    public boolean canDouble() {
        return this.activeHand().canDouble();
    }

    @Override
    public void addCard(Card card) {
        this.activeHand().addCard(card);

        this.eventBus.post(new GuiAddCardEvent(playerID, card, this.handValue(), activeHand));
    }

    @Override
    public void addCards(List<Card> cards) {
        for (Card card : cards)
            this.activeHand().addCard(card);

        this.eventBus.post(new GuiAddCardsEvent(playerID, cards, this.handValue(), activeHand));
    }

    @Override
    public boolean canSplit() {
        return activeHand == 0 && hands.get(1).value() == 0 && activeHand().canSplit();
    }

    @Override
    public void split() {
        List<Card> cards = activeHand().split();
        reset();

        this.didSplit = true;

        this.hands.get(0).addCard(cards.get(0));
        this.hands.get(1).addCard(cards.get(1));

        this.eventBus.post(new GuiSplitEvent(playerID, cards.get(0), cards.get(1), handValue()));
    }

    @Override
    public boolean next() {
        if (this.didSplit && this.activeHand == 0) {
            this.activeHand = 1;
            return false;
        }


        return true;
    }
}
