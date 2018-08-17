package ostboysnrun.entities.objects.buttons;

import ostboysnrun.Properties;
import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.entities.objects.blocks.CoinBlock;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.entities.objects.creatures.Direction;
import ostboysnrun.window.canvases.InGameCanvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class BlueButtonBlock extends Entity implements Collision {

    private int currentImage = 0;
    private ButtonState state = ButtonState.READY;
    private BufferedImage[] images;

    private String[] imagePaths = {
            "Buttons/Blue/0.png",
            "Buttons/Blue/1.png",
            "Buttons/Blue/2.png",
    };

    public BlueButtonBlock(final int x, final int y) {
        super();
        createPosition(x, y);

        images = new BufferedImage[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = Helper.getImage(imagePaths[i]);
        }
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
        return images[getCurrentImage()].getHeight();
    }

    @Override
    public void onRender() {
        if (getCurrentImage() == 2) {
            timerTask.cancel();
        }
    }

    private int getCurrentImage() {
        return currentImage >= images.length ? images.length - 1 : currentImage;
    }

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            currentImage = currentImage + 1;
        }
    };

    @Override
    public void draw(Graphics2D g) {
        Helper.drawObject(images[getCurrentImage()], this, g);
    }

    @Override
    public void onCollision(Entity e, CollisionDirection collisionDirection) {
        if (collisionDirection == CollisionDirection.TOP && state == ButtonState.READY && InGameCanvas.getPlayer().getYAxisDirection() == Direction.DOWN) {
            state = ButtonState.PRESSED;

            Game.TIMER.scheduleAtFixedRate(timerTask, 0, 100);

            InGameCanvas.getHeightmap().addEntry(new CoinBlock(getPosition().getX(), getPosition().getY() - 4, Helper.getRandomInteger(Properties.MAX_COIN_VALUE / 2 + 1, Properties.MAX_COIN_VALUE), CoinBlock.CoinType.GOLD));
        }
    }

    @Override
    public boolean hasCollision() {
        return state == ButtonState.READY;
    }
}
