package ostboysnrun.entities.objects.blocks.bricks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.entities.objects.blocks.animations.AnimationEntity;
import ostboysnrun.entities.objects.blocks.animations.BrickAnimation;
import ostboysnrun.entities.objects.blocks.CoinBlock;
import ostboysnrun.entities.objects.creatures.State;
import ostboysnrun.Helper;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.grid.Heightmap;

import java.awt.*;
import java.awt.image.BufferedImage;

final public class BrownBrickBlock extends AnimationEntity implements Collision {

    private BufferedImage[] images;

    private String[] imagePaths = {
            "Blocks/Brick/0.png",
            "Blocks/Brick/1.png",
            "Blocks/Brick/2.png",
            "Blocks/Brick/3.png",
            "Blocks/Brick/4.png",
    };

    private boolean hasAnimation = true;

    private boolean hasCoin;
    private BrickState state = BrickState.NONE;

    public BrownBrickBlock(final int x, final int y, final boolean hasCoin) {
        this(x, y, hasCoin, true);
    }

    public BrownBrickBlock(final int x, final int y, final boolean hasCoin, final boolean hasAnimation) {
        super();
        super.createPosition(x, y);

        this.hasCoin = hasCoin;
        this.hasAnimation = hasAnimation;

        images = new BufferedImage[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = Helper.getImage(imagePaths[i]);
        }

        if(hasAnimation) {
            BrickAnimation.get().addEntity(this);
        }
    }

    public boolean hasCollision() {
        return true;
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {
        if(state == BrickState.NONE && collisionDirection == CollisionDirection.BOTTOM) {
            if(InGameCanvas.getPlayer().getState() == State.JUMPING) {
                state = hasCoin ? BrickState.DESTROYED : BrickState.HIT;

                switch (state) {
                    case DESTROYED:
                        Heightmap<Entity> map = InGameCanvas.getHeightmap();
                        map.addEntry(new CoinBlock(this.getPosition().getX(), this.getPosition().getY() - 1, Helper.getRandomInteger(51, 100), CoinBlock.CoinType.GOLD));
                        map.remove(this);
                        break;

                    case HIT:
                        if (hasAnimation) {
                            BrickAnimation.get().getEntities().remove(this);

                            setImage(imagePaths.length - 1);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public boolean hasGravity() {
        return true;
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
    public int getMaxImage() {
        return imagePaths.length - 2;
    }
}
