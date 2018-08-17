package ostboysnrun;

import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.FirstLevel;
import ostboysnrun.window.canvases.DialogueCanvas;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.canvases.MenuCanvas;

import java.awt.*;

public class Properties {

    public static class Window {
        public final int WIDTH = 700;
        public final int HEIGHT = 300;
    }

    public static final String GAME_NAME = "OSTBOYS'N'RUN";

    public static Font[] FONTS = new Font[]{
            new Font("Super Mario 256", Font.BOLD, 12),
            new Font("Super Mario 256", Font.BOLD, 24),
            new Font("Super Mario 256", Font.BOLD, 55),
            new Font("Super Mario 256", Font.PLAIN, 14),
    };

    public enum GameState {
        READY,
        ALIVE,
        DEAD,
        FINISHED,
        MENU
    }

    public enum State {
        IN_GAME,
        EXIT
    }

    public static final int PLAYER_OFFSET = 100;

    public static final boolean DEBUG = false;

    private static State state = State.IN_GAME;
    private static final Object stateLock = new Object();
    public static void setState(final State s) {
        synchronized (stateLock) {
            state = s;
        }
    }
    public static State getState() {
        synchronized (stateLock) {
            return state;
        }
    }

    private static GameState gameState = GameState.READY;
    private static final Object gameStateLock = new Object();
	public static void setGameState(final GameState s) {
        synchronized (gameStateLock) {
        	gameState = s;
        
        	switch(gameState) {
        		
    		case READY:
    		case ALIVE:
				Game.setKeyListener(Game.KeyListenerType.GAME);
				Game.getFrame().setCanvas(new InGameCanvas());
				break;
    		case MENU:
    			Game.setKeyListener(Game.KeyListenerType.MENU);
				Game.destroyCreatures();
				Game.getFrame().setCanvas(new MenuCanvas());
				InGameCanvas.setHeightmap(null);
				gameState = GameState.READY;
    			break;
			case DEAD:
			case FINISHED:
				Game.setKeyListener(Game.KeyListenerType.MENU);
				Game.destroyCreatures();
				Game.getFrame().setCanvas(new DialogueCanvas());
				InGameCanvas.setHeightmap(null);

				Helper.stopAudios();
				break;
			default:
				break;
        	}
        }
    }
    public static GameState getGameState() {
        synchronized (gameStateLock) {
            return gameState;
        }
    }

    private static AbstractLevel level = new FirstLevel();
    private static final Object levelLock = new Object();
    public synchronized static void setLevel(final AbstractLevel l) {
        synchronized (levelLock) {
            level = l;
        }
    }
    public static AbstractLevel getLevel() {
        synchronized (levelLock) {
            return level;
        }
    }

    private static int score = 0;
    private static final Object scoreLock = new Object();

    public static final int MAX_COIN_VALUE = 100;

    public static int getScore() {
        synchronized (scoreLock) {
            return score;
        }
    }
    public static void addScore(final int num) {
        synchronized (scoreLock) {
            score += num;
        }
    }

    public static final Window WINDOW = new Window();

    public static final int JUMP_HEIGHT = 16*4 + 8; // 4.5 blocks

    public static final int WALK_SPEED = 1;

    private static final Object offsetLock = new Object();
    private static int offset = 0;

    public static int getOffset() {
        synchronized (offsetLock) {
            return offset;
        }
    }

    public static void addOffset(final int o) {
        synchronized (offsetLock) {
            offset += o;
        }
    }
    public static void setOffset(final int o) {
        synchronized (offsetLock) {
            offset = o;
        }
    }
}
