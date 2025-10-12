package org.neatore.javagame.scene.m1;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.character.NPC;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.map.obj.Go;
import org.neatore.javagame.object.story.Scene;

public class Street_1 extends GameMap {
    public Street_1(Scene scene) {
        super(scene, "map/scene01/street_1.tmx");

        scene.map = this;
        setFollowCamera(JavaGame.player);

        // ** Interative Object && Impassable Object 등록
        // 실제 좌표 (Tiled 맵 좌표)로 저장
        // x, y, width, height
        objects.add(new Go(scene, this, "teleport", () -> new Street_2(scene)));

        // Add NPCs
        JavaGame.NPCs.add(new NPC(this, 10, 20));
    }
}
