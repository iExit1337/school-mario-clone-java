package ostboysnrun.levels;

import ostboysnrun.dialogues.AbstractDialogue;
import ostboysnrun.entities.objects.blocks.animations.AbstractAnimation;
import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.Position;
import ostboysnrun.Properties;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class AbstractLevel {

    abstract public AbstractDialogue getDialogue(final Properties.GameState gameState);

    abstract public AbstractAnimation[] getAnimations();

    private Creature[] creatures = new Creature[0];
    private final Object creaturesLock = new Object();

    abstract public int getLevelId();
    abstract public int[][][] getMap();
    abstract public String getAudioFile();
    abstract public float getAudioDecibels();
    
    abstract public AbstractLevel get();

    public BufferedImage getBackgroundImage() { return null; }
    public Color getBackgroundColor() { return null; }
    
    abstract public int getRows();
    abstract public int getColumns();

    protected void setCreatures(final Creature[] creatures) {
        synchronized (creaturesLock) {
            this.creatures = creatures;
        }
    }

    public synchronized Creature[] getCreatures() {
        synchronized (creaturesLock) {
            return creatures;
        }
    }

    /**
     * returns starting position of Player-Entity
     * @return Position
     */
    abstract public Position getStartPosition();

}
