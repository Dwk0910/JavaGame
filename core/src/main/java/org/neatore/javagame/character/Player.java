package org.neatore.javagame.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.character.CharacterAnimation;
import org.neatore.javagame.object.character.Direction;

import java.util.ArrayList;
import java.util.List;

public class Player extends ControllableCharacter {
    private final Texture spriteSheet;

    private TextureRegion frame;

    public long frameTerm;
    private boolean lastState;
    private Direction lastDirection;

    // Animations
    private final CharacterAnimation static_up_animation;
    private final CharacterAnimation static_down_animation;
    private final CharacterAnimation static_right_animation;
    private final CharacterAnimation static_left_animation;

    private final CharacterAnimation moving_up_animation;
    private final CharacterAnimation moving_down_animation;
    private final CharacterAnimation moving_right_animation;
    private final CharacterAnimation moving_left_animation;

    // Size for each sprite frame (unit: px)
    private static final int FRAME_WIDTH = 16, FRAME_HEIGHT = 16;

    public Player() {
        JavaGame game = JavaGame.getInstance();
        WIDTH  = 1f;
        HEIGHT = 1f;

        spriteSheet = game.asset.get("player/player.png", Texture.class);

        speed = 3.5f;

        // Set-up animations
        direction = Direction.UP;
        frameTerm = 250L;

        List<TextureRegion> ani_up_static = new ArrayList<>();
        List<TextureRegion> ani_down_static = new ArrayList<>();
        List<TextureRegion> ani_right_static = new ArrayList<>();
        List<TextureRegion> ani_left_static = new ArrayList<>();

        List<TextureRegion> ani_up_moving = new ArrayList<>();
        List<TextureRegion> ani_down_moving = new ArrayList<>();
        List<TextureRegion> ani_right_moving = new ArrayList<>();
        List<TextureRegion> ani_left_moving = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ani_up_static.add(new TextureRegion(spriteSheet, FRAME_WIDTH * i, FRAME_HEIGHT * 2, FRAME_WIDTH, FRAME_HEIGHT));
            ani_up_moving.add(new TextureRegion(spriteSheet, FRAME_WIDTH * i, FRAME_HEIGHT * 5, FRAME_WIDTH, FRAME_HEIGHT));
            ani_down_static.add(new TextureRegion(spriteSheet, FRAME_WIDTH * i, 0, FRAME_WIDTH, FRAME_HEIGHT));
            ani_down_moving.add(new TextureRegion(spriteSheet, FRAME_WIDTH * i, FRAME_HEIGHT * 3, FRAME_WIDTH, FRAME_HEIGHT));

            TextureRegion right_static_frame = new TextureRegion(spriteSheet, FRAME_WIDTH * i, FRAME_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
            TextureRegion left_static_frame = new TextureRegion(right_static_frame);
            // Flip the right-sided frame
            left_static_frame.flip(true, false);

            ani_right_static.add(right_static_frame);
            ani_left_static.add(left_static_frame);

            TextureRegion right_moving_frame = new TextureRegion(spriteSheet, FRAME_WIDTH * i, FRAME_HEIGHT * 4, FRAME_WIDTH, FRAME_HEIGHT);
            TextureRegion left_moving_frame = new TextureRegion(right_moving_frame);
            // Flip the right-sided frame
            left_moving_frame.flip(true, false);

            ani_right_moving.add(right_moving_frame);
            ani_left_moving.add(left_moving_frame);
        }

        this.static_up_animation = new CharacterAnimation(ani_up_static, frameTerm);
        this.static_down_animation = new CharacterAnimation(ani_down_static, frameTerm);
        this.static_right_animation = new CharacterAnimation(ani_right_static, frameTerm);
        this.static_left_animation = new CharacterAnimation(ani_left_static, frameTerm);

        this.moving_up_animation = new CharacterAnimation(ani_up_moving, frameTerm);
        this.moving_down_animation = new CharacterAnimation(ani_down_moving, frameTerm);
        this.moving_right_animation = new CharacterAnimation(ani_right_moving, frameTerm);
        this.moving_left_animation = new CharacterAnimation(ani_left_moving, frameTerm);
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        super.update();

        boolean diff;

        if (isMoving) {
            diff = direction != lastDirection || !lastState;
            frame = switch (direction) {
                case UP -> moving_up_animation.getNextFrame(diff);
                case DOWN -> moving_down_animation.getNextFrame(diff);
                case LEFT, UP_LEFT, DOWN_LEFT -> moving_left_animation.getNextFrame(diff);
                case RIGHT, UP_RIGHT, DOWN_RIGHT -> moving_right_animation.getNextFrame(diff);
            };
        } else {
            diff = direction != lastDirection || lastState;
            frame = switch (direction) {
                case UP, UP_LEFT, UP_RIGHT -> static_up_animation.getNextFrame(diff);
                case DOWN, DOWN_LEFT, DOWN_RIGHT -> static_down_animation.getNextFrame(diff);
                case LEFT -> static_left_animation.getNextFrame(diff);
                case RIGHT -> static_right_animation.getNextFrame(diff);
            };
        }
        lastState = isMoving;
        lastDirection = direction;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(frame, x, y, WIDTH, HEIGHT);
    }

    @Override
    public void dispose() {
        spriteSheet.dispose();
    }
}
