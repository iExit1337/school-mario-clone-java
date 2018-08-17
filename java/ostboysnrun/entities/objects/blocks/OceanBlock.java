package ostboysnrun.entities.objects.blocks;

import ostboysnrun.entities.objects.blocks.animations.AnimationEntity;
import ostboysnrun.entities.objects.blocks.animations.WaterAnimation;
import ostboysnrun.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;

public class OceanBlock extends AnimationEntity {

    private BufferedImage[] images;

    @Override
    public int getMaxImage() {
        return type.getImages().length - 1;
    }

    public enum OceanType {
        RELAXED(new String[]{
                "Blocks/Ocean/0/0.png",
                "Blocks/Ocean/0/1.png",
                "Blocks/Ocean/0/2.png",
                "Blocks/Ocean/0/3.png"
        }),
        WAVY(new String[]{
                "Blocks/Ocean/1/0.png",
                "Blocks/Ocean/1/1.png",
                "Blocks/Ocean/1/2.png",
                "Blocks/Ocean/1/3.png",
        });

        private final String[] images;

        OceanType(final String[] images) {
            this.images = images;
        }

        public String[] getImages() {
            return images;
        }
    }

    private OceanType type;

    public OceanBlock(final int x, final int y, final OceanType type) {
        super();
        createPosition(x, y);
        this.type = type;

        final String[] _images = type.getImages();
        images = new BufferedImage[_images.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = Helper.getImage(_images[i]);
        }

        WaterAnimation.get().addEntity(this);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public int getHeight() {
        return images[getCurrentImage()].getHeight();
    }

    @Override
    public int getWidth() {
        return images[getCurrentImage()].getWidth();
    }

    @Override
    public void draw(Graphics2D g) {
        Helper.drawObject(images[getCurrentImage()], this, g);
    }
}
