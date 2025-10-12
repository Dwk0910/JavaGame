package org.neatore.javagame.scene.m1;

import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.obj.GoTo;
import org.neatore.javagame.object.story.Scene;

public class Storage extends GameMap {
    public Storage(Scene scene) {
        super(scene, "map/scene01/storage.tmx");
        setStaticCamera();
        setCameraOffset(7, 5);
        objects.add(new GoTo(scene, this, "teleport", () -> new Lobby(scene), "from_storage"));
    }
}
