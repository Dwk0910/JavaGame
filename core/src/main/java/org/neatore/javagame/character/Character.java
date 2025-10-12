package org.neatore.javagame.character;

import org.neatore.javagame.object.character.Direction;
import org.neatore.javagame.object.map.Renderable;

public abstract class Character extends Renderable {
    public float speed;
    public float WIDTH, HEIGHT;
    public boolean isMoving;
    public Direction direction;

    public abstract void update();
}
