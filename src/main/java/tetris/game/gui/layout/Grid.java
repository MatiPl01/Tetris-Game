package tetris.game.gui.layout;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final GridPane gridPane;
    private final int rowsNum;
    private final int colsNum;
    private final double cellWidth;
    private final double cellHeight;

    private final List<List<StackPane>> gridCells = new ArrayList<>();

    public Grid(GridPane gridPane, int colsNum, int rowsNum, double width, double height) {
        this.gridPane = gridPane;
        this.rowsNum = rowsNum;
        this.colsNum = colsNum;
        this.cellWidth = calcCellWidth(colsNum, rowsNum, width, height);
        this.cellHeight = calcCellHeight(colsNum, rowsNum, width, height);
        buildGrid();
    }

    public Grid(GridPane gridPane, int colsNum, int rowsNum, int cellSize) {
        this.gridPane = gridPane;
        this.colsNum = colsNum;
        this.rowsNum = rowsNum;
        this.cellHeight = this.cellWidth = cellSize;
        buildGrid();
    }

    public StackPane get(int x, int y) {
        return gridCells.get(y).get(x);
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public void clear() {
        for (int y = 0; y < rowsNum; y++) {
            for (int x = 0; x < colsNum; x++) {
                get(x, y).getChildren().clear();
            }
        }
    }

    private void buildGrid() {
        for (int i = 0; i < rowsNum; i++) gridPane.getRowConstraints().add(new RowConstraints(cellHeight));
        for (int i = 0; i < colsNum; i++) gridPane.getColumnConstraints().add(new ColumnConstraints(cellWidth));
        gridPane.setGridLinesVisible(true);

        // Add grid cells (StackPanes) to make grid updates easier
        for (int y = 0; y < rowsNum; y++) {
            // Add a new row
            gridCells.add(new ArrayList<>());
            for (int x = 0; x < colsNum; x++) {
                // Add a new column (StackPane to the row)
                StackPane stackPane = new StackPane();
                // Add a stackPane to the grid
                gridPane.add(stackPane, x, y,1, 1);
                gridCells.get(y).add(stackPane);
            }
        }
    }

    private double calcCellWidth(int colsNum, int rowsNum, double width, double height) {
        // If width wasn't specified
        if (width < 0) return height / rowsNum;
        // Otherwise
        return width / colsNum;
    }

    private double calcCellHeight(int colsNum, int rowsNum, double width, double height) {
        // If height wasn't specified
        if (height < 0) return width / colsNum;
        // Otherwise
        return height / rowsNum;
    }
}
