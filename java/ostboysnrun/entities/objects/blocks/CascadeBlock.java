package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.entities.objects.blocks.animations.AnimationEntity;
import ostboysnrun.entities.objects.blocks.animations.CascadeAnimation;
import ostboysnrun.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CascadeBlock extends AnimationEntity implements Collision {

    private BufferedImage[] images;

    @Override
    public int getMaxImage() {
        return type.getImages().length - 1;
    }

    public enum CascadeType {
        LIGHT_START(new String[]{
                "Blocks/Cascade/0/0.png",
                "Blocks/Cascade/0/1.png",
                "Blocks/Cascade/0/2.png",
                "Blocks/Cascade/0/3.png",
                "Blocks/Cascade/0/4.png",
                "Blocks/Cascade/0/5.png",
                "Blocks/Cascade/0/6.png",
                "Blocks/Cascade/0/7.png",
        }),
        LIGHT(new String[]{
                "Blocks/Cascade/1/0.png",
                "Blocks/Cascade/1/1.png",
                "Blocks/Cascade/1/2.png",
                "Blocks/Cascade/1/3.png",
                "Blocks/Cascade/1/4.png",
                "Blocks/Cascade/1/5.png",
                "Blocks/Cascade/1/6.png",
                "Blocks/Cascade/1/7.png",
        }),
        DARK_START(new String[]{
                "Blocks/Cascade/2/0.png",
                "Blocks/Cascade/2/1.png",
                "Blocks/Cascade/2/2.png",
                "Blocks/Cascade/2/3.png",
                "Blocks/Cascade/2/4.png",
                "Blocks/Cascade/2/5.png",
                "Blocks/Cascade/2/6.png",
                "Blocks/Cascade/2/7.png",
        }),
        DARK(new String[]{
                "Blocks/Cascade/3/0.png",
                "Blocks/Cascade/3/1.png",
                "Blocks/Cascade/3/2.png",
                "Blocks/Cascade/3/3.png",
                "Blocks/Cascade/3/4.png",
                "Blocks/Cascade/3/5.png",
                "Blocks/Cascade/3/6.png",
                "Blocks/Cascade/3/7.png",
        });

        private final String[] images;
        CascadeType(final String[] images) {
            this.images = images;
        }
        public String[] getImages() {
            return images;
        }
    }

    private CascadeType type;

    public CascadeBlock(final int x, final int y, CascadeType type) {
        super();
        this.type = type;
        createPosition(x, y);

        final String[] _images = type.getImages();
        images = new BufferedImage[_images.length];
        for (int i = 0; i < _images.length; i++) {
            images[i] = Helper.getImage(_images[i]);
        }

        CascadeAnimation.get().addEntity(this);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public int getHeight() {
        return images[getCurrentImage()].getHeight();
    }

    @Override
    public int getWidth() {
        return images[getCurrentImage()].getWidth();
    }

    @Override
    public void draw(Graphics2D g) {
        Helper.drawObject(images[getCurrentImage()], this, g);
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {

    }

    @Override
    public boolean hasCollision() {
        return false;
    }
}
