package me.brecher.blackjack.server.scoring;

public interface ScoreSaver<K,V> {
    V getScoreForPlayer(K player);
    void putScoreForPlayer(K player, V score);
}
