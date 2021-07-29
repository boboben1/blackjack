package me.brecher.blackjack.server.scoring;

public interface BetManager {
    int getBet();
    int getMinBet();
    boolean canDouble();
    boolean canSplit();
    void doubleDown();
    void split();
}
