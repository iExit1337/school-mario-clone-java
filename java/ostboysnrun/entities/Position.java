package ostboysnrun.entities;

public class Position {

    private int x;
    private int y;

    private final Object xLock = new Object();
    private final Object yLock = new Object();

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void addX(final int x) {
        synchronized (xLock) {
            this.x += x;
        }
    }

    public synchronized void addY(final int y) {
        synchronized (yLock) {
            this.y += y;
        }
    }

    public synchronized void setX(final int x) {
        synchronized (xLock) {
            this.x = x;
        }
    }

    public synchronized void setY(final int y) {
        synchronized (yLock) {
            this.y = y;
        }
    }

    public synchronized int getX() {
        synchronized (xLock) {
            return x;
        }
    }

    public synchronized int getY() {
        synchronized (yLock) {
            return y;
        }
    }
}
