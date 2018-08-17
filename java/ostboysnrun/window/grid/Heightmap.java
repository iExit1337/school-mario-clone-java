package ostboysnrun.window.grid;

import ostboysnrun.entities.Entity;
import ostboysnrun.entities.objects.blocks.bricks.BrownBrickBlock;
import ostboysnrun.entities.objects.blocks.*;
import ostboysnrun.entities.objects.buttons.BlueButtonBlock;
import ostboysnrun.entities.Position;
import ostboysnrun.Helper;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.LevelRegistry;
import ostboysnrun.Properties;

public class Heightmap<T extends Entity> {

    private int rows;
    private int columns;
    private int positionMax;
    public static int BOTTOM_ROW = 0;

    private boolean loading = false;
    private final Object loadingLock = new Object();

    public boolean isLoading() {
        synchronized (loadingLock) {
            return loading;
        }
    }

    public void setLoading(final boolean state) {
        synchronized (loadingLock) {
            loading = state;
        }
    }

    private Entry<T>[][][] entries;

    public Heightmap(final int rows, final int columns, final int positionMax) {
        this.rows = rows;
        this.columns = columns;
        this.positionMax = positionMax;

        BOTTOM_ROW = rows - 1;

        entries = new Entry[rows][columns][positionMax];
    }

    /**
     * Load Level, sets isLoading to true until process is done
     *
     * @param level
     */
    public void loadLevel(AbstractLevel level) {
        setLoading(true);
        final int[][][] map = level.getMap();
        entries = new Entry[rows][columns][positionMax];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                for (int z = 0; z < map[y][x].length; z++) {
                    final int type = map[y][x][z];
                    T entry = null;
                    if (type == Mapping.AIR) continue;
                    else if (type == Mapping.GRASS_TOP_LEFT) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.TOP_LEFT);
                    } else if (type == Mapping.GRASS_BOTTOM_LEFT) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_LEFT);
                    } else if (type == Mapping.GRASS_TOP_MIDDLE) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.TOP_MIDDLE);
                    } else if (type == Mapping.GRASS_BOTTOM_MIDDLE) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_MIDDLE);
                    } else if (type == Mapping.GRASS_TOP_RIGHT) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.TOP_RIGHT);
                    } else if (type == Mapping.GRASS_BOTTOM_RIGHT) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_RIGHT);
                    } else if (type == Mapping.GRASS_BOTTOM_RIGHT_REVERSE_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_RIGHT_REVERSE_CORNER);
                    } else if (type == Mapping.GRASS_TOP_LEFT_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.TOP_LEFT_CORNER);
                    } else if (type == Mapping.GRASS_TOP_RIGHT_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.TOP_RIGHT_CORNER);
                    } else if (type == Mapping.GRASS_BOTTOM_LEFT_REVERSE_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_LEFT_REVERSE_CORNER);
                    } else if (type == Mapping.GRASS_BOTTOM_LEFT_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_LEFT_CORNER);
                    } else if (type == Mapping.GRASS_BOTTOM_RIGHT_CORNER) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_RIGHT_CORNER);
                    } else if (type == Mapping.GRASS_BOTTOM_MIDDLE_GRASS) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.BOTTOM_MIDDLE_GRASS);
                    } else if (type == Mapping.GRASS_LEFT_GRASS) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.LEFT_GRASS);
                    } else if (type == Mapping.GRASS_RIGHT_GRASS) {
                        entry = (T) new GrassBlock(x, y, GrassBlock.GrassBlockType.RIGHT_GRASS);
                    } else if (type == Mapping.COIN_GOLD) {
                        entry = (T) new CoinBlock(x, y, Helper.getRandomInteger(Properties.MAX_COIN_VALUE / 2 + 1, Properties.MAX_COIN_VALUE), CoinBlock.CoinType.GOLD);
                    } else if (type == Mapping.COIN_BRONZE) {
                        entry = (T) new CoinBlock(x, y, Helper.getRandomInteger(1, Properties.MAX_COIN_VALUE / 2), CoinBlock.CoinType.BRONZE);
                    } else if (type == Mapping.CASCADE_START) {
                        entry = (T) new CascadeBlock(x, y, CascadeBlock.CascadeType.LIGHT_START);
                    } else if (type == Mapping.CASCADE) {
                        entry = (T) new CascadeBlock(x, y, CascadeBlock.CascadeType.LIGHT);
                    } else if (type == Mapping.CASCADE_DARK_START) {
                        entry = (T) new CascadeBlock(x, y, CascadeBlock.CascadeType.DARK);
                    } else if (type == Mapping.CASCADE_DARK) {
                        entry = (T) new CascadeBlock(x, y, CascadeBlock.CascadeType.DARK_START);
                    } else if (type == Mapping.BRICK_BROWN_NO_COIN) {
                        entry = (T) new BrownBrickBlock(x, y, false);
                    } else if (type == Mapping.BRICK_BROWN_WITH_COIN) {
                        entry = (T) new BrownBrickBlock(x, y, true);
                    } else if (type == Mapping.BRICK_BROWN_NO_ANIMATION) {
                        entry = (T) new BrownBrickBlock(x, y, false, false);
                    } else if (type == Mapping.OCEAN) {
                        entry = (T) new OceanBlock(x, y, OceanBlock.OceanType.RELAXED);
                    } else if (type == Mapping.OCEAN_WAVY) {
                        entry = (T) new OceanBlock(x, y, OceanBlock.OceanType.WAVY);
                    } else if (type == Mapping.BUTTON_BLUE) {
                        entry = (T) new BlueButtonBlock(x, y);
                    } else if (type == Mapping.BUSH) {
                        entry = (T) new BushBlock(x, y, BushBlock.BushBlockType.SINGLE);
                    } else if (type == Mapping.BUSH_START) {
                        entry = (T) new BushBlock(x, y, BushBlock.BushBlockType.LEFT);
                    } else if (type == Mapping.BUSH_MIDDLE) {
                        entry = (T) new BushBlock(x, y, BushBlock.BushBlockType.MIDDLE);
                    } else if (type == Mapping.BUSH_END) {
                        entry = (T) new BushBlock(x, y, BushBlock.BushBlockType.RIGHT);
                    } else if (type == Mapping.LEVEL_FINISHED) {
                        entry = (T) new FinishedBlock(x, y);
                    } else if (type > 10000) {
                        final int levelId = type - 10000;
                        entry = (T) new SelectLevelBlock(x, y, LevelRegistry.byId(levelId));
                    }

                    if (entry != null) addEntry(entry);
                }
            }
        }

        setLoading(false);
    }

    public void remove(T entity) {
        Entry<T>[][][] array = new Entry[rows][columns][positionMax];
        for (int y = 0; y < entries.length; y++) {
            for (int x = 0; x < entries[y].length; x++) {
                for (int z = 0; z < entries[y][x].length; z++) {
                    if (entries[y][x][z] != null && entries[y][x][z].getEntry() != entity) {
                        array[y][x][z] = entries[y][x][z];
                    }
                }
            }
        }

        entries = array;
    }

    /**
     * Converts 3D-heightmap to 1D
     *
     * @return
     */
    public Entry<T>[] getAs1dArray() {
        Entry<T>[] result = new Entry[rows * columns * positionMax];

        for (int c = 0, y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                for (int z = 0; z < positionMax; z++) {
                    if (entries[y][x][z] != null) {
                        result[c++] = entries[y][x][z];
                    }
                }
            }
        }

        return result;
    }

    public Entry<T>[] getLastColumnEntries() {
        Entry<T>[] entries = new Entry[rows * positionMax];
        for(int i = 0, y = 0; y < rows; y++) {
            for(int z = 0; z < positionMax; z++) {
                entries[i++] = this.entries[y][columns - 1][z];
            }
        }

        return entries;
    }

    /**
     * Add Object of T to list
     *
     * @param entry
     */
    public void addEntry(final T entry) {
        final Position p = entry.getPosition();

        Entry<T>[] positionEntries = entries[p.getY()][p.getX()];
        int index = 0;
        // increase z-position first valid
        for (; index < positionEntries.length; index++) {
            if (positionEntries[index] == null) break;
        }
        positionEntries[index] = new Entry<>(entry);
    }
}
