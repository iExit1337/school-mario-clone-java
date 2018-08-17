package ostboysnrun.window.canvases;

import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.window.Colors;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class StartScreenCanvas extends GameCanvas {

    private int i = 0;

    private TimerTask t = new TimerTask() {
        @Override
        public void run() {
            i++;

            repaint();
        }
    };

    @Override
    public void setup() {

        Game.TIMER.scheduleAtFixedRate(t, 0, 500);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                Game.getFrame().setCanvas(new MenuCanvas());
                Game.setKeyListener(Game.KeyListenerType.MENU);

                t.cancel();
            }
        });
    }

    private BufferedImage wladik = Helper.getImage("Dialogues/CustomLevel/Death/Wladik.png");

    private final String clickText = "Klicken, um Spiel zu starten";

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        setBackground(Colors.BACKGROUND_LIGHT);

        g2d.drawImage(wladik, 525, 0, null);

        g2d.setColor(Colors.TITLE_SHADOW);
        Helper.writeCenteredText(Properties.GAME_NAME, g2d, Properties.FONTS[2], -75, 0);
        g2d.setColor(Colors.TITLE);
        Helper.writeCenteredText(Properties.GAME_NAME, g2d, Properties.FONTS[2], -75 + 2, 2);

        if (i % 2 == 0) {
            g2d.setColor(Colors.DARKER);
            Helper.writeCenteredText(clickText, g2d, Properties.FONTS[1], -75, 10);
            g2d.setColor(Colors.DARK);
            Helper.writeCenteredText(clickText, g2d, Properties.FONTS[1], -75 + 1, 11);
        }
    }
}
