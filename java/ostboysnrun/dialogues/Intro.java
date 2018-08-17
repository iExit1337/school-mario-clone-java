package ostboysnrun.dialogues;

import ostboysnrun.dialogues.AbstractDialogue;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.GameCanvas;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Intro extends AbstractDialogue {

    private BufferedImage[] images = new BufferedImage[]{
            Helper.getImage("Dialogues/CustomLevel/Intro/Background.png"),
            Helper.getImage("Dialogues/CustomLevel/Intro/Slavik.png")
    };

    @Override
    public void draw(final Graphics2D g2d, final GameCanvas canvas) {
        canvas.setBackground(Color.GREEN);
        g2d.drawImage(images[0], 0, 0, null);
        g2d.drawImage(images[1], -50, 0, null);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Bruder, dein Mission:", 200, 100);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Colors.SCORE);
        g2d.drawString("DEUTSCHER PASS", 250, 125);

        draw();
    }

    @Override
    protected void afterExecution() {
        Properties.setGameState(Properties.GameState.ALIVE);
    }

    @Override
    public int getDelay() {
        return 2500;
    }
}
