package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class AddCardEvent implements Serializable {
    private final int playerID;
    private final Card card;
    private final boolean forceFinish;

    private final String name = "AddCardEvent";

    public AddCardEvent(int playerID, Card card, boolean forceFinish) {
        this.playerID = playerID;
        this.card = card;
        this.forceFinish = forceFinish;
    }

    public AddCardEvent(int playerID, Card card) {
        this.playerID = playerID;
        this.card = card;
        this.forceFinish = false;
    }

    public String getName() {
        return name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Card getCard() {
        return card;
    }

    public boolean isForceFinish() {
        return forceFinish;
    }
}
