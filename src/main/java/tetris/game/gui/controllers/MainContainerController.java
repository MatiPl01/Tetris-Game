package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tetris.game.GameController;
import tetris.game.enums.GameMode;

public class MainContainerController {
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
    private Scene scene;

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

    public void init(Scene scene, int boardWidth, int boardHeight, GameMode gameMode, boolean spawnBombs) {
        this.scene = scene;

        GameController gameController = new GameController(
            boardWidth,
            boardHeight,
            gameMode,
            this,
            spawnBombs
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
