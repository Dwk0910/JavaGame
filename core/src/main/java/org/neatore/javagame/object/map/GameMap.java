package org.neatore.javagame.object.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Matrix4;
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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class GameMap {
    public boolean visible = true;

    public List<MapObject> objects = new ArrayList<>();
    private boolean followCamera = false;
    private boolean staticCamera = false;

    private float cOffsetX, cOffsetY;

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

        // setMapVisiblity()에서 사용
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        this.blackTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void setCameraOffset(float cameraOffsetX, float cameraOffsetY) {
        if (staticCamera) {
            this.cOffsetX = cameraOffsetX;
            this.cOffsetY = cameraOffsetY;

            OrthographicCamera camera = JavaGame.camera;
            camera.position.set(cameraOffsetX, cameraOffsetY, 0);
            camera.update();
        }
    }

    private Texture blackTexture;
    private boolean completed = false;
    private float alphaZ = 0;
    private float alphaO = 1;
    private final Matrix4 orthoMatrix = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private final Consumer<Boolean> visiblitychange_draw = (tOn) -> {
            if (blackTexture == null) return;
            JavaGame game = JavaGame.getInstance();
            game.batch.setColor(1, 1, 1, tOn ? alphaO : alphaZ);
            Matrix4 projMatrix = game.batch.getProjectionMatrix();
            game.batch.setProjectionMatrix(orthoMatrix);
            game.batch.draw(blackTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.batch.setProjectionMatrix(projMatrix);
            game.batch.setColor(Color.WHITE);
    };

    /** Set Visiblity with Fade In-Out Animation (You should call this method every frame) **/
    public void setMapVisiblity(boolean visiblity) {
        // Speed of animation
        float alphaAmount = 2f;

        if ((alphaO <= 0 && visiblity) || (alphaZ >= 1 && !visiblity)) completed = false;
        if (!completed) {
            if (visiblity) {
                // 페이드 인 애니메이션 구현을 위해서는 미리 맵이 켜져 있어야 함.
                this.visible = true;

                // OFF -> ON
                visiblitychange_draw.accept(true);
                alphaO -= (alphaAmount * Gdx.graphics.getDeltaTime());
                if (alphaO <= 0) completed = true;
            } else {
                // ON -> OFF
                visiblitychange_draw.accept(false);
                alphaZ += (alphaAmount * Gdx.graphics.getDeltaTime());
                if (alphaZ >= 1) {
                    this.visible = false;
                    completed = true;
                }
            }
        }
    }

    /** Move player to target position. If `to` is null, move player to map start position **/
    public void initializePlayerPosition(@Nullable String to) {
        final MapLayer systemLayer = map.getLayers().get("system");
        com.badlogic.gdx.maps.MapObject startPoint = systemLayer.getObjects().get(to == null ? "playerstart" : to);
        MapProperties properties = startPoint.getProperties();

        float x = properties.get("x", Float.class);
        float y = properties.get("y", Float.class);
        JavaGame.player.setLocation(x * unitScale - (JavaGame.player.WIDTH / 2f), y * unitScale);

        if (staticCamera) {
            OrthographicCamera camera = JavaGame.camera;
            camera.position.set(0, 0, 0);
            camera.update();
        } else {
            // Set camera position to player position
            JavaGame.camera.position.set(JavaGame.player.x + (JavaGame.player.WIDTH / 2), JavaGame.player.y, 0);
            JavaGame.camera.update();
        }
    }

    public void setFollowCamera(Character followTarget) {
        this.followTarget = followTarget;
        this.staticCamera = false;
        followCamera = true;
    }

    public void setStaticCamera() {
        this.followCamera = false;
        this.followTarget = null;
        this.staticCamera = true;
    }

    public void update() {
        OrthographicCamera camera = JavaGame.camera;
        if (followCamera && followTarget != null && camera != null) {
            float targetX = followTarget.x + (followTarget.WIDTH / 2);
            float targetY = followTarget.y + (followTarget.HEIGHT / 2);

            float viewportWidth = camera.viewportWidth * camera.zoom;
            float viewportHeight = camera.viewportHeight * camera.zoom;

            float minX = viewportWidth / 2f;
            float minY = viewportHeight / 2f;
            float maxX = width - viewportWidth / 2f;
            float maxY = height - viewportHeight / 2f;

            float cX = Math.max(minX, Math.min(targetX, maxX));
            float cY = Math.max(minY, Math.min(targetY, maxY));

            camera.position.set(cX, cY, 0);
            camera.update();
        } else if (camera != null) {
            camera.position.set(cOffsetX, cOffsetY, 0);
            camera.update();
        }
    }

    public void render(OrthographicCamera camera) {
        JavaGame game = JavaGame.getInstance();
        if (!visible) {
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
