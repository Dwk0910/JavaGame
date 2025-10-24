package org.neatore.javagame.util.animation.transition;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.math.Matrix4;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.story.Scene;

import org.jetbrains.annotations.Nullable;

public class BasicTransition {
    /** speed of animation **/
    public float alphaAmount = 2f;

    private float alpha;
    private boolean transformed = false;
    private final Texture blackTexture;

    private Scene targetScene = null;
    private GameMap targetMap = null;

    private String targetObj = null;

    public BasicTransition(Color color) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void setTarget(@Nullable Scene targetScene, @Nullable GameMap targetMap) {
        this.transformed = false;
        this.targetScene = targetScene;
        this.targetMap = targetMap;
    }

    public void setTargetObj(@Nullable String obj) {
        this.targetObj = obj;
    }

    public void draw(float delta, SpriteBatch batch) {
        // 무시 : targetMap 과 targetScene이 null이거나, 작업이 모두 완료되어 alpha가 0보다 작거나 같을 때
        if ((targetMap == null && targetScene == null) || (transformed && alpha <= 0)) {
            if (targetScene != null || targetMap != null) {
                targetScene = null;
                targetMap = null;
            }
            if (!JavaGame.player.canMove) JavaGame.player.canMove = true;
            return;
        }

        // black fade-in
        batch.setColor(1, 1, 1, alpha);
        Matrix4 projMatrix = batch.getProjectionMatrix();
        batch.setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // restore
        batch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(projMatrix);
        batch.setColor(Color.WHITE);

        if (!transformed) {
            if (JavaGame.player.canMove) {
                JavaGame.player.canMove = false;
                JavaGame.player.isMoving = false;
            }
        }

        // edit alpha
        if (!transformed) {
            if (alpha > 1) {
                if (targetScene != null) JavaGame.changeSceneDirectly(targetScene);
                else JavaGame.scene.changeMapDirectly(targetMap, targetObj);
                transformed = true;
            } else alpha += (alphaAmount * delta);
        } else alpha -= (alphaAmount * delta);
    }

    public void dispose() {
        blackTexture.dispose();
    }
}
