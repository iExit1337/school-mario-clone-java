package ostboysnrun.threads;

import ostboysnrun.entities.collision.CollisionCheck;
import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.objects.creatures.Direction;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.entities.Position;
import ostboysnrun.Helper;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.grid.Entry;
import ostboysnrun.window.grid.Heightmap;

public class CreatureThread extends Thread {

    private int id;

    private Creature creature;

    public CreatureThread(Creature creature, final int _id) {
        super();
        id = _id;
        this.creature = creature;
    }

    private boolean running = false;
    private final Object runLock = new Object();

    public synchronized Creature getCreature() {
        return creature;
    }

    public synchronized void setRunning(final boolean b) {
        synchronized (runLock) {
            running = b;
        }
    }

    public synchronized boolean isRunning() {
        synchronized (runLock) {
            return running;
        }
    }

    @Override
    public void start() {
        do {
            setRunning(true);
        } while(!isRunning());
        super.start();
    }

    public void terminate() {
        setRunning(false);
        creature.setState(ostboysnrun.entities.objects.creatures.State.NONE);
        creature.setDirection(Direction.NONE);
    }

    @Override
    public void run() {
        boolean sleep;
        boolean longSleep;
        Heightmap<Entity> map;
        Entry<Entity>[] map1D;
        Player player;
        
        while (isRunning() && Properties.getState() != Properties.State.EXIT && !(Properties.getGameState() != Properties.GameState.READY && Properties.getGameState() != Properties.GameState.ALIVE)) {
            if (!Helper.isInView(creature)) continue;

            map = InGameCanvas.getHeightmap();
            if (map.isLoading()) continue;

            map1D = map.getAs1dArray();
            player = InGameCanvas.getPlayer();

            sleep = false;
            longSleep = false;

            final Position p = creature.getPosition();
            final ostboysnrun.entities.objects.creatures.State state = creature.getState();
            final Direction direction = creature.getDirection();

            if (!CollisionCheck.checkOnHeightmap(creature, map1D, CollisionDirection.BOTTOM) && !CollisionCheck.check(creature, player, CollisionDirection.BOTTOM)) {
                p.addY(creature.getGravitySpeed());
                creature.setState(ostboysnrun.entities.objects.creatures.State.FALLING);
                sleep = true;
            } else {
                if (state == ostboysnrun.entities.objects.creatures.State.FALLING) {
                    creature.setState(ostboysnrun.entities.objects.creatures.State.NONE);
                }
            }

            switch (state) {
                case WALKING:
                    switch (direction) {
                        case RIGHT:
                            if (!CollisionCheck.checkOnHeightmap(creature, map1D, CollisionDirection.RIGHT) && !CollisionCheck.check(creature, player, CollisionDirection.RIGHT)) {
                                p.addX(creature.getSpeed());
                                longSleep = true;
                            }
                            break;

                        case LEFT:
                            if (!CollisionCheck.checkOnHeightmap(creature, map1D, CollisionDirection.LEFT) && !CollisionCheck.check(creature, player, CollisionDirection.LEFT)) {
                                p.addX(-creature.getSpeed());
                                longSleep = true;
                            }
                            break;
                    }
                    break;
            }

            if (sleep || longSleep) {
                try {
                    Thread.sleep(longSleep ? 50 : 5);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}
