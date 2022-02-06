package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tetris.game.config.Config;
import tetris.game.config.ConfigLoader;
import tetris.game.config.IntValueConfig;
import tetris.game.enums.GameMode;
import tetris.game.others.Dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.min;
import static java.lang.Integer.parseInt;

public class SettingsPopupController {
    private static final String CONFIG_JSON_PATH = "./src/main/resources/config.json";
    private static final String MAIN_SCENE_PATH = "/fxml/MainContainer.fxml";

    private final List<TextField> textFields = new ArrayList<>();
    private Config config;

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
        config = loadConfig();
        textFields.add(widthInput);
        textFields.add(heightInput);
        textFields.add(minRefreshIntervalInput);
        textFields.add(maxRefreshIntervalInput);
        setupDifficultyInput();
        setupInputFields();
    }

    @FXML
    private void onStart() {
        if (checkIfNotEmpty()) openMainScene();
        else {
            Dialog.informationDialog(
                "Tetris",
                "Invalid settings in Folding map form" ,
                "Found empty text fields");
        }
    }

    @FXML
    private void onClear() {
        textFields.forEach(field -> field.setText(""));
        difficultyInput.setValue(config.mode.def);
    }

    @FXML
    private void onReset() {
        fillWithDefaults();
    }

    private void setupInputFields() {
        fillWithDefaults();
        setupInputValidators();
    }

    private boolean checkIfNotEmpty() {
        for (TextField textField: textFields) {
            if (Objects.equals(textField.getText(), "")) return false;
        }
        return true;
    }

    private Config loadConfig() {
        try {
            return ConfigLoader.load(CONFIG_JSON_PATH);
        } catch (IOException e) {
            Dialog.informationDialog("Tetris", "Error", "Failed to load config file");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    private void setupDifficultyInput() {
        difficultyInput.getItems().addAll(config.mode.all);
    }

    private void fillWithDefaults() {
        widthInput.setText(String.valueOf(config.width.def));
        heightInput.setText(String.valueOf(config.height.def));
        minRefreshIntervalInput.setText(String.valueOf(config.minRefreshInterval.def));
        maxRefreshIntervalInput.setText(String.valueOf(config.maxRefreshInterval.def));
        difficultyInput.setValue(config.mode.def);
    }

    private void setupInputValidators() {
        setupIntValueValidator(widthInput, config.width);
        setupIntValueValidator(heightInput, config.height);
        setupIntValueValidator(minRefreshIntervalInput, config.minRefreshInterval);
        setupIntValueValidator(maxRefreshIntervalInput, config.maxRefreshInterval);

        minRefreshIntervalInput.textProperty().addListener((observable, oldValue, newValue) -> {
           if (parseInt(newValue) > parseInt(maxRefreshIntervalInput.getText())) {
               maxRefreshIntervalInput.setText(newValue);
           }
        });

        maxRefreshIntervalInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (parseInt(newValue) < parseInt(minRefreshIntervalInput.getText())) {
                minRefreshIntervalInput.setText(newValue);
            }
        });
    }

    private int validateInput(int value, IntValueConfig valueConfig) {
        if (value < valueConfig.min) return valueConfig.min;
        if (value > valueConfig.max) return valueConfig.max;
        return value;
    }

    private void setupIntValueValidator(TextField textField, IntValueConfig valueConfig) {
        int defaultValueLength = String.valueOf(valueConfig.def).length();
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue,"")) return;
            try {
                if (newValue.length() >= defaultValueLength) {
                    textField.setText(String.valueOf(validateInput(parseInt(newValue), valueConfig)));
                }
            } catch (NumberFormatException ignored) {
                textField.setText(oldValue);
            }
        });
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String inputText = textField.getText();
                if (Objects.equals(inputText, "")) inputText = String.valueOf(valueConfig.def);
                else inputText = String.valueOf(validateInput(parseInt(inputText), valueConfig));
                textField.setText(inputText);
            }
        });
    }

    private void openMainScene() {
        // Load main container
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(MAIN_SCENE_PATH));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set main container in the scene
        Stage stage = (Stage) widthInput.getScene().getWindow();
        assert anchorPane != null;
        Scene scene = new Scene(anchorPane);
        String css = Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.show();

        // Initialize main controller
        MainContainerController mainContainerController = loader.getController();
        initializeMainController(mainContainerController, scene);
    }

    private GameMode getDifficultyInput() {
        return switch (difficultyInput.getValue()) {
            case "hard" -> GameMode.HARD;
            case "normal" -> GameMode.NORMAL;
            default -> GameMode.NORMAL;
        };
    }

    private void initializeMainController(MainContainerController mainContainerController, Scene scene) {
        mainContainerController.init(
            scene,
            parseInt(widthInput.getText()),
            parseInt(heightInput.getText()),
            getDifficultyInput(),
            spawnBombsCheckbox.isSelected()
        );
    }
}
