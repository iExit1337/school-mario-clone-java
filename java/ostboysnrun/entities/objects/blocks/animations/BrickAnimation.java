package ostboysnrun.entities.objects.blocks.animations;

import java.util.TimerTask;

public class BrickAnimation extends AbstractAnimation {

    private static BrickAnimation instance = new BrickAnimation();
    private static final Object instanceLock = new Object();

    public synchronized static BrickAnimation get() {
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
        return 250;
    }

    @Override
    TimerTask getTask() {
        return new BasicAnimationTask(this);
    }
}
