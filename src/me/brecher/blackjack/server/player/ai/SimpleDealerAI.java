package me.brecher.blackjack.server.player.ai;

public class SimpleDealerAI implements DealerAI {


    public SimpleDealerAI() { }

    @Override
    public Actions takeAction(int handValue) {
        if (handValue >= 17) {
            return Actions.STAND;
        }


        return Actions.HIT;
    }
}
