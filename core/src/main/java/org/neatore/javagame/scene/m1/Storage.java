package org.neatore.javagame.scene.m1;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.story.Scene;

public class Storage extends GameMap {
    public Storage(Scene scene) {
        super(scene, "map/scene01/storage.tmx");
        setFollowCamera(JavaGame.player);
    }
}
