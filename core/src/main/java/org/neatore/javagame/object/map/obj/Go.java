package org.neatore.javagame.object.map.obj;

import org.neatore.javagame.character.ControllableCharacter;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.Interactable;
import org.neatore.javagame.object.map.MapObject;
import org.neatore.javagame.object.story.Scene;

import java.util.function.Supplier;

public class Go extends MapObject implements Interactable {
    private final Scene scene;
    private final Supplier<GameMap> target;
    public Go(Scene scene, GameMap currentMap, String objName, Supplier<GameMap> target) {
        super(scene, currentMap, objName);
        this.scene = scene;
        this.target = target;
    }

    @Override
    public void interact(ControllableCharacter context) {
        GameMap map = target.get();
        scene.changeMap(map);
    }
}
