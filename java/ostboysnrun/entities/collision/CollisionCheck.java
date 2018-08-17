package ostboysnrun.entities.collision;

import ostboysnrun.entities.Entity;
import ostboysnrun.entities.Collision;
import ostboysnrun.entities.objects.creatures.Creature;
import ostboysnrun.entities.Position;
import ostboysnrun.Helper;
import ostboysnrun.window.grid.Entry;

public class CollisionCheck {

    /**
     * @param a         (eg Player)
     * @param b
     * @param direction
     * @return
     */
    public static boolean check(final Entity a, final Entity b, final CollisionDirection direction) {
        final boolean considerInView = !(a instanceof Creature ^ b instanceof Creature);
        if (a != b) {
            boolean check = true;
            if (considerInView) {
                check = Helper.isInView(a) && Helper.isInView(b);
            }

            if (check) {
                switch (direction) {
                    case TOP:
                        return top(a, b);

                    case RIGHT:
                        return right(a, b);

                    case LEFT:
                        return left(a, b);

                    case BOTTOM:
                        return bottom(a, b);
                }
            }
        }
        return false;
    }

    public static boolean checkOnHeightmap(final Entity entity, final Entry<Entity>[] list, final CollisionDirection direction) {
        for (Entry<Entity> entry : list) {
            if (entry == null) continue;
            Entity e = entry.getEntry();
            if (check(entity, e, direction)) {
                return true;
            }
        }

        return false;
    }

    public static boolean sharesRow(final Entity a, final Entity b, final Position pa, final Position pb) {
        int aYMin = pa.getY();
        if (!a.isMovable()) {
            aYMin *= a.getHeight();
        }
        final int aYMax = aYMin + a.getHeight();

        int bYMin = pb.getY();
        if (!b.isMovable()) {
            bYMin *= b.getHeight();
        }
        final int bYMax = bYMin + b.getHeight();

        return (aYMin > bYMin && aYMin < bYMax) || (aYMax > bYMin && aYMax < bYMax) || (aYMin < bYMin && aYMax > bYMax) || (aYMin == bYMin || aYMax == bYMax);
    }

    public static boolean sharesRow(final Entity a, final Entity b) {
        return sharesRow(a, b, a.getPosition(), b.getPosition());
    }

    public static boolean sharesColumn(final Entity a, final Entity b, final Position pa, final Position pb) {
        int aLeft = pa.getX();
        if (!a.isMovable()) {
            aLeft *= b.getWidth();
        }
        final int aRight = aLeft + a.getWidth();

        int bLeft = pb.getX();
        if (!b.isMovable()) {
            bLeft *= b.getWidth();
        }
        final int bRight = bLeft + b.getWidth();

        return ((aLeft > bLeft && aLeft < bRight) || (aRight > bLeft && aRight < bRight)) || (aLeft == bLeft || aRight == bRight) || (aLeft < bLeft && aRight > bRight);
    }

    public static boolean sharesColumn(final Entity a, final Entity b) {
        return sharesColumn(a, b, a.getPosition(), b.getPosition());
    }

    /**
     * Check for collision between
     * in case of Direction RIGHT:
     *  Entity As right and Bs left side
     * in case of Direction LEFT:
     *  Entity Bs right and as left side
     * @param a
     * @param b
     * @param direction
     * @return
     */
    public static boolean rightOrLeft(final Entity a, final Entity b, final CollisionDirection direction) {
        boolean result = false;
        if (sharesRow(a, b) && hasCollision(a) && hasCollision(b)) {
            int aXMin = a.getPosition().getX();
            if (!a.isMovable()) {
                aXMin *= a.getWidth();
            }
            final int aXMax = aXMin + a.getWidth();

            int bXMin = b.getPosition().getX();
            if (!b.isMovable()) {
                bXMin *= b.getWidth();
            }
            final int bXMax = bXMin + b.getWidth();

            switch (direction) {
                case RIGHT:
                    if (bXMin - aXMax <= 0 && bXMax > aXMax) {
                        triggerCollision(a, b, CollisionDirection.RIGHT, CollisionDirection.LEFT);
                        if (((Collision) a).hasCollision() && ((Collision) b).hasCollision()) {
                            result = true;
                        }
                    }
                    break;

                case LEFT:
                    if (aXMin - bXMax <= 0 && aXMax > bXMax) {
                        triggerCollision(a, b, CollisionDirection.LEFT, CollisionDirection.RIGHT);
                        if (((Collision) a).hasCollision() && ((Collision) b).hasCollision()) {
                            result = true;
                        }
                    }
                    break;
            }
        }

        return result;
    }

    public static boolean right(final Entity a, final Entity b) {
        return rightOrLeft(a, b, CollisionDirection.RIGHT);
    }

    public static boolean left(final Entity a, final Entity b) {
        return rightOrLeft(a, b, CollisionDirection.LEFT);
    }

    /**
     * check for Collision between Entity As Top and Bs bottom
     * @param a
     * @param b
     * @return
     */
    public static boolean top(final Entity a, final Entity b) {
        boolean result = false;
        if (sharesColumn(a, b) && hasCollision(a) && hasCollision(b)) {
            int aTop = a.getPosition().getY();
            if (!a.isMovable()) {
                aTop *= a.getHeight();
            }

            int bTop = b.getPosition().getY();
            if (!b.isMovable()) {
                bTop *= b.getHeight();
            }
            final int bBottom = bTop + b.getHeight();

            if (bBottom - aTop == 0 || (bBottom - aTop > 0 && bTop < aTop)) {
                triggerCollision(a, b, CollisionDirection.TOP, CollisionDirection.BOTTOM);
                if (((Collision) a).hasCollision() && ((Collision) b).hasCollision()) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Trigger collision
     * @param a
     * @param b
     * @param da
     * @param db
     */
    private static void triggerCollision(final Entity a, final Entity b, final CollisionDirection da, final CollisionDirection db) {
        ((Collision) a).onCollision(b, da);
        ((Collision) b).onCollision(a, db);
    }

    public static boolean checkOnCreatures(final Entity entity, final Creature[] list, final CollisionDirection cd) {
        for (final Creature e : list) {
            if (e == null || !e.isAlive()) continue;
            if (check(entity, e, cd)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check for Collisions to trigger
     * @param entity
     * @param list
     */
    public static void triggerCollision(final Entity entity, final Entry<Entity>[] list) {
        for (Entry<Entity> entry : list) {
            if (entry != null) {
                final Entity e = entry.getEntry();
                if (e != null && e != entity) {
                    if (check(entity, e, CollisionDirection.RIGHT)) {
                        triggerCollision(entity, e, CollisionDirection.RIGHT, CollisionDirection.LEFT);
                    }

                    if (check(entity, e, CollisionDirection.LEFT)) {
                        triggerCollision(entity, e, CollisionDirection.LEFT, CollisionDirection.RIGHT);
                    }

                    if (check(entity, e, CollisionDirection.TOP)) {
                        triggerCollision(entity, e, CollisionDirection.TOP, CollisionDirection.BOTTOM);
                    }

                    if (check(entity, e, CollisionDirection.BOTTOM)) {
                        triggerCollision(entity, e, CollisionDirection.BOTTOM, CollisionDirection.TOP);
                    }
                }
            }
        }
    }

    private static boolean hasCollision(final Entity e) {
        return e instanceof Collision;
    }

    /**
     * check for collision between Entity As bottom and Bs top
     * @param a
     * @param b
     * @return
     */
    public static boolean bottom(final Entity a, final Entity b) {
        boolean result = false;

        if (sharesColumn(a, b) && hasCollision(a) && hasCollision(b)) {
            int aTop = a.getPosition().getY();
            if (!a.isMovable()) {
                aTop *= a.getHeight();
            }
            final int aBottom = aTop + a.getHeight();

            int bTop = b.getPosition().getY();
            if (!b.isMovable()) {
                bTop *= b.getHeight();
            }

            if (bTop - aBottom == 0 || (bTop - aBottom < 0 && aTop < bTop)) {
                triggerCollision(a, b, CollisionDirection.BOTTOM, CollisionDirection.TOP);
                if (((Collision) a).hasCollision() && ((Collision) b).hasCollision()) {
                    result = true;
                }
            }
        }

        return result;
    }
}
