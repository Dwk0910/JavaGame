package org.neatore.javagame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// ** SCENE **
import org.neatore.javagame.character.Player;
import org.neatore.javagame.object.character.Direction;
import org.neatore.javagame.object.story.Scene;
import org.neatore.javagame.scene.Scene01;
import org.neatore.javagame.util.animation.transition.BasicTransition;

public class JavaGame extends ApplicationAdapter {
    public static JavaGame instance;
    public static JavaGame getInstance() {
        return instance;
    }

    public static Scene scene;

    // Essential Classes

    public SpriteBatch batch;
    public static OrthographicCamera camera;
    public static FitViewport viewport;

    // Asset Classes
    public static Player player;

    // User Interface
    public Stage uiStage;
    public Skin uiSkin;

    // Scene transitions
    public static BasicTransition transition;

    @Override
    public void create() {
        // **PUBLIC RESOURCES**
        instance = this;
        batch = new SpriteBatch();

        // Scene Transitions
        transition = new BasicTransition(Color.BLACK);

        // ** BINDING **
        // player binding
        player = new Player();
        player.direction = Direction.RIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(30, 20, camera);

        // initial scene setting
        changeSceneDirectly(new Scene01());

        // Orthographic Camera (직교 투영 카메라) 이므로 z좌표는 무시해도 됨.
        camera.position.set(scene.map.width / 2, scene.map.height / 2, 0);
        camera.zoom -= 0.3f;
        camera.update();

        viewport.apply();

        // User Interface
        uiStage = new Stage(new ScreenViewport(), batch);
        uiSkin = new Skin(Gdx.files.internal("uiskin/uiskin.json"));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        update();
        player.render(batch);
        scene.render(batch);
        transition.draw(Gdx.graphics.getDeltaTime(), batch);

        batch.end();

        // User Interface
        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();
    }

    public static void changeSceneDirectly(Scene newScene) {
        scene = newScene;
        scene.map.initializePlayerPosition();
    }

    public static void changeScene(Scene newScene) {
        transition.setTarget(newScene, null);
    }

    private void update() {
        float delta = Gdx.graphics.getDeltaTime();
        player.update(delta, scene.map);
    }

    @Override
    public void dispose() {
        scene.dispose();
        player.dispose();
        batch.dispose();
        uiStage.dispose();
        uiSkin.dispose();
        transition.dispose();
    }
}
