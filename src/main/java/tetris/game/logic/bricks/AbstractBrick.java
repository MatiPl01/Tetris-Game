package tetris.game.logic.bricks;

import javafx.scene.paint.Color;
import tetris.game.helpers.Copy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class AbstractBrick implements Brick {
    private final int id;
    private final Color color;

    protected final List<int[][]> brickShapes = new ArrayList<>();
    private int currentShapeIdx = 0;

    public AbstractBrick(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    @Override
    public String toString() {
        return createShapeString(brickShapes.get(0));
    }

    @Override
    public List<int[][]> getShapes() {
        return brickShapes;
    }

    @Override
    public void rotate() {
        currentShapeIdx = getNextShapeIndex();
    }

    @Override
    public int[][] getCurrentShape() {
        return Copy.copy2DArray(brickShapes.get(currentShapeIdx));
    }

    @Override
    public int[][] getNextShape() {
        return Copy.copy2DArray(brickShapes.get(getNextShapeIndex()));
    }

    @Override
    public int getCurrentWidth() {
        return brickShapes.get(currentShapeIdx)[0].length;
    }

    @Override
    public int getCurrentHeight() {
        return brickShapes.get(currentShapeIdx).length;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void printAllShapes() {
        for (int i = 0; i < brickShapes.size(); i++) {
            System.out.println("Shape " + (i + 1));
            System.out.println(createShapeString(brickShapes.get(i)));
            System.out.println();
        }
    }

    protected void generateShapes(int[][] shape, int count) {
        brickShapes.add(Copy.copy2DArray(shape));

        for (int i = 0; i < count - 1; i++) {
            shape = getRotatedShape(shape);
            brickShapes.add(shape);
        }
    }

    private int getNextShapeIndex() {
        return (currentShapeIdx + 1) % brickShapes.size();
    }

    private String createShapeString(int[][] shape) {
        StringJoiner sj = new StringJoiner("\n");

        for (int[] row: shape) {
            sj.add(Arrays.stream(row)
                    .mapToObj(l -> l == 1 ? "#" : " ")
                    .collect(Collectors.joining("")));
        }

        return sj.toString();
    }

    private int[][] getRotatedShape(int[][] shape) {
        int h = shape.length;
        int w = shape[0].length;
        int[][] newShape = new int[w][h];

        try {
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if (h > w) newShape[x][h - 1 - y] = shape[y][x];
                    else newShape[x][y] = shape[y][x];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newShape;
    }
}
