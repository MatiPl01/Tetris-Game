package tetris.game.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tetris.game.GameController;
import tetris.game.gui.controllers.MainContainerController;

import java.io.FileInputStream;
import java.util.Objects;

public class App extends Application {
    private static final String WINDOW_TITLE = "Tetris";
    private static final String ICON_PATH = "src/main/resources/images/icon.png";
    private static final String STYLESHEET_PATH = "/css/style.css";

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the initial window
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/MainContainer.fxml"));
        AnchorPane mainContainer = loader.load();
        Scene scene = new Scene(mainContainer);

        // Pass the current scene to the MainContainerController
        MainContainerController mainContainerController = loader.getController();
        mainContainerController.init(scene);
        
        // Load stylesheet file
        String css = Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm();
        scene.getStylesheets().add(css);

        // Add an icon
        Image icon = new Image(new FileInputStream(ICON_PATH));
        primaryStage.getIcons().add(icon);

        // Set the icon and the window title
        primaryStage.setScene(scene);
        primaryStage.setTitle(WINDOW_TITLE);
        primaryStage.show();
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
