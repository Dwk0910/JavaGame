package org.neatore.javagame.object.map.obj;

import org.neatore.javagame.character.ControllableCharacter;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.Interactable;
import org.neatore.javagame.object.map.MapObject;
import org.neatore.javagame.object.story.Scene;

public class GoTo extends MapObject implements Interactable {
    private final Scene scene;
    private final GameMap target;
    public GoTo(Scene scene, GameMap currentMap, String objName, GameMap target) {
        super(scene, currentMap, objName);
        this.scene = scene;
        this.target = target;
    }

    @Override
    public void interact(ControllableCharacter context) {
        scene.changeMap(target);
    }
}
