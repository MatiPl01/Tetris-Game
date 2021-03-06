package tetris.game.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;

public class App extends Application {
    private static final String WINDOW_TITLE = "Tetris";
    private static final String ICON_PATH = "src/main/resources/images/icon.png";

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the initial window
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/SettingsPopup.fxml"));
            AnchorPane settingsPopup = loader.load();
            Scene scene = new Scene(settingsPopup);

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
