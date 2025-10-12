package org.neatore.javagame.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.map.GameMap;

public class NPC extends Character {
    private final TextureRegion texture;
    private GameMap map;

    public NPC(GameMap map, float x, float y) {
        JavaGame game = JavaGame.getInstance();
        texture = new TextureRegion(game.asset.get("player/player.png", Texture.class), 0, 16, 16, 16);
        WIDTH = 1f;
        HEIGHT = 1f;
        this.x = x;
        this.y = y;
        this.map = map;
    }

    public void replaceMap(GameMap map) {
        this.map = map;
    }

    public void replace(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(float x, float y) {}

    @Override
    public void render(SpriteBatch batch) {
        if (JavaGame.scene.map == this.map) batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    @Override
    public void update() {}
}
