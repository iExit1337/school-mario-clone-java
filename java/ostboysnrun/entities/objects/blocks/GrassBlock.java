package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GrassBlock extends Entity implements Collision {


    public enum GrassBlockType {
        TOP_LEFT("Blocks/Grass/0.png"),
        TOP_MIDDLE("Blocks/Grass/1.png"),
        TOP_RIGHT("Blocks/Grass/2.png"),
        TOP_LEFT_CORNER("Blocks/Grass/6.png"),
        TOP_RIGHT_CORNER("Blocks/Grass/8.png"),

        BOTTOM_LEFT("Blocks/Grass/3.png"),
        BOTTOM_MIDDLE("Blocks/Grass/4.png"),
        BOTTOM_RIGHT("Blocks/Grass/5.png"),
        BOTTOM_RIGHT_REVERSE_CORNER("Blocks/Grass/7.png"),
        BOTTOM_LEFT_REVERSE_CORNER("Blocks/Grass/9.png"),
        BOTTOM_LEFT_CORNER("Blocks/Grass/10.png"),
        BOTTOM_RIGHT_CORNER("Blocks/Grass/14.png"),
        BOTTOM_MIDDLE_GRASS("Blocks/Grass/11.png"),

        LEFT_GRASS("Blocks/Grass/12.png"),
        RIGHT_GRASS("Blocks/Grass/13.png");


        private final String image;
        GrassBlockType(final String image) { this.image = image; }
        public String getImagePath() { return image; }
    }

    private BufferedImage image;

    public GrassBlock(final int x, final int y, final GrassBlockType type) {
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
        return true;
    }
}
