package me.brecher.blackjack.client.cardresourcefinder;

import me.brecher.blackjack.shared.models.Card;

import java.awt.*;

public interface CardResourceFinder {
    Image getCardImage(Card card);
}
