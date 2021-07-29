package me.brecher.blackjack.shared.events;

import me.brecher.blackjack.shared.models.RoundResult;

import java.io.Serializable;
import java.util.List;

public class RoundEndEvent implements Serializable {
    private final String name = "RoundEndEvent";
    private final List<RoundResult> roundResults;

    public RoundEndEvent(List<RoundResult> roundResults)
    {
        this.roundResults = roundResults;
    }

    public List<RoundResult> getRoundResults() {
        return roundResults;
    }

    public String getName() {
        return name;
    }
}
