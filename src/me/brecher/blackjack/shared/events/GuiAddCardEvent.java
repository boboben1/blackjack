package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiAddCardEvent implements Serializable {
    private final String name = "GuiAddCardEvent";
    private final int playerID;
    private final Card card;
    private final int handValue;
    private final int hand;

    public GuiAddCardEvent(int playerID, Card card, int handValue) {
        this.playerID = playerID;
        this.card = card;
        this.handValue = handValue;
        this.hand = 0;
    }

    public GuiAddCardEvent(int playerID, Card card, int handValue, int hand) {
        this.playerID = playerID;
        this.card = card;
        this.handValue = handValue;
        this.hand = hand;
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
