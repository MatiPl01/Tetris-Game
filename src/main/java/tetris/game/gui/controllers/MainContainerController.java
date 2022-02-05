package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import tetris.game.GameController;
import tetris.game.enums.GameMode;

public class MainContainerController {
    private Scene scene;
    private GameController gameController;

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
        gameController = new GameController(10, 20, GameMode.NORMAL, this); // TODO - make these params adjustable before game initialization
    }

    public BoardContainerController getBoardContainerController() {
        return boardContainerController;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        boardContainerController.setScene(scene);
    }
}
