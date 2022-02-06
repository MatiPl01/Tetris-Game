package tetris.game.logic.scores;

public record ScoreRecord(int score, String date) {
    public String toCSV(String delimiter) {
        return score + delimiter + date + "\n";
    }
}

