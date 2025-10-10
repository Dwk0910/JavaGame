package org.neatore.javagame.scene;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.gui.label.AreaLabel;
import org.neatore.javagame.object.story.Scene;
import org.neatore.javagame.scene.m1.Street_1;
import org.neatore.javagame.util.DelayedTask;

public class Scene01 extends Scene {
    private AreaLabel areaLabel;
    private final DelayedTask showMapTask;

    public Scene01() {
        super(JavaGame.viewport);
        // initial map
        map = new Street_1(this);
        map.visible = false;

        // Scene instances
        this.areaLabel = new AreaLabel("1903년 3월 12일, 한성부 종로 일대");
        this.showMapTask = new DelayedTask(() -> map.visible = true);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        areaLabel.show();
        showMapTask.delayStart(5000L, false);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.areaLabel = null;
    }
}
