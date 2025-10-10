package org.neatore.javagame.gui.label;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.neatore.javagame.JavaGame;

public class AreaLabel {
    private final String[] messageCharacterList;

    public float LETTER_SPACING = 3f;
    public float maintain = -5f;

    private int seq = 0;
    private float nextPos = 100;
    private final float seqTime;

    private boolean showed = false;
    public AreaLabel(String message) {
        this.messageCharacterList = message.split("");
        this.seqTime = .5f * message.length();
    }

    public void show() {
        if (!showed) {
            JavaGame game = JavaGame.getInstance();
            for (String i : messageCharacterList) {
                float appearDelay = seq / seqTime;
                float totalDuration = (seqTime - appearDelay) + maintain;

                Label label = new Label(i, game.uiSkin);
                label.setFontScale(1.05f, .9f);
                label.setPosition(nextPos, 70);
                label.addAction(Actions.sequence(
                    Actions.hide(),
                    Actions.delay(appearDelay),
                    Actions.show(),
                    Actions.delay(totalDuration),
                    Actions.fadeOut(.4f)
                ));
                game.uiStage.addActor(label);
                seq++;
                nextPos = label.getX() + label.getWidth() + LETTER_SPACING;
            }
            showed = true;
        }
    }
}
