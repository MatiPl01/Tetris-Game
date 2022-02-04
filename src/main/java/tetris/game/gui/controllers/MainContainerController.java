package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;

public class MainContainerController {
    private Scene scene;

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
        System.out.println("MainContainerController Initialized"); // TODO - remove me
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        boardContainerController.setScene(scene);
    }
}
