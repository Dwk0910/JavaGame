package org.neatore.javagame.scene.m1;

import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.obj.GoTo;
import org.neatore.javagame.object.story.Scene;

public class Lobby extends GameMap {
    public Lobby(Scene scene) {
        super(scene, "map/scene01/lobby.tmx");
        setStaticCamera();
        setCameraOffset(7, 5);
        objects.add(new GoTo(scene, this, "teleport2", () -> new Storage(scene)));
    }
}
