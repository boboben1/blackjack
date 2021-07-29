package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;
import java.util.List;

public class GuiAddCardsEvent implements Serializable {
    private final String name = "GuiAddCardEvent";
    private final int playerID;
    private final List<Card> cards;
    private final int handValue;
    private final int hand;
    private final int handSize;

    public GuiAddCardsEvent(int playerID, List<Card> cards, int handValue, int handSize, int hand) {
        this.playerID = playerID;
        this.cards = cards;
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

    public List<Card> getCards() {
        return cards;
    }

    public int getHandValue() {
        return handValue;
    }

    public String getName() {
        return name;
    }
}
