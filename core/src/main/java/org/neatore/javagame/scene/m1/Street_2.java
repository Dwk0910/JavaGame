package org.neatore.javagame.scene.m1;

import org.neatore.javagame.JavaGame;

import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.obj.GoTo;
import org.neatore.javagame.object.story.Scene;

public class Street_2 extends GameMap {
    public Street_2(Scene scene) {
        super(scene, "map/scene01/street_2.tmx");
        setFollowCamera(JavaGame.player);
        objects.add(new GoTo(scene, this, "teleport", new Lobby(scene)));
    }
}
