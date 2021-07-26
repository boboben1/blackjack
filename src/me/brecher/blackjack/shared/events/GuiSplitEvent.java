package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiSplitEvent implements Serializable {
    private final String name = "GuiSplitEvent";
    private final int playerID;
    private final Card card1;
    private final Card card2;
    private final int value;

    public GuiSplitEvent(int playerID, Card card1, Card card2, int value) {
        this.playerID = playerID;
        this.card1 = card1;
        this.card2 = card2;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Card getCard1() {
        return card1;
    }

    public Card getCard2() {
        return card2;
    }

    public String getName() {
        return name;
    }
}
