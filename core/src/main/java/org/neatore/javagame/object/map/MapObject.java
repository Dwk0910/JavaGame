package org.neatore.javagame.object.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import org.neatore.javagame.object.story.Scene;

public abstract class MapObject {
    public float x, y, width, height;
    public Scene scene;

    public MapObject(Scene scene, GameMap map, String objName) {
        this.scene = scene;

        MapLayer systemLayer = map.map.getLayers().get("system");
        Array<RectangleMapObject> rectObjects = systemLayer.getObjects().getByType(RectangleMapObject.class);

        boolean set = false;
        for (int i = 0; i < rectObjects.size; i++) {
            RectangleMapObject rect = rectObjects.get(i);
            if (rect.getName() != null && rect.getName().equals(objName)) {
                Rectangle result = rect.getRectangle();
                this.x = result.x * map.unitScale;
                this.y = result.y * map.unitScale;
                this.width = result.width * map.unitScale;
                this.height = result.height * map.unitScale;
                set = true;
                break;
            }
        }

        if (!set) throw new IllegalArgumentException("Object '" + objName + "' not found");
    }

    public MapObject(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - 1, y, width + 1, height);
    }
}
