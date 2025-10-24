package org.neatore.javagame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

// Character
import org.neatore.javagame.character.Character;
import org.neatore.javagame.character.NPC;
import org.neatore.javagame.character.Player;
import org.neatore.javagame.object.character.Direction;

// ** SCENE **
import org.neatore.javagame.object.map.Renderable;
import org.neatore.javagame.object.story.Scene;
import org.neatore.javagame.scene.Scene01;

// Utility
import org.neatore.javagame.util.animation.transition.BasicTransition;

import java.util.ArrayList;
import java.util.List;

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
    public static List<NPC> NPCs;

    // User Interface
    public Stage uiStage;
    public Skin uiSkin;

    // Utility
    public static BasicTransition transition;
    public AssetManager asset;

    @Override
    public void create() {
        // **PUBLIC RESOURCES**
        instance = this;
        batch = new SpriteBatch();

        // Utility
        transition = new BasicTransition(Color.BLACK);
        asset = new AssetManager();

        // Load assets
        asset.load("player/player.png", Texture.class);
        while (!asset.update()) {
            float progress = asset.getProgress() * 100;
            System.out.println("Loading assets..." + progress + "%");
        }

        // ** BINDING **
        // character binding
        player = new Player();
        player.direction = Direction.RIGHT;

        NPCs = new ArrayList<>();

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

        // UPDATE
        update();

        // Renderable Assets
        // 에셋들의 y좌표를 정렬, 겹치게 렌더링해서 원근감 표현 (가까우면 나중에 렌더링, 멀면 먼저 렌더링)
        List<Renderable> renderables = new ArrayList<>();
        renderables.add(player);
        renderables.addAll(NPCs);

        // Add map objects
        scene.map.objects.forEach((obj) -> {
            if (obj instanceof Renderable casted) {
                renderables.add(casted);
            }
        });

        renderables.sort((a, b) -> Float.compare(b.getY(), a.getY()));
        for (Renderable target : renderables) {
            target.render(batch);
        }

        scene.render(batch);
        transition.draw(Gdx.graphics.getDeltaTime(), batch);

        batch.end();

        // User Interface
        uiStage.act(Gdx.graphics.getDeltaTime());
        uiStage.draw();
    }

    public static void changeSceneDirectly(Scene newScene) {
        scene = newScene;
        scene.map.initializePlayerPosition(null);
    }

    public static void changeScene(Scene newScene) {
        transition.setTarget(newScene, null);
    }

    private void update() {
        // Characters
        List<Character> characters = new ArrayList<>();
        characters.add(player);
        characters.addAll(NPCs);
        for (Character character : characters) {
            character.update();
        }
    }

    @Override
    public void dispose() {
        scene.dispose();
        player.dispose();
        batch.dispose();
        uiStage.dispose();
        uiSkin.dispose();
        transition.dispose();
        asset.dispose();
    }
}
