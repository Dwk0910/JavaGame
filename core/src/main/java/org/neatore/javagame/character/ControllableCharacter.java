package org.neatore.javagame.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Vector2;

import org.neatore.javagame.object.map.GameMap;
import org.neatore.javagame.object.character.Direction;
import org.neatore.javagame.object.map.Impassable;
import org.neatore.javagame.object.map.Interactable;
import org.neatore.javagame.object.map.MapObject;

import java.util.List;

public abstract class ControllableCharacter extends Character {
    public Texture texture;

    public Direction direction;
    public float offsetX, offsetY;

    public boolean canMove = true;
    public boolean isMoving = false;

    private final Vector2 velocity = new Vector2();

    public void dispose() {
        if (texture != null) texture.dispose();
    }

    public void update(float delta, GameMap map) {
        if (!canMove) return;

        float mapWidth = map.width, mapHeight = map.height;
        List<MapObject> objects = map.objects;

        velocity.set(0, 0);

        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);

        if (w && a) {
            direction = Direction.UP_LEFT;
            velocity.x -= 1 + offsetX;
            velocity.y += 1 + offsetY;
        } else if (w && d) {
            direction = Direction.UP_RIGHT;
            velocity.x += 1 + offsetX;
            velocity.y += 1 + offsetY;
        } else if (s && a) {
            direction = Direction.DOWN_LEFT;
            velocity.x -= 1 + offsetX;
            velocity.y -= 1 + offsetY;
        } else if (s && d) {
            direction = Direction.DOWN_RIGHT;
            velocity.x += 1 + offsetX;
            velocity.y -= 1 + offsetY;
        } else {
            if (w) {
                direction = Direction.UP;
                velocity.y += 1 + offsetY;
            }

            if (a) {
                direction = Direction.LEFT;
                velocity.x -= 1 + offsetX;
            }

            if (s) {
                direction = Direction.DOWN;
                velocity.y -= 1 + offsetY;
            }

            if (d) {
                direction = Direction.RIGHT;
                velocity.x += 1 + offsetX;
            }
        }

//        if (Gdx.input.isKeyPressed(Input.Keys.F)) System.out.println("x : " + x + ", y : " + y);

        boolean negative = false;
        float targetX = x + (velocity.x * speed * delta), targetY = y + (velocity.y * speed * delta);

        // 통과불가 오브젝트, 인터렉티브 오브젝트 검사
        for (MapObject object : objects) {
            if (object.getBounds().contains(targetX, targetY)) {
                if (object instanceof Interactable) ((Interactable) object).interact(this);
                if (object instanceof Impassable) {
                    negative = true;
                    break;
                }
            }
        }

        // 맵 경계 검사
        if ((targetX + 1 > mapWidth || targetY > mapHeight) || (targetX < 0 || targetY < 0))  {
            negative = true;
        }

        if (negative) velocity.set(0, 0);

        isMoving = !velocity.isZero();

        if (!isMoving && (direction == Direction.UP_LEFT || direction == Direction.UP_RIGHT)) direction = Direction.UP;
        if (!isMoving && (direction == Direction.DOWN_LEFT || direction == Direction.DOWN_RIGHT)) direction = Direction.DOWN;

        if (velocity.len2() > 0) velocity.nor();

        x += velocity.x * speed * delta;
        y += velocity.y * speed * delta;
    }
}
