package ostboysnrun.entities.objects.blocks.animations;

import ostboysnrun.entities.Entity;

public abstract class AnimationEntity extends Entity {

    private int currentImage = 0;
    private final Object currentImageLock = new Object();

    public int getCurrentImage() {
        synchronized (currentImageLock) {
            return currentImage;
        }
    }

    public int getFirstImage() {
        return 0;
    }

    abstract public int getMaxImage();

    public void setImage(final int index) {
        synchronized (currentImageLock) {
            currentImage = index;
        }
    }

}
