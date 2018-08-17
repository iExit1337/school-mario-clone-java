package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BushBlock extends Entity implements Collision {

    public enum BushBlockType {
        SINGLE("Blocks/Bush/0.png"),
        LEFT("Blocks/Bush/1.png"),
        MIDDLE("Blocks/Bush/2.png"),
        RIGHT("Blocks/Bush/3.png");

        private final String image;
        BushBlockType(final String image) { this.image = image; }
        public String getImagePath() { return image; }
    }

    private BufferedImage image;

    public BushBlock(final int x, final int y, final BushBlockType type) {
        super();
        createPosition(x, y);

        image = Helper.getImage(type.getImagePath());
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

    }

    @Override
    public boolean hasCollision() {
        return false;
    }
}
