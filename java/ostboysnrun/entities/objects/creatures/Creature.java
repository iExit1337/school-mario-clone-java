package ostboysnrun.entities.objects.creatures;

import ostboysnrun.entities.Entity;

abstract public class Creature extends Entity {

    public enum Axis {
        X,
        Y
    }

    public int getSpeed() {
        return 1;
    }
    public int getGravitySpeed() { return 1; }
    public int getJumpSpeed() { return 1; }

    private boolean isAlive = true;
    private final Object aliveLock = new Object();
    public synchronized boolean isAlive() {
        synchronized (aliveLock) {
            return isAlive;
        }
    }

    public synchronized void setAlive(final boolean alive) {
        synchronized (aliveLock) {
            isAlive = alive;
        }
    }

    private Direction xAxisDirection = Direction.RIGHT;
    private final Object xAxisDirectionLock = new Object();

    private Direction yAxisDirection = Direction.RIGHT;
    private final Object yAxisDirectionLock = new Object();

    public synchronized Direction getDirection(final Axis axis) {
        if(axis == Axis.X) {
            return getXAxisDirection();
        } else {
            return getYAxisDirection();
        }
    }

    public synchronized Direction getXAxisDirection() {
        synchronized (xAxisDirectionLock) {
            return xAxisDirection;
        }
    }

    public synchronized Direction getYAxisDirection() {
        synchronized (yAxisDirectionLock) {
            return yAxisDirection;
        }
    }

    public synchronized Direction getDirection() {
        return getDirection(Axis.X);
    }

    public synchronized void setDirection(Direction direction) {
        setDirection(direction, Axis.X);
    }

    public void setXAxisDirection(Direction direction) {
        synchronized (xAxisDirectionLock) {
            xAxisDirection = direction;
        }
    }

    public void setYAxisDirection(Direction direction) {
        synchronized (yAxisDirectionLock) {
            yAxisDirection = direction;
        }
    }

    public synchronized void setDirection(Direction direction, Axis axis) {
        if(axis == Axis.X) {
            setXAxisDirection(direction);
        } else {
            setYAxisDirection(direction);
        }
    }

    private State state = State.NONE;
    private final Object stateLock = new Object();

    public synchronized State getState() {
        synchronized (stateLock) {
            return state;
        }
    }

    public synchronized void setState(State state) {
        synchronized (stateLock) {
            this.state = state;
        }
    }


    // TODO change
    private final int BLOCK = 16;

    public void createPosition(final int x, final int y) {
        super.createPosition(x * BLOCK - BLOCK, y * BLOCK - BLOCK);
    }

}
