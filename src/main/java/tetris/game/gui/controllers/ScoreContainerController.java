package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ScoreContainerController {
    @FXML
    private Label currentScoreLabel;

    @FXML
    private Label bestScoreLabel;

    @FXML
    private void initialize() {
        // TODO - add loading scores from a file
    }

    public void updateCurrentScore(int score) {
        currentScoreLabel.setText(String.valueOf(score));
    }
}
