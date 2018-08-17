package ostboysnrun.entities;

import ostboysnrun.entities.collision.CollisionDirection;

public interface Collision {

    void onCollision(Entity e, CollisionDirection collisionDirection);

    boolean hasCollision();

}
