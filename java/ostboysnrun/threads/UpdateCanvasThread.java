package ostboysnrun.threads;

import ostboysnrun.Game;
import ostboysnrun.Properties;
import ostboysnrun.entities.collision.CollisionCheck;
import ostboysnrun.entities.Entity;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.window.canvases.InGameCanvas;
import ostboysnrun.window.grid.Heightmap;

import java.util.TimerTask;

public class UpdateCanvasThread extends Thread {

    private Heightmap<Entity> map;
    private Player player;
    private InGameCanvas canvas;
    
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
        	
        	if(Properties.getGameState() != Properties.GameState.READY && Properties.getGameState() != Properties.GameState.ALIVE) {
        		timerTask.cancel();
        	}
        	
            fetchData();

            //if (!map.isLoading()) {
            //    CollisionCheck.triggerCollision(player, map.getAs1dArray());
            //}

            canvas.repaint();
        }
    };

    public UpdateCanvasThread(final InGameCanvas canvas) {
        super();

        this.canvas = canvas;
    }

    private void fetchData() {
        map = InGameCanvas.getHeightmap();
        player = InGameCanvas.getPlayer();
    }

    @Override
    public void run() {
        Game.TIMER.scheduleAtFixedRate(timerTask,0,5);
    }
}
