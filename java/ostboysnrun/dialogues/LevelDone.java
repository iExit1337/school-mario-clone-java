package ostboysnrun.dialogues;

import java.awt.Color;
import java.awt.Graphics2D;

import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.ProgressManager;
import ostboysnrun.Properties;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.LevelRegistry;
import ostboysnrun.window.Colors;
import ostboysnrun.window.canvases.DialogueCanvas;
import ostboysnrun.window.canvases.GameCanvas;

public class LevelDone extends AbstractDialogue {

	@Override
	public void draw(Graphics2D g2d, GameCanvas canvas) {
		g2d.setColor(Colors.FINISHED);
		g2d.fillRect(0, 0, Properties.WINDOW.WIDTH, Properties.WINDOW.HEIGHT);
        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Color.WHITE);
        Helper.writeCenteredText("Level geschafft!", g2d, Properties.FONTS[1]);
        g2d.setFont(Properties.FONTS[0]);
        Helper.writeCenteredText("Score: " + Properties.getScore(), g2d, Properties.FONTS[0], 0, 10);
        
        draw();
	}

	@Override
	protected void afterExecution() {
	    final AbstractLevel level = LevelRegistry.byId(Properties.getLevel().getLevelId() + 1);
	    if(level != null) {
	        ProgressManager.addLevel(level);
	        Properties.setLevel(level);
	        Properties.setGameState(Properties.GameState.READY);
	        Game.getFrame().setCanvas(new DialogueCanvas());  
	    }
	}
	
	@Override
	public int getDelay() {
	    return 1000;
	}
}
