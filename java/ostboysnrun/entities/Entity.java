package ostboysnrun.entities;

import java.awt.*;

abstract public class Entity {

    private Position position;

    private final Object positionLock = new Object();

    public synchronized Position getPosition() {
        synchronized (positionLock) {
            return position;
        }
    }

    protected void createPosition(final int x, final int y) {
        position = new Position(x, y);
    }

    abstract public boolean isMovable();

    abstract public int getHeight();
    abstract public int getWidth();

    abstract public void draw(Graphics2D g);

    public void onRender() {

    }

    public boolean hasGravity() {
        return false;
    }
}
