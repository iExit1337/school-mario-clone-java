package ostboysnrun.entities.objects.blocks.animations;

import ostboysnrun.Game;

import java.util.ArrayList;
import java.util.TimerTask;

public abstract class AbstractAnimation {

    private ArrayList<AnimationEntity> entities = new ArrayList<>();
    private final Object entityLock = new Object();

    public void addEntity(final AnimationEntity e) {
        synchronized (entityLock) {
            entities.add(e);
        }
    }

    public void removeEntity(final AnimationEntity e) {
        synchronized (entityLock) {
            entities.remove(e);
        }
    }

    public ArrayList<AnimationEntity> getEntities() {
        synchronized (entityLock) {
            return entities;
        }
    }

    public void start() {
        Game.TIMER.scheduleAtFixedRate(getTask(), getDelay(), getPeriod());
    }

    abstract int getDelay();
    abstract int getPeriod();

    abstract TimerTask getTask();

}
