package ostboysnrun.dialogues;

import ostboysnrun.ProgressManager;
import ostboysnrun.Properties;
import ostboysnrun.dialogues.AbstractDialogue;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.LevelRegistry;
import ostboysnrun.window.canvases.DialogueCanvas;
import ostboysnrun.window.canvases.GameCanvas;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.Colors;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Finished extends AbstractDialogue {

    private BufferedImage[] images = new BufferedImage[]{
            Helper.getImage("Dialogues/CustomLevel/Finished/Background.png")
    };

    @Override
    public void draw(Graphics2D g2d, GameCanvas canvas) {
        canvas.setBackground(Color.RED);
        g2d.drawImage(images[0], 0, 0, null);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Colors.DARK);
        g2d.drawString("Glueckwunsch!", 100, 25);
        g2d.drawString("Du bist im Besitz von:", 125, 50);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Colors.FINISHED);
        g2d.drawString("DEUTSCHER PASS", 200, 250);

        draw();
    }

    @Override
    protected void afterExecution() {
        final AbstractLevel level = LevelRegistry.byId(2);
        ProgressManager.addLevel(level);
        Properties.setLevel(level);
        Properties.setGameState(Properties.GameState.READY);
        Game.getFrame().setCanvas(new DialogueCanvas());  
    }

    @Override
    public int getDelay() {
        return 2000;
    }
}
