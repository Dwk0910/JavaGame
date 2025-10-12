package org.neatore.javagame.object.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Renderable {
    public float x, y;
    public abstract void render(SpriteBatch batch);
}
