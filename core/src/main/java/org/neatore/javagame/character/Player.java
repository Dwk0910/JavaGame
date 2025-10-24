package org.neatore.javagame.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import org.neatore.javagame.JavaGame;
import org.neatore.javagame.object.character.CharacterAnimation;
import org.neatore.javagame.object.character.Direction;

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

        List<CharacterAnimation> animations = CharacterAnimation.autoGenerate(spriteSheet, FRAME_WIDTH, FRAME_HEIGHT, frameTerm);
        static_down_animation = animations.get(0);
        static_right_animation = animations.get(1);
        static_left_animation = animations.get(2);
        static_up_animation = animations.get(3);
        moving_down_animation = animations.get(4);
        moving_right_animation = animations.get(5);
        moving_left_animation = animations.get(6);
        moving_up_animation = animations.get(7);
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
