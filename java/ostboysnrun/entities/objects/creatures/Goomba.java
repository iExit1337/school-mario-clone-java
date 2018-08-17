package ostboysnrun.entities.objects.creatures;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Game;
import ostboysnrun.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import static ostboysnrun.entities.objects.creatures.Direction.*;

public class Goomba extends Creature implements Collision {

    private int currentImage = 0;
    private int index = 0;

    private BufferedImage[][] images;
    private String[][] imagePaths = {
            {
                    "Enemies/Goomba/NONE.png"
            },
            // RIGHT
            {
                    "Enemies/Goomba/WALK_0.png",
                    "Enemies/Goomba/WALK_1.png",
                    "Enemies/Goomba/WALK_2.png",
                    "Enemies/Goomba/WALK_3.png",
                    "Enemies/Goomba/WALK_4.png"
            },
            // LEFT
            {
                    "Enemies/Goomba/WALK_5.png",
                    "Enemies/Goomba/WALK_6.png",
                    "Enemies/Goomba/WALK_7.png",
                    "Enemies/Goomba/WALK_8.png",
                    "Enemies/Goomba/WALK_9.png"
            }
    };

    private TimerTask changeDirection = new TimerTask() {
        @Override
        public void run() {
            final int random = Helper.getRandomInteger(1, 10);
            if(random == 1 || random == 2) {
                setDirection(RIGHT);
                setState(State.WALKING);
            } else if(random == 3 || random == 4) {
                setDirection(LEFT);
                setState(State.WALKING);
            } else {
                setDirection(NONE);
                setState(State.NONE);
            }

            currentImage = 0;

            updateIndex();
        }
    };

    private TimerTask walkingAnimation = new TimerTask() {
        @Override
        public void run() {
            if(getState() == State.WALKING) {
                switch (getDirection()) {
                    case NONE:
                        currentImage = 0;
                        break;
                    case RIGHT:
                        currentImage = currentImage - 1 == -1 ? imagePaths[index].length - 1 : currentImage - 1;
                        break;
                    case LEFT:
                        currentImage = currentImage + 1 == imagePaths[index].length ? 0 : currentImage + 1;
                        break;
                }
            }
        }
    };

    public Goomba(int x, int y) {
        super();
        createPosition(x, y);

        index = getIndex();
        images = new BufferedImage[imagePaths.length][imagePaths[1].length];

        for (int i = 0; i < imagePaths.length; i++) {
            for (int j = 0; j < imagePaths[i].length; j++) {
                images[i][j] = Helper.getImage(imagePaths[i][j]);
            }
        }

        setState(State.WALKING);
        setDirection(RIGHT);

        Game.TIMER.scheduleAtFixedRate(walkingAnimation, 0, 100);
        Game.TIMER.scheduleAtFixedRate(changeDirection, 0, 400);
    }

    private void updateIndex() {
        index = getIndex();
    }

    private int getIndex() {
        switch (getDirection()) {
            case LEFT:
                return 2;
            case RIGHT:
                return 1;
            default:
            case NONE:
                return 0;
        }
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public int getHeight() {
        return images[index][currentImage].getHeight();
    }

    @Override
    public int getWidth() {
        return images[index][currentImage].getWidth();
    }

    @Override
    public void draw(Graphics2D g) {
        if(isAlive()) {
            Helper.drawObject(images[index][currentImage], this, g);
        }
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {
    	if(e instanceof Player && isAlive()) {
    		switch(collisionDirection) {
    			case TOP: setAlive(false); break;
    			
    			default:
    				((Player) e).removeLife(1);
    				break;
            }
        }
    }

    @Override
    public boolean hasCollision() {
        return true;
    }
}
