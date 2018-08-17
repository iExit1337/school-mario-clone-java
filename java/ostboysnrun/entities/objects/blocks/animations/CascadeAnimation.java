package ostboysnrun.entities.objects.blocks.animations;

import java.util.TimerTask;

public class CascadeAnimation extends AbstractAnimation {

    private static CascadeAnimation instance = new CascadeAnimation();
    private static final Object instanceLock = new Object();

    public synchronized static CascadeAnimation get() {
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
        return 75;
    }

    @Override
    TimerTask getTask() {
        return new BasicAnimationTask(this);
    }
}
