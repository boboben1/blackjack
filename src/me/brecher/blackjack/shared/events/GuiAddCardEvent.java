package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiAddCardEvent implements Serializable {
    private final String name = "GuiAddCardEvent";
    private final int playerID;
    private final Card card;
    private final int handValue;
    private final int hand;
    private final int handSize;

    public GuiAddCardEvent(int playerID, Card card, int handValue, int handSize, int hand) {
        this.playerID = playerID;
        this.card = card;
        this.handValue = handValue;
        this.hand = hand;
        this.handSize = handSize;
    }

    public int getHandSize() {
        return handSize;
    }

    public int getHand() {
        return hand;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Card getCard() {
        return card;
    }

    public int getHandValue() {
        return handValue;
    }

    public String getName() {
        return name;
    }
}
