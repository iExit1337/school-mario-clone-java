package ostboysnrun.window;

import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.ProgressManager;
import ostboysnrun.Properties;
import ostboysnrun.window.canvases.GameCanvas;
import ostboysnrun.window.canvases.StartScreenCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Frame extends JFrame {

    private int width;
    private int height;

    private GameCanvas canvas;
    private final Object canvasLock = new Object();

    public Frame(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public GameCanvas getCanvas() {
        synchronized (canvasLock) {
            return canvas;
        }
    }

    public void setup() {
        setSize(width, height);
        setTitle(Properties.GAME_NAME);
        setLocationRelativeTo(null);
        setResizable(Properties.DEBUG);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Game.exit();
            }
        });

        Helper.loadFont("Font/SuperMario256.ttf");

        setIconImage(Helper.getImage("Frame/Icon.png"));

        setCanvas(new StartScreenCanvas());
    }

    public void setCanvas(final GameCanvas c) {
        synchronized (canvasLock) {
            Container container = getContentPane();
            if(canvas != null) container.remove(canvas);
            canvas = c;
            canvas.setPreferredSize(new Dimension(width, height));

            canvas.setup();
            container.add(canvas);

            revalidate();
            repaint();
        }
    }

    public void display() {
        setVisible(true);
        requestFocus();
    }

}
