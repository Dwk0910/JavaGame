package org.neatore.javagame.util;

import org.jetbrains.annotations.Nullable;

import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.MapObject;

public class MapUtil {
    public static @Nullable MapObject getMapObj(GameMap map, float x, float y) {
        for (MapObject obj : map.objects) {
            if (obj.getBounds().contains(x, y)) return obj;
        }

        return null;
    }
}
