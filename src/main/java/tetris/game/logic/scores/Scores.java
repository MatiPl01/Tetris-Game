package tetris.game.logic.scores;

import javafx.application.Platform;
import tetris.game.gui.controllers.ScoreContainerController;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Scores {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SCORES_FILE_PATH = "src/main/resources/scores.csv";
    private static final String WRITE_ERROR_MESSAGE = "Error writing scores to a file";
    private static final String READ_ERROR_MESSAGE  = "Error reading scores from a file";
    private static final String DELIMITER = ",";
    private static final int MAX_SAVED_SCORES = 20;

    private static final List<ScoreRecord> prevScores = new ArrayList<>();

    private int score = 0;
    private ScoreRecord bestScore;
    private ScoreContainerController controller;

    public void setController(ScoreContainerController controller) {
        this.controller = controller;
    }

    public void show() {
        controller.displayBestScore(bestScore);
        controller.displayPrevScores(prevScores);
    }

    public int getScore() {
        return score;
    }

    public void add(int delta) {
        score += delta;
        updateDisplayedScore();
    }

    public void reset() {
        saveScores();
        resetScores();
        loadScores();
    }

    public void saveScores() {
        Platform.runLater(() -> {
            if (bestScore != null) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter(SCORES_FILE_PATH));
                    // Write previous scores
                    for (int i = (prevScores.size() == MAX_SAVED_SCORES ? 1 : 0); i < prevScores.size(); i++) {
                        bw.write(prevScores.get(i).toCSV(DELIMITER));
                    }
                    // Write the current score
                    bw.write(new ScoreRecord(score, DATE_FORMAT.format(new Date())).toCSV(DELIMITER));
                    // Write the max score
                    bw.write(bestScore.toCSV(DELIMITER));

                    bw.close();
                } catch (IOException e) {
                    System.out.println(WRITE_ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadScores() {
        Platform.runLater(() -> {
            prevScores.clear();
            try {
                BufferedReader br = new BufferedReader(new FileReader(SCORES_FILE_PATH));

                // Read previous scores
                String currLine = br.readLine();

                if (currLine != null) {
                    String nextLine = br.readLine();
                    do {
                        prevScores.add(parseScoresLine(currLine));
                        currLine = nextLine;
                        nextLine = br.readLine();
                    } while (nextLine != null);

                    // Read the max score
                    bestScore = parseScoresLine(currLine);
                } else {
                    bestScore = new ScoreRecord(0, DATE_FORMAT.format(new Date()));
                }

                br.close();
                show();
            } catch (FileNotFoundException e) {
                System.out.println("No previous scores are saved");
            } catch (IOException e) {
                System.out.println(READ_ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void resetScores() {
        Platform.runLater(() -> {
            score = 0;
            bestScore = null;
            updateDisplayedScore();
        });
    }

    private ScoreRecord parseScoresLine(String line) {
        String[] values = line.split(",");
        return new ScoreRecord(Integer.parseInt(values[0]), values[1]);
    }

    private void updateDisplayedScore() {
        Platform.runLater(() -> {
            controller.updateDisplayedScore(score);
            if (score > bestScore.score()) {
                bestScore = new ScoreRecord(score, DATE_FORMAT.format(new Date()));
                controller.displayBestScore(bestScore);
            }
        });
    }
}
