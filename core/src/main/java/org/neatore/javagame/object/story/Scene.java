package org.neatore.javagame.object.story;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import org.jetbrains.annotations.Nullable;
import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.map.GameMap;

public abstract class Scene {
    public final FitViewport viewport;
    public GameMap map;

    public Scene(FitViewport viewport) {
        this.viewport = viewport;
    }

    public void render(SpriteBatch batch) {
        map.update();
        map.render(JavaGame.camera);
    }

    public void changeMapDirectly(GameMap map, @Nullable String targetObj) {
        if (this.map != null) this.map.dispose();
        this.map = map;
        map.initializePlayerPosition(targetObj);
        map.renderer.setView(JavaGame.camera);
    }

    public void changeMap(GameMap map) {
        JavaGame.transition.setTarget(null, map);
    }

    public void dispose() {
        map.dispose();
    }
}
