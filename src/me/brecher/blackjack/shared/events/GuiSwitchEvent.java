package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.Card;

import java.io.Serializable;

public class GuiSwitchEvent implements Serializable {
    private final String name = "GuiSwitchHand";
    private final int playerID;

    public GuiSwitchEvent(int playerID) {
        this.playerID = playerID;
    }


    public int getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }
}
