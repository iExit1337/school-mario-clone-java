package ostboysnrun.window.canvases;

import ostboysnrun.Game;
import ostboysnrun.Helper;
import ostboysnrun.ProgressManager;
import ostboysnrun.Properties;
import ostboysnrun.entities.objects.blocks.animations.AbstractAnimation;
import ostboysnrun.levels.AbstractLevel;
import ostboysnrun.levels.LevelRegistry;
import ostboysnrun.window.Colors;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MenuCanvas extends GameCanvas {

    private BufferedImage[] arrows = new BufferedImage[] {
            Helper.getImage("Levelmenu/arrow_0.png"),
            Helper.getImage("Levelmenu/arrow_1.png"),
    };

    @Override
    public void setup() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {}

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {
                for (final Button b : buttons) {
                    if (b.isMouseHover(mouseEvent)) {
                        b.setHover(true);
                    } else {
                        b.setHover(false);
                    }
                }

                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(mouseEvent);

                for (final Button b : buttons) {
                    if (b.isHover && !b.disabled) {
                        b.runnable.run();
                    }
                }
            }
        });
    }

    private void loadLevel(final int id) {
        final AbstractLevel level = LevelRegistry.byId(id);
        if (level != null) {
            Properties.setLevel(level);
            Game.getFrame().setCanvas(new DialogueCanvas());
        }
    }

    private static final int PADDING_SIDES = 25;
    private static final int PADDING_TOP = PADDING_SIDES + 50;
    private static final int PADDING_BOTTOM = PADDING_SIDES;

    private int buttonsCount = 0;
    private Button[] buttons = new Button[]{
            new Button("1", buttonsCount++, 0, ButtonType.LEVEL, !ProgressManager.hasLevelUnlocked(buttonsCount), () -> loadLevel(1)),
            new Button("2", buttonsCount++, 0, ButtonType.LEVEL, !ProgressManager.hasLevelUnlocked(buttonsCount), () -> loadLevel(2)),
            new Button("3", buttonsCount++, 0, ButtonType.LEVEL, !ProgressManager.hasLevelUnlocked(buttonsCount), () -> loadLevel(3)),
            new Button("4", buttonsCount++, 0, ButtonType.LEVEL, !ProgressManager.hasLevelUnlocked(buttonsCount), () -> loadLevel(4)),
            new Button("5", buttonsCount++, 0, ButtonType.LEVEL, !ProgressManager.hasLevelUnlocked(buttonsCount), () -> loadLevel(5)),

            new Button("Neues Spiel", 0, 3, ButtonType.RESET, false, () -> {
                ProgressManager.getUnlockedLevels().removeIf(i -> i == null || i.getLevelId() != 1);

                ProgressManager.save();
                try {
                    ProgressManager.load();
                } catch (Exception e) {
                    //e.printStackTrace();
                }

                Game.getFrame().setCanvas(new StartScreenCanvas());
                Game.setKeyListener(Game.KeyListenerType.MENU);
            }),
            new Button("Beenden", 3, 3, ButtonType.CLOSE, false, Game::exit),
    };

    private enum ButtonType {

        LEVEL(Color.decode("#34495e"), Color.decode("#2c3e50"), Color.decode("#95a5a6"), Color.decode("#ecf0f1"), (Properties.WINDOW.WIDTH - PADDING_SIDES*2) / 5, Properties.WINDOW.WIDTH/15),
        CLOSE(Color.decode("#c0392b"), Color.decode("#e74c3c"), null, Color.decode("#ecf0f1"), 160, 45),
        RESET(Color.decode("#27ae60"), Color.decode("#2ecc71"), null, Color.decode("#ecf0f1"), 160, 45);

        private Color color;
        private Color hover;
        private Color disabled;
        private Color textColor;

        private int width;
        private int height;

        ButtonType(final Color color, final Color hover, final Color disabled, final Color textColor, final int width, final int height) {
            this.color = color;
            this.hover = hover;
            this.disabled = disabled;
            this.textColor = textColor;
            this.width = width;
            this.height = height;
        }
    }

    private class Button {

        private String text;
        private int x;
        private int y;
        private Runnable runnable;
        private ButtonType type;
        private boolean disabled;

        private boolean isHover = false;

        void setHover(final boolean hover) {
            isHover = hover;
        }

        Button(final String text, final int x, final int y, final ButtonType type, final boolean disabled, final Runnable runnable) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.type = type;
            this.disabled = disabled;
            this.runnable = runnable;
        }

        boolean isMouseHover(final MouseEvent event) {
            final int mx = event.getX();
            final int my = event.getY();

            final int bStartX = x * type.width + PADDING_SIDES;
            final int bEndX = bStartX + type.width;

            final int bStartY = y * type.height + PADDING_TOP;
            final int bEndY = bStartY + type.height;

            return mx >= bStartX && mx <= bEndX && my >= bStartY && my <= bEndY;
        }

        void draw(final Graphics2D g2d) {

            if (!disabled) {
                if (!isHover) {
                    g2d.setColor(type.color);
                } else {
                    g2d.setColor(type.hover);
                }
            } else {
                g2d.setColor(type.disabled);
            }

            g2d.fillRect(PADDING_SIDES + x * type.width, PADDING_TOP + y * type.height, type.width, type.height);

            final FontMetrics metrics = g2d.getFontMetrics(Properties.FONTS[3]);
            g2d.setFont(Properties.FONTS[3]);
            g2d.setColor(type.textColor);
            g2d.drawString(text, PADDING_SIDES + x * type.width + (type.width - metrics.stringWidth(text)) / 2, PADDING_TOP + y * type.height + (type.height - metrics.getHeight()) / 2 + metrics.getHeight());
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Graphics2D g2d = (Graphics2D) g;
        final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        setBackground(Colors.BACKGROUND_LIGHT);

        g2d.setFont(Properties.FONTS[1]);
        g2d.setColor(Colors.TITLE_SHADOW);

        final FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        final int titleY = (PADDING_TOP - metrics.getHeight()) - metrics.getHeight() / 3;

        final String title = "Levelauswahl";
        g2d.drawString(title, 25, titleY);
        g2d.setColor(Colors.TITLE);
        g2d.drawString(title, 27, titleY + 2);

        g2d.drawImage(arrows[0], 20, 125, null);
        g2d.drawImage(arrows[1], 590, 125, null);

        for (final Button b : buttons) {
            b.draw(g2d);
        }

        g2d.setColor(Color.WHITE);
        g2d.setFont(Properties.FONTS[3]);

        final AffineTransform transformCopy = g2d.getTransform();

        g2d.rotate(75);
        g2d.drawString("Start", -10, 180);
        g2d.setTransform(transformCopy);
        g2d.rotate(-50);
        g2d.drawString("Deutscher Pass", 500, 20);
        g2d.setTransform(transformCopy);
    }
}
