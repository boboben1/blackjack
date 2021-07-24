package me.brecher.blackjack.server.scoring;

import java.util.Map;

public interface ScoreSaver {
    void save(Map<Integer,Long> scoreMap);
    Map<Integer, Long> load();
    boolean saveExists();
}
