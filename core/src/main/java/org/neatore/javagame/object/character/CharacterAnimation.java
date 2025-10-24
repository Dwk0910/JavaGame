package org.neatore.javagame.object.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CharacterAnimation {
    private final List<TextureRegion> frames;

    private int lastIndex = 0;
    private long term;
    private long lastReturnedTime;

    public CharacterAnimation(List<TextureRegion> frames, long frameterm) {
        this.frames = frames;
        this.term = frameterm;
    }

    /*
    Auto-bindable Animation File :
    static_down (3 frames) OOO
    static_side[RIGHT] (3 frames) OOO
    static_up (3 frames)   OOO
    moving_down (3 frames) OOO
    moving_side[RIGHT] (3 frames) OOO
    moving_up (3 frames)   OOO
     */
    public static @NotNull CharacterAnimation generate(Texture spriteSheet, int y, int width, int height, long term) {
        List<TextureRegion> frames = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(spriteSheet, width * i, y, width, height));
        }
        return new CharacterAnimation(frames, term);
    }

    /**
     * return index <br/>
     * 0 : STATIC_DOWN <br/>
     * 1 : STATIC_RIGHT <br/>
     * 2 : STATIC_LEFT <br/>
     * 3 : STATIC_UP <br/>
     * 4 : MOVING_DOWN <br/>
     * 5 : MOVING_RIGHT <br/>
     * 6 : MOVING_LEFT <br/>
     * 7 : MOVING_UP
     */
    public static List<CharacterAnimation> autoGenerate(Texture spriteSheet, int width, int height, long term) {
        List<CharacterAnimation> result = new ArrayList<>();
        int c = 1;
        for (int y = 0; y <= 6 * height; y += height) {
            result.add(generate(spriteSheet, y, width, height, term));

            // STATIC_RIGHT, MOVING_RIGHT일 때, 좌우반전해서 LEFT도 생성
            if (c == 2 || c == 5) {
                // LEFT (Manual Generate)
                List<TextureRegion> frames = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    TextureRegion frame = new TextureRegion(spriteSheet, width * j, y, width, height);
                    frame.flip(true, false);
                    frames.add(frame);
                }
                result.add(new CharacterAnimation(frames, term));
            }
            c++;
        }
        return result;
    }

    /**
     * 애니메이션의 속도를 조절합니다
     * @param newTerm 프레임 교체 주기 (ms)
     */
    public void setTerm(long newTerm) {
        this.term = newTerm;
    }

    private TextureRegion getLastFrame() {
        // 기존 프레임 반환
        return frames.get(lastIndex);
    }

    private TextureRegion getNextFrame_(long current) {
        // 새로운 프레임 반환

        lastIndex++;
        if (lastIndex < frames.size()) {
            lastReturnedTime = current;
            return frames.get(lastIndex);
        } else {
            lastReturnedTime = current;
            lastIndex = 0;
            return frames.get(lastIndex);
        }
    }

    public TextureRegion getNextFrame(boolean ignoreTerm) {
        long current = System.currentTimeMillis();

        if (ignoreTerm || current - lastReturnedTime > term) return getNextFrame_(current);
        else return getLastFrame();
    }

}
