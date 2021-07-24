package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiAddCardEvent implements Serializable {
    private final String name = "GuiAddCardEvent";
    private final int playerID;
    private final Card card;

    public GuiAddCardEvent(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Card getCard() {
        return card;
    }

    public String getName() {
        return name;
    }
}
