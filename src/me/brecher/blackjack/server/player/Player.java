package me.brecher.blackjack.server.player;

public interface Player {
    void beginTurn();

    void waitForTurn();

    void finishTurn();

    boolean isTakingTurn();
}
