package ostboysnrun.entities.objects.blocks.animations;

import java.util.TimerTask;

import ostboysnrun.Properties;

public class BasicAnimationTask extends TimerTask {

    private AbstractAnimation animation;

    public BasicAnimationTask(final AbstractAnimation abstractAnimation) {
        super();
        animation = abstractAnimation;
    }

    @Override
    public void run() { 
    	if(Properties.getGameState() != Properties.GameState.READY && Properties.getGameState() != Properties.GameState.ALIVE) {
    		this.cancel();
    		return;
    	}
    	
        for (final AnimationEntity e : animation.getEntities()) {
            final int currentImage = e.getCurrentImage();
            if (currentImage == e.getMaxImage()) {
                e.setImage(e.getFirstImage());
            } else {
                e.setImage(currentImage + 1);
            }
        }
    }
}
