package me.brecher.blackjack.server.scoring;

import java.io.Closeable;
import java.lang.ref.Cleaner;
import java.util.Map;

public interface ScoreSaver<K,V> {
    public V getScoreForPlayer(K player);
    public void putScoreForPlayer(K player, V score);
}
