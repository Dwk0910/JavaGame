package org.neatore.javagame.object.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.character.Character;
import org.neatore.javagame.object.map.obj.Block;
import org.neatore.javagame.object.story.Scene;

import java.util.ArrayList;
import java.util.List;

public abstract class GameMap {
    public boolean visible = true;

    public List<MapObject> objects = new ArrayList<>();
    public boolean followCamera = false;

    public Character followTarget;

    public Scene scene;
    public TiledMap map;
    public OrthogonalTiledMapRenderer renderer;

    public float unitScale;
    public float width;
    public float height;

    /**
     *
     * @param internalPath : tmx 파일 위치 (internal)
     */
    public GameMap(Scene scene, String internalPath) {
        this.scene = scene;
        this.map = new TmxMapLoader().load(internalPath);

        Integer tileWidth = map.getProperties().get("tilewidth", Integer.class); // height도 똑같음
        Integer mapWidth = map.getProperties().get("width", Integer.class);
        Integer mapHeight = map.getProperties().get("height", Integer.class);

        this.width = mapWidth;
        this.height = mapHeight;

        // 렌더러는 타일맵을 가져올 때 tmx 파일에 설정된 타일 크기와 unit scale을 곱해서 World Unit으로 변환합니다.
        // 예를 들어, tmx 파일에 타일 크기가 32px x 32px로 설정되어 있다고 가정합시다. (example.tmx의 경우 실제로 1타일의 크기가 32px x 32px입니다.)
        // 이 32px x 32px 크기가 unit scale과 곱해서 1이 되려면 즉, 1 World Unit = 32px * (unit scale)이 되려면
        // unit scale이 1/32f가 되어야 합니다.

        // 결론적으로, unit scale을 설정할 때에는, tmx 파일의 타일 크기를 확인하고 그 타일 크기와 곱해서 1이 되는 값을 설정해야 합니다.
        // unit scale = 1 / (타일 크기(px))
        this.unitScale = 1f / tileWidth;

        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        MapLayer systemLayer = map.getLayers().get("system");

        if (systemLayer != null) {
            Array<RectangleMapObject> rectObjects = systemLayer.getObjects().getByType(RectangleMapObject.class);
            List<Rectangle> blocks = new ArrayList<>();
            for (int i = 0; i < rectObjects.size; i++) {
                RectangleMapObject rectObj = rectObjects.get(i);
                if (rectObj.getName() != null && rectObj.getName().equals("block")) blocks.add(rectObj.getRectangle());
            }

            // 단위가 px이므로 unitScale을 곱해서 worldunit으로 변환 후 objects에 등록
            for (Rectangle rect : blocks) {
                float px = rect.x;
                float py = rect.y;
                float pw = rect.width;
                float ph = rect.height;
                objects.add(new Block(px * unitScale, py * unitScale, pw * unitScale, ph * unitScale));
            }
        }
    }

    public void initializePlayerPosition() {
        MapLayer systemLayer = map.getLayers().get("system");
        com.badlogic.gdx.maps.MapObject startPoint = systemLayer.getObjects().get("playerstart");
        MapProperties properties = startPoint.getProperties();

        float x = properties.get("x", Float.class);
        float y = properties.get("y", Float.class);
        JavaGame.player.setLocation(x * unitScale, y * unitScale);

        // Set camera position to player position
        JavaGame.camera.position.set(JavaGame.player.x + (JavaGame.player.WIDTH / 2), JavaGame.player.y, 0);
        JavaGame.camera.update();
    }

    public void setFollowCamera(Character followTarget) {
        this.followTarget = followTarget;
        followCamera = true;
    }

    public void update() {
        OrthographicCamera camera = JavaGame.camera;
        if (followCamera && followTarget != null && camera != null) {
            camera.position.set(followTarget.x + (followTarget.WIDTH / 2), followTarget.y, 0);
            camera.update();
        }
    }

    public void render(OrthographicCamera camera) {
        if (!visible) {
            JavaGame game = JavaGame.getInstance();

            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.BLACK);
            pixmap.fill();
            Texture blackTexture = new Texture(pixmap);
            pixmap.dispose();

            game.batch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            renderer.setView(camera);
            renderer.render();
        }
    }
    public void dispose() {
        renderer.dispose();
        map.dispose();
    }
}
