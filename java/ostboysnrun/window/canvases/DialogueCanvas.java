package ostboysnrun.window.canvases;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.Properties.GameState;
import ostboysnrun.dialogues.AbstractDialogue;
import ostboysnrun.window.Colors;

public class DialogueCanvas extends GameCanvas {

	private AbstractDialogue dialogue;
	private GameState gameState;
	
	@Override
	public void setup() {
		gameState = Properties.getGameState();
		dialogue = Properties.getLevel().getDialogue(gameState);
		if(dialogue != null) {
			dialogue.setup();
		}
	}
	
	public void paintComponent(final Graphics g) {
		
		final Graphics2D g2d = (Graphics2D) g;
		
		if(dialogue == null) {
			switch(gameState) {
			case FINISHED:
				g2d.setColor(Colors.FINISHED);
				g2d.fillRect(0, 0, Properties.WINDOW.WIDTH, Properties.WINDOW.HEIGHT);
                g2d.setFont(Properties.FONTS[1]);
                g2d.setColor(Color.WHITE);
                Helper.writeCenteredText("Level geschafft!", g2d, Properties.FONTS[1]);
                g2d.setFont(Properties.FONTS[0]);
                Helper.writeCenteredText("Score: " + Properties.getScore(), g2d, Properties.FONTS[0], 0, 10);
				break;
				
			case DEAD:
				setBackground(Colors.DEAD);
                g2d.setFont(Properties.FONTS[1]);
                g2d.setColor(Color.WHITE);
                Helper.writeCenteredText("Du bist gestorben!", g2d, Properties.FONTS[1]);
				break;
				
			case READY:
				Properties.setGameState(Properties.GameState.ALIVE);
				break;
			}
		} else {
			 dialogue.draw(g2d, this);
		}
	}
}
