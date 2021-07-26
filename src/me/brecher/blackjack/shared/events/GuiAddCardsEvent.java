package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;
import java.util.List;

public class GuiAddCardsEvent implements Serializable {
    private final String name = "GuiAddCardEvent";
    private final int playerID;
    private final List<Card> cards;
    private final int handValue;

    public GuiAddCardsEvent(int playerID, List<Card> cards, int handValue) {
        this.playerID = playerID;
        this.cards = cards;
        this.handValue = handValue;
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
