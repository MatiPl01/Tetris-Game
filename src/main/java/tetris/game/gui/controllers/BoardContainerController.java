package tetris.game.gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import tetris.game.GameController;
import tetris.game.enums.EventSource;
import tetris.game.enums.EventType;
import tetris.game.enums.GameState;
import tetris.game.gui.events.MoveEvent;

public class BoardContainerController {
    private Scene scene;
    private Timeline timeline;

    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 10;  // TODO - allow user to set this on game startup
    private static final int GRID_HEIGHT = 20; // TODO - allow user to set this on game startup

    @FXML
    private GridPane grid;
    private GameController gameController;

    @FXML
    private void initialize() {
        buildGrid();
        init();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        setupKeyboardEvents();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private void buildGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) grid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        for (int i = 0; i < GRID_WIDTH; i++)  grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        grid.setGridLinesVisible(true);
    }

    private void init() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(400),
                actionEvent -> moveBrickDown(new MoveEvent(EventSource.COMPUTER, EventType.DOWN))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void setupKeyboardEvents() {
        scene.setOnKeyPressed(event -> {
            if (gameController.getGameState() == GameState.RUNNING) {
                switch (event.getCode()) {
                    case UP, W -> {
                        rotateBrick(new MoveEvent(EventSource.PLAYER, EventType.ROTATE));
                        event.consume();
                    }
                    case LEFT, A -> {
                        moveBrickLeft(new MoveEvent(EventSource.PLAYER, EventType.LEFT));
                        event.consume();
                    }
                    case RIGHT, D -> {
                        moveBrickRight(new MoveEvent(EventSource.PLAYER, EventType.RIGHT));
                        event.consume();
                    }
                    case DOWN, S -> {
                        moveBrickDown(new MoveEvent(EventSource.PLAYER, EventType.DOWN));
                        event.consume();
                    }
                    case SPACE -> {
                        placeBrick();
                        event.consume();
                    }
                }
            }
        });
    }

    private void rotateBrick(MoveEvent event) {
        System.out.println("rotateBrick: " + event);
    }

    private void moveBrickLeft(MoveEvent event) {
        System.out.println("moveBrickLeft: " + event);
    }

    private void moveBrickRight(MoveEvent event) {
        System.out.println("moveBrickRight: " + event);
    }

    private void moveBrickDown(MoveEvent event) {
        System.out.println(event);
    }

    private void placeBrick() {
        System.out.println("placeBrick");
    }
}
