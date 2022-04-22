package tetris.game.gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimeContainerController {
    private static final int REFRESH_INTERVAL = 10;
    private Timeline timeline;
    private long startTime = 0;

    @FXML
    private Label hoursLabel;

    @FXML
    private Label minutesLabel;

    @FXML
    private Label secondsLabel;

    @FXML
    private Label millisecondsLabel;

    @FXML
    private void initialize() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(REFRESH_INTERVAL),
                actionEvent -> updateTimer())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
        resumeTimer();
    }

    public void pauseTimer() {
        timeline.stop();
    }

    public void resumeTimer() {
        timeline.play();
    }

    private void updateTimer() {
        long elapsed = System.currentTimeMillis() - startTime;

        int milliseconds = (int)(elapsed % 1000);
        elapsed /= 1000;
        int seconds = (int)(elapsed % 60);
        elapsed /= 60;
        int minutes = (int)(elapsed % 60);
        elapsed /= 60;
        int hours = (int)(elapsed);

        hoursLabel.setText(String.valueOf(hours));
        minutesLabel.setText(String.format("%02d", minutes));
        secondsLabel.setText(String.format("%02d", seconds));
        millisecondsLabel.setText(String.format("%03d", milliseconds));
    }
}
