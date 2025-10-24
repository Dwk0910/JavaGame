package org.neatore.javagame.object.map.obj;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.character.ControllableCharacter;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.Interactable;
import org.neatore.javagame.object.map.MapObject;
import org.neatore.javagame.object.story.Scene;

import java.util.function.Supplier;

public class GoTo extends MapObject implements Interactable {
    private final Supplier<GameMap> targetMap;
    private final String targetObj;

    public GoTo(Scene scene, GameMap map, String objName, Supplier<GameMap> targetMap, String targetObj) {
        super(scene, map, objName);
        this.targetMap = targetMap;
        this.targetObj = targetObj;
    }

    @Override
    public void interact(ControllableCharacter context) {
        scene.changeMap(targetMap.get());
        JavaGame.transition.setTargetObj(targetObj);
    }
}
