package tetris.game.logic;

import javafx.scene.paint.Color;
import tetris.game.enums.GameMode;
import tetris.game.enums.EventType;
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

    public Board(int width, int height, GameMode gameMode) {
        this.width = width;
        this.height = height;
        boardMatrix = new int[height][width];
        brickPicker = new RandomBrickPicker(gameMode == GameMode.HARD);
        scores = new Scores();
    }

    public int[][] getBoardMatrix() {
        return Copy.copy2DArray(boardMatrix);
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
        return isIntersecting(currentPosition, currentBrick.getCurrentShape());
    }

    public boolean moveBrick(EventType direction) {
        Point newPosition = new Point(currentPosition);

        switch (direction) {
            case LEFT -> newPosition.translate(-1, 0);
            case DOWN -> newPosition.translate(0, 1);
            case RIGHT -> newPosition.translate(1, 0);
        }

        if (isIntersecting(newPosition, currentBrick.getCurrentShape())) return false;
        currentPosition = newPosition;
        return true;
    }

    public boolean rotateBrick() {
        int[][] nextShape = currentBrick.getNextShape();
        if (isIntersecting(currentPosition, nextShape)) return false;
        currentBrick.rotate();
        return true;
    }

    public void newGame() {
        boardMatrix = new int[height][width];
        scores.reset();
        createNewBrick();
    }

    private boolean isOutOfBounds(Point point) {
        return (point.x < 0 || point.x >= width || point.y < 0 || point.y >= height);
    }

    private boolean isIntersecting(Point position, int[][] shape) {
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

    private void placeBrick() throws Exception {
        int[][] shape = currentBrick.getCurrentShape();

        if (isIntersecting(currentPosition, shape)) {
            throw new Exception("Cannot place brick as it is intersecting with some other brick or is out of bounds");
        }

        for (int shapeY = 0; shapeY < currentBrick.getCurrentHeight(); shapeY++) {
            for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
                if (shape[shapeY][shapeX] == 0) continue;
                int boardX = currentPosition.x + shapeX;
                int boardY = currentPosition.y + shapeY;
                boardMatrix[boardY][boardX] = currentBrick.getID();
            }
        }
    }

    private void clearFullRows() {
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
        for (int y = height - 1, i = 0; i <= newRowsIndices.size(); i++, y--) {
            newMatrix[y] = boardMatrix[newRowsIndices.get(i)];
        }
        boardMatrix = newMatrix;

        // Update score
        int clearedCount = boardMatrix.length - newRowsIndices.size();
        scores.add(50 * clearedCount * clearedCount);
    }
}
