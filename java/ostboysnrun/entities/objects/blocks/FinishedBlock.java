package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Helper;
import ostboysnrun.Properties;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FinishedBlock extends Entity implements Collision {

    private BufferedImage image;

    private String imagePath = "Blocks/Finished/0.png";

    public FinishedBlock(final int x, final int y) {
        super();
        createPosition(x, y);

        image = Helper.getImage(imagePath);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public void draw(Graphics2D g) {
        Helper.drawObject(image, this, g);
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {
        if(collisionDirection == CollisionDirection.TOP) {
            Properties.setGameState(Properties.GameState.FINISHED);
        }
    }

    @Override
    public boolean hasCollision() {
        return true;
    }
}
