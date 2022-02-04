package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BoardContainerController {
    private Scene scene;

    private static final int CELL_SIZE = 30;
    private static final int GRID_WIDTH = 10;  // TODO - allow user to set this on game startup
    private static final int GRID_HEIGHT = 20; // TODO - allow user to set this on game startup

    @FXML
    private GridPane grid;

    @FXML
    private void initialize() {
        buildGrid();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        setupKeyboardEvents();
    }

    private void buildGrid() {
        for (int i = 0; i < GRID_HEIGHT; i++) grid.getRowConstraints().add(new RowConstraints(CELL_SIZE));
        for (int i = 0; i < GRID_WIDTH; i++)  grid.getColumnConstraints().add(new ColumnConstraints(CELL_SIZE));
    }

    private void setupKeyboardEvents() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> rotateBrick();
                case LEFT -> moveBrickLeft();
                case RIGHT -> moveBrickRight();
                case DOWN -> moveBrickDown();
                case SPACE -> placeBrick();
            }
        });
    }

    private void rotateBrick() {
        System.out.println("rotateBrick");
    }

    private void moveBrickLeft() {
        System.out.println("moveBrickLeft");
    }

    private void moveBrickRight() {
        System.out.println("moveBrickRight");
    }

    private void moveBrickDown() {
        System.out.println("moveBrickDown");
    }

    private void placeBrick() {
        System.out.println("placeBrick");
    }
}
