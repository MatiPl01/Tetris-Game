package tetris.game.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.Objects;

public class App extends Application {
    private static final String WINDOW_TITLE = "Tetris";
    private static final String ICON_PATH = "src/main/resources/images/icon.png";
    private static final String STYLESHEET_PATH = "/css/style.css";

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the initial window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SettingsPopup.fxml"));
            AnchorPane settingsPopup = loader.load();
            Scene scene = new Scene(settingsPopup);
//            AnchorPane mainContainer = loader.load();
//            Scene scene = new Scene(mainContainer);

//            // Pass the current scene to the MainContainerController
//            MainContainerController mainContainerController = loader.getController();
//            mainContainerController.init(scene, 15, 30, GameMode.NORMAL);

            // Load stylesheet file
            String css = Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm();
            scene.getStylesheets().add(css);

            // Add an icon
            Image icon = new Image(new FileInputStream(ICON_PATH));
            primaryStage.getIcons().add(icon);

            // Disable window resizing
            primaryStage.setResizable(false);

            // Set the icon and the window title
            primaryStage.setScene(scene);
            primaryStage.setTitle(WINDOW_TITLE);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void init(String[] args) {
        Application.launch(App.class, args);
    }
}
