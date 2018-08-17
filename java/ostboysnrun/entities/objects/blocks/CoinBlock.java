package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.entities.objects.blocks.animations.AnimationEntity;
import ostboysnrun.entities.objects.blocks.animations.CoinAnimation;
import ostboysnrun.window.canvases.InGameCanvas;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CoinBlock extends AnimationEntity implements Collision {

    private BufferedImage[] images;

    @Override
    public int getMaxImage() {
        return type.getImages().length - 1;
    }

    public enum CoinType {
        GOLD(new String[]{
                "Coins/0/0.png",
                "Coins/0/1.png",
                "Coins/0/2.png",
                "Coins/0/3.png"
        }),
        BRONZE(new String[]{
                "Coins/1/0.png",
                "Coins/1/1.png",
                "Coins/1/2.png",
                "Coins/1/3.png"
        });

        private final String[] images;
        CoinType(final String[] images) {
            this.images = images;
        }
        public String[] getImages() {
            return images;
        }
    }

    private CoinType type;

    private int value;

    public CoinBlock(final int x, final int y, final int value, final CoinType type) {
        super();

        this.value = value;
        this.type = type;

        createPosition(x, y);

        final String[] _images = type.getImages();
        images = new BufferedImage[_images.length];
        for (int i = 0; i < _images.length; i++) {
            images[i] = Helper.getImage(_images[i]);
        }

        CoinAnimation.get().addEntity(this);
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

        Properties.addScore(value);

        CoinAnimation.get().removeEntity(this);

        InGameCanvas.getHeightmap().remove(this);
    }

    @Override
    public boolean hasCollision() {
        return false;
    }
}
