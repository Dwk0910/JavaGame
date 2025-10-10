package org.neatore.javagame.object.character;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.List;

public class CharacterAnimation {
    private final List<TextureRegion> frames;

    private int lastIndex = 0;
    private long term;
    private long lastReturnedTime;

    public CharacterAnimation(List<TextureRegion> animation, long frameterm) {
        this.frames = animation;
        this.term = frameterm;
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
