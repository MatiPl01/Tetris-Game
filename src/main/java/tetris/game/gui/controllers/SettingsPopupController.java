package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class SettingsPopupController {
    @FXML
    private TextField widthInput;

    @FXML
    private TextField heightInput;

    @FXML
    private TextField minRefreshIntervalInput;

    @FXML
    private TextField maxRefreshIntervalInput;

    @FXML
    private ComboBox<String> difficultyInput;

    @FXML
    private CheckBox spawnBombsCheckbox;

    @FXML
    private void initialize() {
        // TODO - laod config
    }

    @FXML
    private void onStart() {
        // TODO
    }

    @FXML
    private void onClear() {
        // TODO
    }

    @FXML
    private void onReset() {
        // TODO
    }

    @FXML
    private void onSpawnBombsCheck() {
        // TODO
    }

    @FXML
    private void onDifficultyChange() {
        // TODO
    }
}
