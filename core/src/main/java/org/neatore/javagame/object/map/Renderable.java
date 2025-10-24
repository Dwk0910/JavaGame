package org.neatore.javagame.object.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Renderable {
    float getX();
    float getY();
    void render(SpriteBatch batch);
}
