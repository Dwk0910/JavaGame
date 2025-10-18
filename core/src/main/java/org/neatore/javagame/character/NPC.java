package org.neatore.javagame.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.character.CharacterAnimation;
import org.neatore.javagame.object.character.Direction;
import org.neatore.javagame.object.map.GameMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class NPC extends Character {
    private TextureRegion texture;

    protected int FRAME_WIDTH, FRAME_HEIGHT;
    protected long frameTerm;
    protected Map<Direction, CharacterAnimation> static_animations, moving_animations;
    protected Texture spriteSheet;
    protected GameMap map;

    private Direction lastDirection;
    private boolean lastState;

    private boolean moveTask;
    private float moveX, moveY, moveSpeed;

    public NPC(GameMap map, float x, float y) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    protected void init() {
        if (FRAME_WIDTH == 0 || FRAME_HEIGHT == 0 || WIDTH == 0 || HEIGHT == 0 || spriteSheet == null) throw new NullPointerException("Some of the essential field (FRAME_WIDTH, FRAME_HEIGHT, WIDTH, HEIGHT, spriteSheet) are null.");

        static_animations = new HashMap<>();
        moving_animations = new HashMap<>();

        // Set-up animations
        direction = Direction.UP;
        frameTerm = 200L;

        List<CharacterAnimation> generated = CharacterAnimation.autoGenerate(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT, frameTerm);
        static_animations.put(Direction.DOWN, generated.get(0));
        static_animations.put(Direction.RIGHT, generated.get(1));
        static_animations.put(Direction.LEFT, generated.get(2));
        static_animations.put(Direction.UP, generated.get(3));
        moving_animations.put(Direction.DOWN, generated.get(4));
        moving_animations.put(Direction.RIGHT, generated.get(5));
        moving_animations.put(Direction.LEFT, generated.get(6));
        moving_animations.put(Direction.UP, generated.get(7));
    }

    public void replaceMap(GameMap map) {
        this.map = map;
    }

    public void replace(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void move(float x, float y) {
        this.moveTask = true;
        this.moveX = x;
        this.moveY = y;
        this.moveSpeed = this.speed;
    }

    public void move(float x, float y, float speed) {
        this.moveTask = true;
        this.moveX = x;
        this.moveY = y;
        this.moveSpeed = speed;
    }

    private void move_() {
        if (x != moveX) {
            this.isMoving = true;
            // 작으면 왼쪽을 바라보고, 크면 오른쪽을 바라봄
            if (moveX < x) this.direction = Direction.LEFT;
            else if (moveX > x) this.direction = Direction.RIGHT;
            this.x += moveX * moveSpeed;
        } else if (y != moveY) {
            this.isMoving = true;
            // 작으면 아래를 바라보고 크면 위를 바라봄
            if (moveY < y) this.direction = Direction.DOWN;
            else if (moveY > y) this.direction = Direction.UP;
            this.y += moveY * moveSpeed;
        } else {
            this.moveTask = false;
            this.isMoving = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (JavaGame.scene.map == this.map) batch.draw(texture, x, y, WIDTH, HEIGHT);
    }

    @Override
    public void update() {
        if (moveTask) move_();

        boolean diff;

        if (isMoving) {
            diff = direction != lastDirection || !lastState;
            texture = switch (direction) {
                case UP -> moving_animations.get(Direction.UP).getNextFrame(diff);
                case DOWN -> moving_animations.get(Direction.DOWN).getNextFrame(diff);
                case LEFT, UP_LEFT, DOWN_LEFT -> moving_animations.get(Direction.LEFT).getNextFrame(diff);
                case RIGHT, UP_RIGHT, DOWN_RIGHT -> moving_animations.get(Direction.RIGHT).getNextFrame(diff);
            };
        } else {
            diff = direction != lastDirection || lastState;
            texture = switch (direction) {
                case UP, UP_LEFT, UP_RIGHT -> static_animations.get(Direction.UP).getNextFrame(diff);
                case DOWN, DOWN_LEFT, DOWN_RIGHT -> static_animations.get(Direction.DOWN).getNextFrame(diff);
                case LEFT -> static_animations.get(Direction.LEFT).getNextFrame(diff);
                case RIGHT -> static_animations.get(Direction.RIGHT).getNextFrame(diff);
            };
        }
        lastState = isMoving;
        lastDirection = direction;
    }
}
