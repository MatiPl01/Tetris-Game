package tetris.game.gui.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import tetris.game.GameController;
import tetris.game.enums.EventSource;
import tetris.game.enums.EventType;
import tetris.game.enums.GameState;
import tetris.game.gui.events.MoveEvent;
import tetris.game.gui.layout.Grid;
import tetris.game.logic.Board;

public class BoardContainerController {
    private static final int REFRESH_INTERVAL = 400;
    private static final int WINDOW_HEIGHT = 690;

    private Grid grid;
    private Scene scene;
    private Timeline timeline;
    private GameController gameController;

    @FXML
    private GridPane gridPane;

    public void init(Scene scene, int boardWidth, int boardHeight) {
        this.scene = scene;
        grid = new Grid(gridPane, boardWidth, boardHeight, -1, WINDOW_HEIGHT);
        setupKeyboardEvents();
        gameController.newGame();
    }

    public void pauseAnimation() {
        if (timeline != null) timeline.stop();
    }

    public void startAnimation() {
        if (timeline == null) {
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(REFRESH_INTERVAL),
                    actionEvent -> {
                        if (gameController.getGameState() == GameState.RUNNING) {
                            gridPane.requestFocus();
                            gameController.handleMoveEvent(new MoveEvent(EventSource.COMPUTER, EventType.DOWN));
                        } else {
                            timeline.stop();
                        }
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
        }
        timeline.play();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void requestAnimationFrame(Board board) {
        int[][] matrix = board.getBoardMatrix();
        int height = matrix.length;
        int width = matrix[0].length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.get(x, y).getChildren().clear();
                if (matrix[y][x] == 0) continue;

                Rectangle rectangle = new Rectangle(grid.getCellWidth(), grid.getCellHeight());

                // Display the current brick
                if (matrix[y][x] > 0) {
                    rectangle.setFill(board.getBrickColor(matrix[y][x]));
                // Display a shadow of the current brick
                } else {
                    rectangle.setFill(board.getBrickColor(-matrix[y][x]));
                    rectangle.setOpacity(.2);
                }

                grid.get(x, y).getChildren().add(rectangle);
            }
        }
    }

    private void setupKeyboardEvents() {
        scene.setOnKeyPressed(event -> {
            if (gameController.getGameState() == GameState.RUNNING) {
                switch (event.getCode()) {
                    case UP, W -> {
                        gameController.handleMoveEvent(new MoveEvent(EventSource.PLAYER, EventType.ROTATE));
                        event.consume();
                    }
                    case LEFT, A -> {
                        gameController.handleMoveEvent(new MoveEvent(EventSource.PLAYER, EventType.LEFT));
                        event.consume();
                    }
                    case RIGHT, D -> {
                        gameController.handleMoveEvent(new MoveEvent(EventSource.PLAYER, EventType.RIGHT));
                        event.consume();
                    }
                    case DOWN, S -> {
                        gameController.handleMoveEvent(new MoveEvent(EventSource.PLAYER, EventType.DOWN));
                        event.consume();
                    }
                    case SPACE -> {
                        gameController.handleMoveEvent(new MoveEvent(EventSource.PLAYER, EventType.PLACE));
                        event.consume();
                    }
                }
            }
        });
    }
}
