package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import tetris.game.GameController;
import tetris.game.enums.GameMode;

public class MainContainerController {
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

    @FXML
    private void initialize() {
        GameController gameController = new GameController(10, 20, GameMode.HARD, this); // TODO - make these params adjustable before game initialization
        settingsContainerController.setGameController(gameController);
    }

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

    public void init(Scene scene, int boardWidth, int boardHeight) {
        boardContainerController.init(scene, boardWidth, boardHeight);
    }
}
