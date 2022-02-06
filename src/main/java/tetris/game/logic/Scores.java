package tetris.game.logic;

import tetris.game.gui.controllers.ScoreContainerController;

// TODO - add max score and previous scores (load from a file)
public class Scores {
    private int score = 0;
    private ScoreContainerController controller;

    public void setController(ScoreContainerController controller) {
        this.controller = controller;
    }

    public int getScore() {
        return score;
    }

    public void add(int delta) {
        score += delta;
        controller.updateCurrentScore(score);
    }

    public void reset() {
        score = 0;
        controller.updateCurrentScore(score);
    }
}
