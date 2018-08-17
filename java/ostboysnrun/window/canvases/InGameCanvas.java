package ostboysnrun.window.canvases;

import ostboysnrun.entities.Entity;
import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.objects.creatures.Direction;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.entities.objects.creatures.State;
import ostboysnrun.entities.Position;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.Properties;
import ostboysnrun.threads.InitGameThread;
import ostboysnrun.threads.UpdateCanvasThread;
import ostboysnrun.window.Colors;
import ostboysnrun.window.grid.Entry;
import ostboysnrun.window.grid.Heightmap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import static ostboysnrun.Properties.GameState.*;

public class InGameCanvas extends GameCanvas {

    private static final Object heightmapLock = new Object();
    private static Heightmap<Entity> heightmap;
    
    public static void setHeightmap(Object map) {
    	synchronized (heightmapLock) {
    		if(map == null) {
    			heightmap = null;
    		}
    	}
	}
    
    public static void setHeightmap(Heightmap<Entity> map) {
        synchronized (heightmapLock) {
            heightmap = map;
        }
    }

    public static Heightmap<Entity> getHeightmap() {
        synchronized (heightmapLock) {
            return heightmap;
        }
    }

    private static final Object playerLock = new Object();
    private static Player player;

    public static void setPlayer(Player p) {
        synchronized (playerLock) {
            player = p;
        }
    }

    public synchronized static Player getPlayer() {
        synchronized (playerLock) {
            return player;
        }
    }
    
    private boolean showLifes = true;
    private TimerTask lifesBlinkTask;
    
    public void blinkLifes() {
    	lifesBlinkTask = new TimerTask() {
    		
    		private int i = 0;
    		
			@Override
			public void run() {
				showLifes = !showLifes;
				i++;
				
				if(i == 10) {
					lifesBlinkTask.cancel();
					showLifes = true;
				}
				
			}
    	};
    	
    	Game.TIMER.scheduleAtFixedRate(lifesBlinkTask, 0, 250);
	}

    private String[] texts = {
            "Score: %s",
            "Level: %s"
    };

    private InitGameThread initGameThread;

    /**
     * load given AbstractLevel
     * if Map is loading or initialization has started, wait for its end
     * @param level
     */
    public void loadLevel(final AbstractLevel level) {
        if(getHeightmap() == null || !getHeightmap().isLoading()) {
            if(!(initGameThread != null && initGameThread.isAlive())) {
                Properties.setLevel(level);
                Properties.setOffset(0);

                final Player player = getPlayer();
                final Position position = player.getPosition();
                final Position startPosition = level.getStartPosition();

                // set startingposition
                position.setX(startPosition.getX());
                position.setY(startPosition.getY());

                // reset states
                player.setState(State.NONE);
                player.setDirection(Direction.NONE, Creature.Axis.X);
                player.setDirection(Direction.NONE, Creature.Axis.Y);

                player.resetLifes();
                Properties.addScore(-Properties.getScore());

                setHeightmap(new Heightmap<>(level.getRows(), level.getColumns(), 10));

                // start initialization
                initGameThread = new InitGameThread(level);
                initGameThread.start();
            } else {
                // destroy existing creatures
                while(!Game.destroyCreatures());
                initGameThread = null;
                loadLevel(level);
            }
        }
    }

    @Override
    public void setup() {
        setPlayer(new Player(3, 5));
        loadLevel(Properties.getLevel());

        // repaint
        new UpdateCanvasThread(this).start();
    }

    public void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.setFont(Properties.FONTS[0]);
        final AbstractLevel level = Properties.getLevel();

        final Heightmap<Entity> map = InGameCanvas.getHeightmap();
        if ((!map.isLoading() || Properties.DEBUG) && Properties.getGameState() == ALIVE) {
            if(level.getBackgroundImage() != null) {
                g2d.drawImage(level.getBackgroundImage(), 0, 0, null);
            } else if(level.getBackgroundColor() != null) {
                setBackground(level.getBackgroundColor());
            } else {
                setBackground(Colors.BACKGROUND);
            }

            for (final Entry<Entity> entry : map.getAs1dArray()) {
                if (entry != null && entry.getEntry() != null && Helper.isInView(entry.getEntry())) {
                    final Entity entity = entry.getEntry();
                    entity.onRender();
                    entity.draw(g2d);
                }
            }

            for (final Creature entity : Properties.getLevel().getCreatures()) {
                if (Helper.isInView(entity)) {
                    entity.onRender();
                    entity.draw(g2d);
                }
            }

            final Player p = getPlayer();

            if (Helper.isInView(p)) {
                p.onRender();
                p.draw(g2d);
            } else {
                Properties.setGameState(Properties.GameState.DEAD);
            }
            
            if(showLifes) {
                for(int i = 0; i < p.getLifes(); i++) {    	
                	final int lifeY = 10;
                	final BufferedImage lifesImage = Helper.getImage("Lifes/0.png");
                	final int lifeX = Properties.WINDOW.WIDTH - lifesImage.getWidth() * i - 30 - i*5;
                	g2d.drawImage(lifesImage, lifeX, lifeY, null);
            	}
            }
        

            g2d.setColor(Colors.SCORE);
            g2d.drawString(texts[0].replace("%s", "" + Properties.getScore()), 10, 20);

            if (Properties.DEBUG) {
                g2d.setColor(Color.WHITE);
                final FontMetrics metrics = g.getFontMetrics(Properties.FONTS[0]);
                final String threadsText = "Threads: " + Thread.getAllStackTraces().size();
                g.drawString(threadsText, Properties.WINDOW.WIDTH - metrics.stringWidth(threadsText) - 25, 20);
            }
            
        } else {
            setBackground(Color.black);
            g2d.setColor(Color.WHITE);
            Helper.writeCenteredText("Lade Level: #" + level.getLevelId(), g2d, Properties.FONTS[0]);
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
