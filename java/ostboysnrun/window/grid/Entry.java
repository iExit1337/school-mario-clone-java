package ostboysnrun.window.grid;

import ostboysnrun.entities.Entity;

public class Entry<T extends Entity> {

    private T entry;

    public Entry(final T entry) {
        this.entry = entry;
    }

    public synchronized T getEntry() {
        return entry;
    }

}
