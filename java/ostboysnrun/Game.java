package ostboysnrun;

import ostboysnrun.controls.GameControls;
import ostboysnrun.controls.MenuControls;
import ostboysnrun.entities.collision.CollisionCheck;
import ostboysnrun.entities.collision.CollisionDirection;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.objects.creatures.Direction;
import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.objects.creatures.State;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.entities.Position;
import ostboysnrun.levels.LevelRegistry;
import ostboysnrun.threads.CreatureThread;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.Frame;
import ostboysnrun.window.grid.Entry;
import ostboysnrun.window.grid.Heightmap;

import java.util.Timer;
import javax.swing.*;
import java.awt.event.KeyListener;

public class Game {

    public enum KeyListenerType {
        GAME,
        MENU,
        NONE
    }

    private static KeyListener keyListener;

    public static void setKeyListener(final KeyListenerType type) {
        switch (type) {
            case GAME:
                keyListener = new GameControls();
                break;
            case MENU:
                keyListener = new MenuControls();
                break;

            case NONE:
                return;
        }

        final Frame frame = Game.getFrame();

        for (final KeyListener listener : frame.getKeyListeners()) {
            frame.removeKeyListener(listener);
        }

        frame.addKeyListener(keyListener);
    }

    /**
     * Global timer for animations
     */
    public static final Timer TIMER = new Timer();

    private static Frame frame;
    private static final Object frameLock = new Object();

    public static Frame getFrame() {
        synchronized (frameLock) {
            return frame;
        }
    }

    private static CreatureThread[] creatureThreads;
    private static int threads = 0;

    private static CreatureThread createCreatureThread(Creature c) {
        return new CreatureThread(c, ++threads);
    }

    /**
     * used by CreatureThread
     */
    private static boolean creaturesAlive = true;

    public static boolean destroyCreatures() {
        creaturesAlive = false;
        try {
            if (creatureThreads != null && creatureThreads.length > 0) {
                for (final CreatureThread t : creatureThreads) {
                    if (t != null) {
                    	t.getCreature().setAlive(true);
                        t.terminate();
                    }
                }
            }

            creatureThreads = null;
            creaturesAlive = true;
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    public static void exit() {
        Properties.setState(Properties.State.EXIT);
        while(!ProgressManager.save());
        System.exit(0);
    }

    public static void initCreatures() {

        if (creatureThreads != null) {
            for (final CreatureThread t : creatureThreads) {
                if (t != null) {
                    t.terminate();
                }
            }
        }

        final Creature[] creatures = Properties.getLevel().getCreatures();
        creatureThreads = new CreatureThread[creatures.length];

        for (int i = 0; i < creatures.length; i++) {
            final Creature creature = creatures[i];
            creatureThreads[i] = createCreatureThread(creature);
        }

        do {
            if (Properties.getGameState() == Properties.GameState.READY || creatureThreads == null) continue;
            
            int length;

            try {
                length = creatureThreads.length;
            } catch (NullPointerException e) {
                //e.printStackTrace();
                continue;
            }

            for (int i = 0; i < length; i++) {
                CreatureThread thread = creatureThreads[i];
                if (thread == null) continue;

                final Creature creature = thread.getCreature();
                final boolean running = thread.isRunning();

                if (Helper.isInView(creature) && creature.isAlive()) {
                    if (!running && thread.getState() != Thread.State.RUNNABLE) {
                        try {
                            thread.start();
                        } catch (IllegalThreadStateException e) {
                            //e.printStackTrace();
                        }
                    }
                } else {
                    if (running) {
                        thread.terminate();
                        creatureThreads[i] = createCreatureThread(creature);
                    }
                }

            }
        } while (Properties.getState() == Properties.State.IN_GAME && creaturesAlive && Properties.getGameState() == Properties.GameState.ALIVE);
    }

    public static void main(String[] args) {

        boolean loaded;

        do {
            try {
                ProgressManager.load();
                loaded = true;
            } catch (Exception e) {
                loaded = false;
                ProgressManager.addLevel(LevelRegistry.byId(1));
                ProgressManager.save();
                //e.printStackTrace();
            }
        } while (!loaded);

        frame = new Frame(Properties.WINDOW.WIDTH, Properties.WINDOW.HEIGHT);
        frame.setup();

        // player action

        // y-axis
        new Thread(() -> {
            float sleep = 5;
            int maxSleep = 5;
            boolean shouldSleep;

            int movedPixels = 0;
            Player player;
            Heightmap<Entity> map;
            Entry<Entity>[] map1D;
            Position playerPosition;
            Creature[] creatures;
            Direction direction;
            State state;

            while (Properties.getState() == Properties.State.IN_GAME) {
                if (Properties.getGameState() == Properties.GameState.READY) continue;
                shouldSleep = false;

                map = InGameCanvas.getHeightmap();
                if (map == null || map.isLoading()) continue;

                player = InGameCanvas.getPlayer();
                map1D = map.getAs1dArray();
                state = player.getState();
                direction = player.getYAxisDirection();
                playerPosition = player.getPosition();
                creatures = Properties.getLevel().getCreatures();

                // trigger jump
                if (direction == Direction.UP && state != State.JUMPING && state != State.FALLING) {
                    player.setState(State.JUMPING);
                    sleep = 1;
                    movedPixels = 0;
                }

                // trigger fall
                if (state != State.JUMPING && state != State.FALLING) {
                    if (!CollisionCheck.checkOnHeightmap(player, map1D, CollisionDirection.BOTTOM) &&
                            !CollisionCheck.checkOnCreatures(player, creatures, CollisionDirection.BOTTOM)) {
                        sleep = 5;
                        player.setState(State.FALLING);
                        player.setDirection(Direction.DOWN, Creature.Axis.Y);
                    }
                }

                switch (state) {
                    case JUMPING:
                        boolean stopJumping = false;
                        if (!CollisionCheck.checkOnHeightmap(player, map1D, CollisionDirection.TOP) &&
                                !CollisionCheck.checkOnCreatures(player, creatures, CollisionDirection.TOP)) {
                            if ((int) sleep != maxSleep)
                                sleep *= 1.025;

                            movedPixels += player.getJumpSpeed();

                            if (movedPixels == Properties.JUMP_HEIGHT) {
                                stopJumping = true;
                            } else {
                                playerPosition.addY(-player.getJumpSpeed());
                                shouldSleep = true;
                            }
                        } else stopJumping = true;

                        if (stopJumping) {
                            player.setState(State.NONE);
                            sleep = 5;
                            player.setDirection(Direction.NONE, Creature.Axis.Y);
                        }
                        break;
                    case FALLING:
                        boolean stopFalling = false;
                        if (!CollisionCheck.checkOnHeightmap(player, map1D, CollisionDirection.BOTTOM) &&
                                !CollisionCheck.checkOnCreatures(player, creatures, CollisionDirection.BOTTOM)) {
                            if ((int) sleep != 1)
                                sleep *= 0.95;

                            movedPixels += player.getJumpSpeed();

                            playerPosition.addY(player.getGravitySpeed());
                            shouldSleep = true;

                        } else stopFalling = true;

                        if (stopFalling) {
                            player.setState(State.NONE);
                            sleep = 1;
                            player.setDirection(Direction.NONE, Creature.Axis.Y);
                        }
                        break;
                }

                if (shouldSleep) {
                    Helper.sleep((int) Math.ceil(sleep));
                }
            }
        }).start();

        // x-axis
        new Thread(() -> {
            int sleep = 5;
            boolean shouldSleep;

            Player player;
            Heightmap<Entity> map;
            Entry<Entity>[] map1D;
            Position playerPosition;
            Creature[] creatures;
            Direction direction;

            while (Properties.getState() == Properties.State.IN_GAME) {

                if (Properties.getGameState() == Properties.GameState.READY) continue;
                shouldSleep = false;

                map = InGameCanvas.getHeightmap();
                if (map == null || map.isLoading()) continue;

                player = InGameCanvas.getPlayer();
                map1D = map.getAs1dArray();
                direction = player.getXAxisDirection();
                playerPosition = player.getPosition();
                creatures = Properties.getLevel().getCreatures();

                switch (direction) {
                    case LEFT:
                        if (!CollisionCheck.checkOnHeightmap(player, map1D, CollisionDirection.LEFT) &&
                                !CollisionCheck.checkOnCreatures(player, creatures, CollisionDirection.LEFT)) {
                            final int offset = Properties.getOffset();
                            if (offset > 0 && (playerPosition.getX() - offset == Properties.PLAYER_OFFSET)) {
                                Properties.addOffset(-player.getSpeed());
                            }

                            playerPosition.addX(-player.getSpeed());
                            shouldSleep = true;
                        }
                        break;

                    case RIGHT:
                        if (!CollisionCheck.checkOnHeightmap(player, map1D, CollisionDirection.RIGHT) &&
                                !CollisionCheck.checkOnCreatures(player, creatures, CollisionDirection.RIGHT)) {
                            boolean isEndInView = false;
                            for (final Entry<Entity> entry : map.getLastColumnEntries()) {
                                if (entry != null && entry.getEntry() != null) {
                                    if (Helper.isFullInView(entry.getEntry())) {
                                        isEndInView = true;
                                    }
                                }
                            }

                            if (playerPosition.getX() >= Properties.PLAYER_OFFSET && !isEndInView) {
                                Properties.addOffset(player.getSpeed());
                            }

                            playerPosition.addX(player.getSpeed());
                            shouldSleep = true;
                        }
                        break;
                }

                if (shouldSleep) {
                    Helper.sleep(sleep);
                }
            }
        }).start();

        setKeyListener(KeyListenerType.NONE);

        SwingUtilities.invokeLater(frame::display);
    }
}
