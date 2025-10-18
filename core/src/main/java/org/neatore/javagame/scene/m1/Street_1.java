package org.neatore.javagame.scene.m1;

import com.badlogic.gdx.graphics.Texture;

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

        // ** Interactive Object && Impassable Object 등록
        // 실제 좌표 (Tiled 맵 좌표)로 저장
        // x, y, width, height
        objects.add(new Go(scene, this, "teleport", () -> new Street_2(scene)));

        // Add NPCs
        JavaGame.NPCs.add(new TestNPC(this, 10, 20));
    }
}

class TestNPC extends NPC {
    public TestNPC(GameMap map, float x, float y) {
        super(map, x, y);
        JavaGame game = JavaGame.getInstance();
        spriteSheet = game.asset.get("player/player.png", Texture.class);
        FRAME_WIDTH = 16;
        FRAME_HEIGHT = 16;
        WIDTH = 1f;
        HEIGHT = 1f;
        init();
    }
}
