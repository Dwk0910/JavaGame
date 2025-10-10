package org.neatore.javagame.object.map.obj;

import org.neatore.javagame.object.map.Impassable;
import org.neatore.javagame.object.map.MapObject;

public class Block extends MapObject implements Impassable {
    public Block(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
}
