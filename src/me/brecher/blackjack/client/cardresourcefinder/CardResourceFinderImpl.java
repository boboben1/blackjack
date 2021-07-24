package me.brecher.blackjack.client.cardresourcefinder;

import me.brecher.blackjack.shared.models.Card;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardResourceFinderImpl implements CardResourceFinder {
    final Map<Card, Image> cardImages;
    final Map<Card, Image> rawImages;

    final Dimension dim;

    public CardResourceFinderImpl() {
        this.cardImages = new HashMap<>();
        this.rawImages = new HashMap<>();
        this.dim = new Dimension(0, 0);
    }

    @Override
    public Image getCardImage(Card card) {

        if (Objects.equals(card.number().getResourceString(), "") && card.faceUp())
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        try {

            if (this.cardImages.containsKey(card)) {
                return this.cardImages.get(card);
            }

            Image image = ImageIO.read(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(card.faceUp()
                            ? "res/" + card.number().getResourceString() + card.suit().getResourceString() + ".png"
                            : "res/blue_back.png"))
            );

            this.cardImages.put(card, image);

            return image;

        } catch (IOException ex) {
            System.err.println(ex.getMessage());

        }

        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

    }
}
