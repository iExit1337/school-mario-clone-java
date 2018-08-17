package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.entities.objects.creatures.State;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.window.canvases.InGameCanvas;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SelectLevelBlock extends Entity implements Collision {

    private AbstractLevel level;
    private BufferedImage image;
    private String[] imagePaths = {
        "Blocks/Level/0.png",
        "Blocks/Level/1.png",
        "Blocks/Level/2.png",
        "Blocks/Level/3.png",
        "Blocks/Level/4.png",
    };

    public SelectLevelBlock(final int x, final int y, final AbstractLevel l) {
        super();
        createPosition(x, y);

        level = l;
        image = Helper.getImage(imagePaths[l.getLevelId()]);
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
        if(e instanceof Player) {
            Player p = (Player) e;
            if (Game.getFrame().getCanvas() instanceof InGameCanvas) {
                ((InGameCanvas) Game.getFrame().getCanvas()).loadLevel(level);
            }
        }
    }

    @Override
    public boolean hasCollision() {
        return true;
    }
}
