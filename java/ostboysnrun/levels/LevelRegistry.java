package ostboysnrun.levels;

public class LevelRegistry {

    private static final AbstractLevel[] levels = {
            new FirstLevel(),
            new SecondLevel(),
            new ThirdLevel(),
            new FourthLevel(),
            new FifthLevel()
    };

    public static AbstractLevel byId(final int id) {
        for (AbstractLevel level : levels) {
            if (level.getLevelId() == id) {
                return level.get();
            }
        }

        return null;
    }

}
