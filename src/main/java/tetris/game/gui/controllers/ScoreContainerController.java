package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import tetris.game.logic.scores.ScoreRecord;

import java.util.List;

public class ScoreContainerController {
    @FXML
    private Label currScoreLabel;

    @FXML
    private Label bestScoreValueLabel;

    @FXML
    private Label bestScoreDateLabel;

    @FXML
    private VBox prevScoresListVBox;

    @FXML
    private Label prevScoresCountLabel;

    public void updateDisplayedScore(int score) {
        currScoreLabel.setText(String.valueOf(score));
    }

    public void displayBestScore(ScoreRecord bestScore) {
        bestScoreValueLabel.setText(String.valueOf(bestScore.score()));
        bestScoreDateLabel.setText(bestScore.date());
    }

    public void displayPrevScores(List<ScoreRecord> prevScores) {
        prevScoresListVBox.getChildren().clear();
        prevScoresCountLabel.setText(String.valueOf(prevScores.size()));

        for (int i = prevScores.size() - 1; i >= 0; i--) {
            ScoreRecord score = prevScores.get(i);
            HBox hbox = new HBox();
            Label scoreLabel = new Label(String.valueOf(score.score()));
            Label dateLabel = new Label(score.date());
            Region region = new Region();
            HBox.setHgrow(region, Priority.ALWAYS);
            hbox.getChildren().addAll(scoreLabel, region, dateLabel);
            prevScoresListVBox.getChildren().add(hbox);

            if (i > 0) {
                prevScoresListVBox.getChildren().add(new Separator());
            }
        }
    }
}
