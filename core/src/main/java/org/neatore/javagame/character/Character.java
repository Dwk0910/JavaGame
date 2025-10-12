package org.neatore.javagame.character;

import org.neatore.javagame.object.character.Direction;
import org.neatore.javagame.object.map.Renderable;

public abstract class Character implements Renderable {
    public float speed;
    public float WIDTH, HEIGHT;
    public float x, y;
    public boolean isMoving;
    public Direction direction;

    public abstract void update();

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }
}
