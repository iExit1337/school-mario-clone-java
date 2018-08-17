package ostboysnrun.dialogues;

import ostboysnrun.Properties;
import ostboysnrun.Helper;
import ostboysnrun.window.canvases.GameCanvas;
import ostboysnrun.window.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Death extends AbstractDialogue {

    private BufferedImage[] images = new BufferedImage[]{
            Helper.getImage("Dialogues/CustomLevel/Death/Background.png"),
            Helper.getImage("Dialogues/CustomLevel/Death/Wladik.png")
    };

    @Override
    public void draw(Graphics2D g2d, GameCanvas canvas) {
        canvas.setBackground(Color.RED);
        g2d.drawImage(images[0], 0, 0, null);
        g2d.drawImage(images[1], 450, 0, null);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Color.WHITE);
        g2d.drawString("du bist 1 toter Mann", 50, 100);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Colors.DEAD);
        g2d.drawString("BLYAT", 100, 125);

        draw();
    }

    @Override
    protected void afterExecution() {
        Properties.setGameState(Properties.GameState.MENU);
    }

    @Override
    public int getDelay() {
        return 2000;
    }
}
