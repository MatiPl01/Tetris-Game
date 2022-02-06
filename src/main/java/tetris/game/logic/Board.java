package tetris.game.logic;

import javafx.scene.paint.Color;
import tetris.game.enums.GameMode;
import tetris.game.enums.EventType;
import tetris.game.enums.Rotation;
import tetris.game.gui.controllers.BoardContainerController;
import tetris.game.logic.bombs.Bomb;
import tetris.game.logic.scores.Scores;
import tetris.game.others.Copy;
import tetris.game.logic.bricks.Brick;
import tetris.game.logic.bricks.RandomBrickPicker;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Board {
    private final int width;
    private final int height;
    private final BoardContainerController boardContainerController;
    private final RandomBrickPicker brickPicker;
    private final Scores scores;
    private Point currentPosition;
    private Brick currentBrick;

    private final Map<Integer, Color> bricksColors = new HashMap<>();
    private final Map<Point, Bomb> bombs = new HashMap<>();
    private int[][] boardMatrix;

    public Board(int width,
                 int height,
                 GameMode gameMode,
                 BoardContainerController boardContainerController) {
        this.width = width;
        this.height = height;
        this.boardContainerController = boardContainerController;
        boardMatrix = new int[height][width];
        brickPicker = new RandomBrickPicker(gameMode == GameMode.HARD);
        scores = new Scores();
    }

    public BoardContainerController getBoardContainerController() {
        return boardContainerController;
    }

    public void newGame() {
        boardMatrix = new int[height][width];
        createNewBrick();
        scores.reset();
    }

    public void saveScores() {
        scores.saveScores();
    }

    public int[][] getBoardMatrix() {
        int[][] matrix = Copy.copy2DArray(boardMatrix);
        Point shadowPosition = getFallenBrickPosition();
        if (shadowPosition.y >= 0) placeBrickHelper(matrix, shadowPosition, -currentBrick.getID());
        placeBrickHelper(matrix, currentPosition);
        return matrix;
    }

    public List<Point> getFallenBricksPositions() {
        List<Point> positions = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (boardMatrix[y][x] > 0) positions.add(new Point(x, y));
            }
        }

        return positions;
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
        placeBrick(false);
    }

    public void placeBrick(boolean updateScore) {
        int prevY = currentPosition.y;
        // Update current brick position
        currentPosition = getFallenBrickPosition();
        placeBrickHelper(boardMatrix, currentPosition);
        // Update score
        if (updateScore) {
            int distance = currentPosition.y - prevY;
            scores.add(distance);
            System.out.println(scores.getScore());
        }
    }

    public void clearFullRows() {
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

        updateRows(newRowsIndices);

        int clearedCount = boardMatrix.length - newRowsIndices.size();
        // Update score
        scores.add(50 * clearedCount * clearedCount);
    }

    public Map<Point, Bomb> getBombs() {
        return bombs;
    }

    public void spawnBomb(Point position, Bomb bomb) {
        bombs.put(position, bomb);
        // Remove the rectangle where the bomb will appear
        boardMatrix[position.y][position.x] = 0;
        boardContainerController.requestAnimationFrame(this);
        scores.add(1);
    }

    public void explodeBomb(Point position) {
        Bomb bomb = bombs.remove(position);
        int explosionRadius = bomb.getExplosionRadius();

        for (int x = 0; x < width; x++) {
            // Remove the whole row of bricks
            if (boardMatrix[position.y][x] > 0) {
                boardMatrix[position.y][x] = 0;
                scores.add(1);
            }
        }

        // Remove surrounding bricks parts within a proper radius
        int startX = Math.max(position.x - explosionRadius, 0);
        int endX = Math.min(position.x + explosionRadius, width - 1);
        int startY = Math.max(position.y - explosionRadius, 0);
        int endY = Math.min(position.y + explosionRadius, height - 1);

        System.out.println("startX: " + startX);
        System.out.println("endX: " + endX);
        System.out.println("startY: " + startY);
        System.out.println("endY: " + endY);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (boardMatrix[y][x] > 0) {
                    System.out.println("REMOVING SURROUNDING: x=" + x + ", y=" + y);
                    boardMatrix[y][x] = 0;
                    scores.add(2);
                }
            }
        }

        // Remove empty rows
        updateRows(getNotEmptyRowsIndexes());
    }

    private List<Integer> getNotEmptyRowsIndexes() {
        List<Integer> notEmptyRowsIndexes = new ArrayList<>();

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                if (boardMatrix[y][x] > 0) {
                    notEmptyRowsIndexes.add(y);
                    break;
                }
            }
        }
        return notEmptyRowsIndexes;
    }

    private void updateRows(List<Integer> newRowsIndices) {
        int[][] newMatrix = new int[height][width];

        // Create new board matrix without empty rows
        for (int y = height - 1, i = 0; i < newRowsIndices.size(); i++, y--) {
            newMatrix[y] = boardMatrix[newRowsIndices.get(i)];
        }
        boardMatrix = newMatrix;
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

    private Point getFallenBrickPosition() {
        int[] shapeBottomIndexes = currentBrick.getShapeBottomIndexes();
        int y = height - currentBrick.getCurrentHeight();
        for (int shapeX = 0; shapeX < currentBrick.getCurrentWidth(); shapeX++) {
            int boardX = currentPosition.x + shapeX;
            for (int boardY = currentPosition.y + shapeBottomIndexes[shapeX]; boardY < height; boardY++) {
                if (boardY < 0 || boardMatrix[boardY][boardX] > 0) {
                    y = Math.min(y, boardY - shapeBottomIndexes[shapeX] - 1);
                    break;
                }
            }
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
