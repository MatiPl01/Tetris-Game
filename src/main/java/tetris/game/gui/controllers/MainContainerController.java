package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tetris.game.GameController;
import tetris.game.enums.GameMode;

import java.awt.*;

public class MainContainerController {
    private Scene scene;
    private Thread gameControllerThread;

    @FXML
    private BoardContainerController boardContainerController;

    @FXML
    private TimeContainerController timeContainerController;

    @FXML
    private NextBricksContainerController nextBricksContainerController;

    @FXML
    private ScoreContainerController scoreContainerController;

    @FXML
    private SettingsContainerController settingsContainerController;

    public BoardContainerController getBoardContainerController() {
        return boardContainerController;
    }

    public NextBricksContainerController getNextBricksContainerController() {
        return nextBricksContainerController;
    }

    public TimeContainerController getTimeContainerController() {
        return timeContainerController;
    }

    public ScoreContainerController getScoreContainerController() {
        return scoreContainerController;
    }

    public SettingsContainerController getSettingsContainerController() {
        return settingsContainerController;
    }

    public void init(Scene scene,
                     int boardWidth,
                     int boardHeight,
                     GameMode gameMode,
                     boolean spawnBombs,
                     int minRefreshInterval,
                     int maxRefreshInterval) {
        this.scene = scene;

        GameController gameController = new GameController(
            boardWidth,
            boardHeight,
            gameMode,
            this,
            spawnBombs,
            minRefreshInterval,
            maxRefreshInterval
        );

        gameControllerThread = new Thread(gameController);
        settingsContainerController.setGameController(gameController);
        boardContainerController.init(scene, boardWidth, boardHeight);
        gameControllerThread.start();
    }

    public void exitGame() {
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
