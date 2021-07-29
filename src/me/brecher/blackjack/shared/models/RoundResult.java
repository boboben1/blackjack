package me.brecher.blackjack.shared.models;

import java.io.Serializable;

public class RoundResult implements Serializable {
    private final int winner;
    private final boolean withBlackjack;

    public RoundResult(int winner, boolean withBlackjack) {
        this.winner = winner;
        this.withBlackjack = withBlackjack;
    }

    public int getWinner() {
        return winner;
    }

    public boolean isWithBlackjack() {
        return withBlackjack;
    }

    @Override
    public String toString() {
        return "RoundResult{" +
                "winner=" + winner +
                ", withBlackjack=" + withBlackjack +
                '}';
    }
}
