package ostboysnrun.entities.objects.blocks.animations;

import java.util.TimerTask;

public class CoinAnimation extends AbstractAnimation {

    private static CoinAnimation instance = new CoinAnimation();
    private static final Object instanceLock = new Object();

    public synchronized static CoinAnimation get() {
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
        return 125;
    }

    @Override
    TimerTask getTask() {
        return new BasicAnimationTask(this);
    }
}
