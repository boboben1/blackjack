package me.brecher.blackjack.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Util {
    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Image resizedImage(Image image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D gfx = newImage.createGraphics();
        gfx.drawImage(tmp, 0, 0, null);
        gfx.dispose();

        return tmp;
    }
}
