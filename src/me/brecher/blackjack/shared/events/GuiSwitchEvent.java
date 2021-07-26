package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiSwitchEvent implements Serializable {
    private final String name = "GuiSwitchHand";
    private final int playerID;
    private final Card drawn;

    public GuiSwitchEvent(int playerID, Card card) {
        this.playerID = playerID;
        this.drawn = card;
    }

    public Card getDrawn() {
        return drawn;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }
}
