package ostboysnrun.dialogues;

import ostboysnrun.Game;
import ostboysnrun.window.canvases.GameCanvas;
import ostboysnrun.window.canvases.InGameCanvas;

import java.awt.*;
import java.util.TimerTask;

abstract public class AbstractDialogue {

    abstract public void draw(final Graphics2D g2d, final GameCanvas canvas);

    private TimerTask remove;
    
    private boolean timerStarted;
    
    public void setup() {
    	timerStarted = false;
    	remove = new TimerTask() {

            private boolean run = true;

            @Override
            public void run() {
                if(run) {
                    afterExecution();

                    remove.cancel();
                    run = false;
                }
            }
        };
    }

    abstract protected void afterExecution();

    abstract public int getDelay();

    protected void draw() {
        if(getDelay() > 0 && !timerStarted) {
            try {
                Game.TIMER.schedule(remove, getDelay(), getDelay());
                timerStarted = true;
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }
}
