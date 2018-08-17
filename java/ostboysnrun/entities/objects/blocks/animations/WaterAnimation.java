package ostboysnrun.entities.objects.blocks.animations;

import java.util.TimerTask;

public class WaterAnimation extends AbstractAnimation {

    private static WaterAnimation instance = new WaterAnimation();
    private static final Object instanceLock = new Object();

    public synchronized static WaterAnimation get() {
        synchronized (instanceLock) {
            return instance;
        }
    }

    @Override
    int getDelay() {
        return 0;
    }

    @Override
    int getPeriod() {
        return 100;
    }

    @Override
    TimerTask getTask() {
        return new BasicAnimationTask(this);
    }
}
