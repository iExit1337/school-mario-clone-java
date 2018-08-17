package ostboysnrun;

import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Position;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Helper {

    private static HashMap<URL, BufferedImage> imageCache = new HashMap<>();
    private static HashMap<URL, AudioInputStream> audioCache = new HashMap<>();
    public static HashMap<URL, Clip> PlayingAudios = new HashMap<>();

    /**
     * Load image into cache
     *
     * @param path
     */
    public static void loadImage(final String path) {
        final URL url = getPath(path);

        if (!imageCache.containsKey(url)) {
            try {
                imageCache.put(url, ImageIO.read(url));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), "Bild (" + path + ") konnte nicht geladen werden.\n\n" + url, "Fehler", JOptionPane.ERROR_MESSAGE);
                //e.printStackTrace();
            }
        }
    }

    /**
     * Get image by path
     * if image is not in cache it'll be loaded first
     *
     * @param path
     * @return String
     */
    public static BufferedImage getImage(final String path) {
        final URL url = getPath(path);

        if (!imageCache.containsKey(url)) {
            loadImage(path);
        }

        return imageCache.get(url);
    }

    public static void stopAudios() {
        Iterator it = PlayingAudios.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ((Clip) pair.getValue()).stop();

        }

        PlayingAudios = new HashMap<>();
    }

    /**
     * register font
     *
     * @param path
     */
    public static void loadFont(final String path) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Thread.currentThread().getContextClassLoader().getResourceAsStream(path)));
        } catch (FontFormatException | IOException e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
        }
    }

    /**
     * Draw given BufferedImage to Entities position
     *
     * @param image
     * @param entity
     * @param graphics2D
     */
    public static void drawObject(final BufferedImage image, final Entity entity, final Graphics2D graphics2D) {
        final Position p = entity.getPosition();
        int x = p.getX();
        // if e is not Movable, it is static in the grid, so we have to calculate its position
        if (!entity.isMovable()) {
            x *= entity.getWidth();
        }
        x -= Properties.getOffset();

        int y = p.getY();
        // if e is not Movable, it is static in the grid, so we have to calculate its position
        if (!entity.isMovable()) {
            y *= entity.getHeight();
        }

        if (Properties.DEBUG) {
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawRect(x, y, entity.getWidth(), entity.getHeight());
        }

        graphics2D.drawImage(image, x, y, null);
    }

    public static URL getPath(final String path) {
        return Thread.currentThread().getContextClassLoader().getResource(path);
    }

    public static InputStream getFile(final String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static void playAudio(final String path) {
        playAudio(path, 0.0f, false);
    }

    public static void playAudio(final String path, final float decibels) {
        playAudio(path, decibels, false);
    }

    public static void playAudio(final String path, final float decibels, final boolean loop) {
        final URL url = getPath(path);
        if (PlayingAudios.containsKey(url)) return;

        if (!audioCache.containsKey(path)) {
            loadAudio(path);
        }

        new Thread(() -> {
            try {
                final Clip clip = AudioSystem.getClip();
                final AudioInputStream audioStream = audioCache.get(url);
                clip.open(audioStream);

                final FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(decibels);

                PlayingAudios.put(url, clip);

                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        PlayingAudios.remove(url);
                    }
                });

                clip.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }).start();
    }

    private static void loadAudio(final String path) {

        try {
            final AudioInputStream inputStream = AudioSystem.getAudioInputStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
            audioCache.put(getPath(path), inputStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    /**
     * Get random Integer between min & max
     *
     * @param min
     * @param max
     * @return int
     */
    public static int getRandomInteger(final int min, final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * check if Entity is in Window-View
     *
     * @param entity
     * @return boolean
     */
    public static boolean isInView(final Entity entity) {
        return isInView(entity, false);
    }

    public static boolean isFullInView(final Entity entity) {
        return isInView(entity, true);
    }

    private static boolean isInView(final Entity entity, final boolean full) {
        final Position position = entity.getPosition();

        int xMin = position.getX();
        // if e is not Movable, it is static in the grid, so we have to calculate its position
        if (!entity.isMovable()) {
            xMin *= entity.getWidth();
        }
        xMin -= Properties.getOffset();

        final int xMax = xMin + entity.getWidth();

        int yMin = position.getY();
        // if e is not Movable, it is static in the grid, so we have to calculate its position
        if (!entity.isMovable()) {
            yMin *= entity.getHeight();
        }
        final int yMax = yMin + entity.getHeight();

        if (!full) {
            return xMin < Properties.WINDOW.WIDTH && xMax > 0 && yMax > 0 && yMin < Properties.WINDOW.HEIGHT;
        } else {
            return xMin > 0 && xMax < Properties.WINDOW.WIDTH && yMax > 0 && yMin < Properties.WINDOW.HEIGHT;
        }
    }

    public static void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    public static void writeCenteredText(final String text, final Graphics2D g2d, final Font font) {
        writeCenteredText(text, g2d, font, 0, 0);
    }

    public static void writeCenteredText(final String text, final Graphics2D g2d, final Font font, final int offsetX, final int offsetY) {
        final FontMetrics metrics = g2d.getFontMetrics(font);
        g2d.setFont(font);
        g2d.drawString(text, (Properties.WINDOW.WIDTH - metrics.stringWidth(text)) / 2 + offsetX, (Properties.WINDOW.HEIGHT - metrics.getHeight()) / 2 + offsetY);

    }
}
