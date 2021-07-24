package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class RoundEndEvent implements Serializable {
    private final String name = "RoundEndEvent";
    private final boolean blackJack;
    private final int result;

    public RoundEndEvent(boolean blackJack, int result)
    {
        this.blackJack = blackJack;
        this.result = result;
    }

    public boolean isBlackJack() {
        return blackJack;
    }

    public int getResult() {
        return result;
    }

    public String getName() {
        return name;
    }
}
