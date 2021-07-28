package me.brecher.blackjack.client.gui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {


    boolean loaded;

    private Font loadingFont;

    private Timer timer;

    private String loadingText;

    private boolean hasError;

    public GamePanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);

        init();
    }

    public GamePanel(LayoutManager layout) {
        super(layout);

        init();
    }

    public GamePanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);

        init();
    }

    public GamePanel() {
        init();
    }

    void setLoaded(boolean hasError) {
        synchronized (this) {
            loaded = true;

            this.timer.stop();

            this.hasError = hasError;

            this.loadingText = "An error has occured while loading";
        }

        repaint();
    }

    protected synchronized void init() {
        this.loadingText = "Loading.";

        this.timer = new Timer(1000,  e -> {
            synchronized (this) {
                this.loadingText += ".";
                if (this.loadingText.length() > 5)
                    this.loadingText = "Loading.";
            }
        });

        this.timer.start();

        this.loadingFont = new Font("TimesRoman", Font.PLAIN, 40);

    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        g.fillRect(0,0,getWidth(),getHeight());

        if (!loaded || hasError) {
            Font oldFont = g.getFont();
            g.setFont(loadingFont);
            g.drawString(loadingText, getWidth()/2,getHeight()/2);
            g.setFont(oldFont);
        }

    }
}
