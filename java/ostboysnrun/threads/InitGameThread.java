package ostboysnrun.threads;

import ostboysnrun.entities.objects.blocks.animations.AbstractAnimation;
import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.InGameCanvas;

public class InitGameThread extends Thread {

    private AbstractLevel level;

    public InitGameThread(final AbstractLevel level) {
        super();

        this.level = level;
    }

    @Override
    public void run() {
        InGameCanvas.getHeightmap().loadLevel(level);
        for(final AbstractAnimation animation : level.getAnimations()) {
            animation.start();
        }

        //Properties.setGameState(Properties.GameState.READY);
        if(level.getAudioFile() != null) {
        	Helper.playAudio(level.getAudioFile(), level.getAudioDecibels(), true);
        }

        Game.initCreatures();
    }
}
