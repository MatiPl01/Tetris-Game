package tetris.game.logic;

import javafx.scene.paint.Color;
import tetris.game.enums.GameMode;
import tetris.game.enums.EventType;
import tetris.game.enums.Rotation;
import tetris.game.helpers.Copy;
import tetris.game.logic.bricks.Brick;
import tetris.game.logic.bricks.RandomBrickPicker;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {
    private final int width;
    private final int height;
    private final RandomBrickPicker brickPicker;
    private final Scores scores;
    private Point currentPosition;
    private Brick currentBrick;

    private final Map<Integer, Color> bricksColors = new HashMap<>();
    private int[][] boardMatrix;
    private final int[] bricksHeights;

    public Board(int width, int height, GameMode gameMode) {
        this.width = width;
        this.height = height;
        boardMatrix = new int[height][width];
        bricksHeights = new int[width];
        brickPicker = new RandomBrickPicker(gameMode == GameMode.HARD);
        scores = new Scores();
    }

    public void newGame() {
        boardMatrix = new int[height][width];
        scores.reset();
        createNewBrick();
    }

    public int[][] getBoardMatrix() {
        int[][] matrix = Copy.copy2DArray(boardMatrix);
        Point shadowPosition = getFallenBrickPosition();
        if (shadowPosition.y >= 0) placeBrickHelper(matrix, shadowPosition, -currentBrick.getID());
        placeBrickHelper(matrix, currentPosition);
        return matrix;
    }

    public Color getBrickColor(int brickID) {
        return bricksColors.get(brickID);
    }

    public List<Brick> peekNextBricks(int count) {
        return brickPicker.peekNextBricks(count);
    }

    public Scores getScoresObj() {
        return scores;
    }

    public boolean createNewBrick() {
        currentBrick = brickPicker.getNextBrick();
        currentPosition = new Point((width - currentBrick.getCurrentWidth()) / 2, 0);
        if (!bricksColors.containsKey(currentBrick.getID())) {
            bricksColors.put(currentBrick.getID(), currentBrick.getColor());
        }
        return isIntersecting(currentPosition);
    }

    public boolean moveBrick(EventType direction) {
        Point newPosition = new Point(currentPosition);

        switch (direction) {
            case LEFT -> newPosition.translate(-1, 0);
            case DOWN -> newPosition.translate(0, 1);
            case RIGHT -> newPosition.translate(1, 0);
        }

        if (isIntersecting(newPosition)) return false;
        currentPosition = newPosition;
        return true;
    }

    public void rotateBrick() {
        rotationHelper(Rotation.RIGHT);
        // Check if a brick can be rotated
        if (isIntersecting(currentPosition)) {
            // Rotate brick back if it is intersecting
            rotationHelper(Rotation.LEFT);
        }
    }

    public void placeBrick() {
        currentPosition = getFallenBrickPosition();
        placeBrickHelper(boardMatrix, currentPosition);
        updateBricksHeights();
    }

    public void clearFullRows() {
        int[][] newMatrix = new int[height][width];
        List<Integer> newRowsIndices = new ArrayList<>();

        // Select rows which will remain after removing full rows
        for (int y = height - 1; y >= 0; y--) {
            boolean isFullRow = true;
            for (int x = 0; x < width; x++) {
                if (boardMatrix[y][x] == 0) {
                    isFullRow = false;
                    break;
                }
            }
            if (!isFullRow) newRowsIndices.add(y);
        }

        // Create new board matrix without cleared rows
        for (int y = height - 1, i = 0; i < newRowsIndices.size(); i++, y--) {
            newMatrix[y] = boardMatrix[newRowsIndices.get(i)];
        }
        boardMatrix = newMatrix;

        // Update score
        int clearedCount = boardMatrix.length - newRowsIndices.size();
        scores.add(50 * clearedCount * clearedCount);
    }

    private boolean isOutOfBounds(Point point) {
        return (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height);
    }

    private void placeBrickHelper(int[][] matrix, Point position) {
        placeBrickHelper(matrix, position, currentBrick.getID());
    }

    private void placeBrickHelper(int[][] matrix, Point position, int id) {
        int[][] shape = currentBrick.getCurrentShape();

        // Place the current brick on a matrix
        for (int shapeY = 0; shapeY < currentBrick.getCurrentHeight(); shapeY++) {
            int boardY = position.y + shapeY;
            if (boardY < 0) continue;

            for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
                if (shape[shapeY][shapeX] == 0) continue;
                int boardX = position.x + shapeX;
//                System.out.println("x=" + boardX + ", y=" + boardY + ", id=" + id);
                matrix[boardY][boardX] = id;
            }
        }
    }

    private boolean isIntersecting(Point position) {
        int[][] shape = currentBrick.getCurrentShape();

        for (int shapeY = 0; shapeY < currentBrick.getCurrentHeight(); shapeY++) {
            for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
                if (shape[shapeY][shapeX] == 0) continue;
                int boardX = position.x + shapeX;
                int boardY = position.y + shapeY;
                if (isOutOfBounds(new Point(boardX, boardY)) || boardMatrix[boardY][boardX] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateBricksHeights() {
        int[] topIndexes = currentBrick.getShapeTopIndexes();
        for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
            bricksHeights[currentPosition.x + shapeX] = height - currentPosition.y - topIndexes[shapeX];
        }
    }

    private Point getFallenBrickPosition() {
        int y = height;
        int[] shapeBottomIndexes = currentBrick.getShapeBottomIndexes();
        for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
            y = Math.min(y, height - bricksHeights[currentPosition.x + shapeX] - shapeBottomIndexes[shapeX] - 1);
        }
        return new Point(currentPosition.x, y);
    }

    private void rotationHelper(Rotation rotation) {
        int prevW = currentBrick.getCurrentWidth();
        int prevH = currentBrick.getCurrentHeight();
        currentBrick.rotate(rotation);
        int currW = currentBrick.getCurrentWidth();
        int currH = currentBrick.getCurrentHeight();
        int dX = (prevW - currW) / 2;
        int dY = (prevH - currH) / 2;
        currentPosition.translate(dX, dY);
    }
}
