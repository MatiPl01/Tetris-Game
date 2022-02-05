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
import tetris.game.logic.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardContainerController {
    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 10;  // TODO - allow user to set this on game startup
    private static final int GRID_HEIGHT = 20; // TODO - allow user to set this on game startup

    private Scene scene;
    private Timeline timeline;

    private final List<List<StackPane>> gridCells = new ArrayList<>();

    @FXML
    private GridPane grid;
    private GameController gameController;

    @FXML
    private void initialize() {
        buildGrid();
    }

    public void init(Scene scene) {
        this.scene = scene;
        setupKeyboardEvents();
        gameController.newGame();
        timeline = new Timeline(new KeyFrame(
                Duration.millis(400),
                actionEvent -> gameController.handleMoveEvent(new MoveEvent(EventSource.COMPUTER, EventType.DOWN))
        ));
        timeline.setCycleCount(Timeline.INDEFINITE);
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
                getCell(x, y).getChildren().clear();

                if (matrix[y][x] != 0) {
                    Rectangle rectangle = new Rectangle(CELL_SIZE, CELL_SIZE);
                    rectangle.setFill(board.getBrickColor(matrix[y][x]));
                    getCell(x, y).getChildren().add(rectangle);
                }
            }
        }
    }

    private void buildGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) grid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        for (int i = 0; i < GRID_WIDTH; i++)  grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
        grid.setGridLinesVisible(true);

        // Add grid cells (StackPanes) to make grid updates easier
        for (int y = 0; y < GRID_HEIGHT; y++) {
            // Add a new row
            gridCells.add(new ArrayList<>());
            for (int x = 0; x < GRID_WIDTH; x++) {
                // Add a new column (StackPane to the row)
                StackPane stackPane = new StackPane();
                // Add a stackPane to the grid
                grid.add(stackPane, x, y,1, 1);
                gridCells.get(y).add(stackPane);
            }
        }
    }

    private StackPane getCell(int x, int y) {
        return gridCells.get(y).get(x);
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
                        placeBrick();
                        event.consume();
                    }
                }
            }
        });
    }

    private void placeBrick() {
        System.out.println("placeBrick"); // TODO
    }
}
