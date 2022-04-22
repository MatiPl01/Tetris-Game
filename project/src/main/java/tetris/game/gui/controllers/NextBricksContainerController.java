package tetris.game.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import tetris.game.gui.layout.Grid;
import tetris.game.logic.bricks.Brick;

import java.util.ArrayList;
import java.util.List;

public class NextBricksContainerController {
    private static final int NEXT_BRICKS_COUNT = 3;
    private static final int GRID_SIZE = 180;

    private final List<Grid> grids = new ArrayList<>();

    @FXML
    private GridPane nextGridPane1;

    @FXML
    private GridPane nextGridPane2;

    @FXML
    private GridPane nextGridPane3;
    private int cellsNum;

    public void init(int cellsNum) {
        this.cellsNum = cellsNum;
        buildGrid(nextGridPane1);
        buildGrid(nextGridPane2);
        buildGrid(nextGridPane3);
    }

    public int getNextBricksCount() {
        return NEXT_BRICKS_COUNT;
    }

    public void refresh(List<Brick> bricks) {
        Platform.runLater(() -> {
            for (int i = 0; i < NEXT_BRICKS_COUNT; i++) {
                refreshGrid(grids.get(i), bricks.get(i));
            }
        });
    }

    private void buildGrid(GridPane gridPane) {
        grids.add(new Grid(gridPane, cellsNum, cellsNum, GRID_SIZE, GRID_SIZE));
    }

    private void refreshGrid(Grid grid, Brick brick) {
        grid.clear();
        int startX = (cellsNum - brick.getCurrentWidth()) / 2;
        int startY = (cellsNum - brick.getCurrentHeight()) / 2;
        int[][] shape = brick.getCurrentShape();

        for (int shapeY = 0; shapeY < brick.getCurrentHeight(); shapeY++) {
            for (int shapeX = 0; shapeX < brick.getCurrentWidth(); shapeX++) {
                if (shape[shapeY][shapeX] == 0) continue;
                int x = startX + shapeX;
                int y = startY + shapeY;
                Rectangle rectangle = new Rectangle(grid.getCellWidth(), grid.getCellHeight());
                rectangle.setFill(brick.getColor());
                grid.get(x, y).getChildren().add(rectangle);
            }
        }
    }
}
