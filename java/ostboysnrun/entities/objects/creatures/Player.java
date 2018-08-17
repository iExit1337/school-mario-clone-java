package ostboysnrun.entities.objects.creatures;

import ostboysnrun.entities.collision.CollisionCheck;
import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.grid.Entry;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import static ostboysnrun.entities.objects.creatures.Direction.NONE;

public class Player extends Creature implements Collision {

    private int currentImage = 0;
    
    private int lifes = 3;
    private boolean blockLife = false;
    private final Object lifesLock = new Object();

    private BufferedImage[] images;
    private String[] imagePaths = {
            "Player/NONE.png",      // 0
            "Player/WALK_1.png",    // 1
            "Player/WALK_2.png",    // 2
            "Player/WALK_3.png",    // 3
            "Player/WALK_4.png",    // 4
            "Player/WALK_5.png",    // 5
            "Player/WALK_6.png",    // 6
            "Player/WALK_7.png",    // 7
            "Player/WALK_8.png",    // 8
            "Player/WALK_9.png",    // 9
            "Player/WALK_10.png",    // 10
            "Player/WALK_11.png",    // 11
            "Player/WALK_12.png",    // 12
            "Player/JUMPING_1.png",   // 13
            "Player/JUMPING_2.png",   // 14
            "Player/JUMPING_3.png",   // 15
            "Player/FALLING_1.png",   // 16
            "Player/FALLING_2.png",   // 17
            "Player/FALLING_3.png",   // 18
    };
    
    private TimerTask lifeTask;
    
    public void removeLife(final int a) {
    	synchronized(lifesLock) {
    		if(!blockLife) {
    			blockLife = true;
	    		lifes -= a;
	    		
	    		lifeTask = new TimerTask() {
	
	    			@Override
	    			public void run() {
	    				blockLife = false;
	    				lifeTask.cancel();
	    			}
	    			
	    		};
	    		
	    		Game.TIMER.scheduleAtFixedRate(lifeTask, 2500, 10000000);
	    		((InGameCanvas) Game.getFrame().getCanvas()).blinkLifes();
	    		Helper.playAudio("Audios/Blyat.wav", -5.0f);
	    		
	    		if(lifes == 0) {
	    			Properties.setGameState(Properties.GameState.DEAD);
	    		}
    		}
    	}
    }

    public void resetLifes() {
        synchronized (lifesLock) {
            lifes = 3;
        }
    }

    public int getLifes() {
    	synchronized(lifesLock) {
    		return lifes;
    	}
    }
    
    private class RunningTimerTask extends TimerTask {

        private int currentIndex;

        @Override
        public void run() {
            final int[] images = getHorizontalImages(getDirection());
            if(images.length > 1) {
                if (currentIndex + 1 == images.length) {
                    currentIndex = 0;
                } else {
                    currentIndex++;
                }
            } else {
                currentIndex = 0;
            }

            currentImage = images[currentIndex];
        }
    }

    private RunningTimerTask runningTimerTask;

    public Player(final int x, final int y) {
        createPosition(x, y);

        images = new BufferedImage[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = Helper.getImage(imagePaths[i]);
        }

        setState(State.NONE);
        setDirection(NONE);
    }

    private int getVerticalImage(State state, Direction direction) {
        int index = 12;

        if (state == State.JUMPING) {
            if (direction == Direction.NONE) {
                index = 13;
            } else if (direction == Direction.RIGHT) {
                index = 14;
            } else {
                index = 15;
            }
        } else if (state == State.FALLING) {
            if (direction == Direction.NONE) {
                index = 16;
            } else if (direction == Direction.RIGHT) {
                index = 17;
            } else {
                index = 18;
            }
        }

        return index;
    }

    private int[] getHorizontalImages(Direction direction) {
        int[] images;

        switch (direction) {
            case RIGHT:
                images = new int[]{
                        1, 2, 3, 4, 5, 6
                };
                break;

            case LEFT:
                images = new int[]{
                        7, 8, 9, 10, 11, 12
                };
                break;

            case NONE:
            default:
                images = new int[]{
                        0
                };
                break;
        }

        return images;
    }

    @Override
    public void onRender() {
        final State state = getState();
        final Direction direction = getDirection();
        final Entry<Entity>[] map1D = InGameCanvas.getHeightmap().getAs1dArray();
        if (
                (state == State.NONE) && (
                        (direction == Direction.RIGHT && !CollisionCheck.checkOnHeightmap(this, map1D, CollisionDirection.RIGHT)) ||
                                (direction == Direction.LEFT && !CollisionCheck.checkOnHeightmap(this, map1D, CollisionDirection.LEFT))
                )
                ) {
            if (runningTimerTask == null) {
                runningTimerTask = new RunningTimerTask();
                Game.TIMER.scheduleAtFixedRate(runningTimerTask, 0, 100);
            }
        } else {
            if (runningTimerTask != null) {
                runningTimerTask.cancel();
                runningTimerTask = null;
            }
        }

        if (state != State.NONE) {
            currentImage = getVerticalImage(state, direction);
        }

        if (direction == Direction.NONE && state == State.NONE) {
            currentImage = getHorizontalImages(Direction.NONE)[0];
        }
    }

    @Override
    public synchronized int getSpeed() {
        return Properties.WALK_SPEED;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public int getHeight() {
        return images[currentImage].getHeight();
    }

    @Override
    public int getWidth() {
        return images[currentImage].getWidth();
    }

    @Override
    public void draw(Graphics2D g) {
        Helper.drawObject(images[currentImage], this, g);
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {

    }

    public int getGravitySpeed() {
        return 1;
    }

    @Override
    public synchronized int getJumpSpeed() {
        return 1;
    }

    @Override
    public boolean hasCollision() {
        return true;
    }
}