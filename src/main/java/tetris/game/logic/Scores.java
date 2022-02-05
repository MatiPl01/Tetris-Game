package tetris.game.logic;

// TODO - add max score or previous scores (load from file)
public class Scores {
    private int score = 0;

    public int getScore() {
        return score;
    }

    public void add(int delta) {
        score += delta;
    }

    public void reset() {
        score = 0;
    }
}
