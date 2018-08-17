package ostboysnrun.controls;

import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.objects.creatures.Direction;
import ostboysnrun.entities.objects.creatures.Player;
import ostboysnrun.entities.objects.creatures.State;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.InGameCanvas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameControls implements KeyListener {

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    /**
     * Set Directions
     * @param keyEvent
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(!InGameCanvas.getHeightmap().isLoading()) {
            final Player p = InGameCanvas.getPlayer();
            final State s = p.getState();
            final Direction d = p.getDirection();
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (s != State.WALKING && d != Direction.RIGHT) {
                        p.setDirection(Direction.LEFT);
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (s != State.WALKING && d != Direction.LEFT) {
                        p.setDirection(Direction.RIGHT);
                    }
                    break;

                case KeyEvent.VK_UP:
                case KeyEvent.VK_SPACE:
                    if (s != State.FALLING && s != State.JUMPING) {
                        p.setDirection(Direction.UP, Creature.Axis.Y);
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(!InGameCanvas.getHeightmap().isLoading()) {
            final Player p = InGameCanvas.getPlayer();
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    p.setDirection(Direction.NONE, Creature.Axis.X);
                    break;

                case KeyEvent.VK_SPACE:
                    if(p.getState() == State.JUMPING) {
                        p.setDirection(Direction.NONE, Creature.Axis.Y);
                        p.setState(State.NONE);
                    }
                    break;
            }
        }
    }
}
