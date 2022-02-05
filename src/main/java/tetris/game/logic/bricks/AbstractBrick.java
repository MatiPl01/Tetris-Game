package tetris.game.logic.bricks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public abstract class AbstractBrick implements IBrick {
    protected final List<int[][]> brickShapes = new ArrayList<>();

    @Override
    public String toString() {
        return createShapeString(brickShapes.get(0));
    }

    @Override
    public List<int[][]> getShapes() {
        return brickShapes;
    }

    public void printAllShapes() {
        for (int i = 0; i < brickShapes.size(); i++) {
            System.out.println("Shape " + (i + 1));
            System.out.println(createShapeString(brickShapes.get(i)));
            System.out.println();
        }
    }

    protected void generateShapes(int[][] shape, int count) {
        brickShapes.add(cloneShape(shape));

        for (int i = 0; i < count - 1; i++) {
            shape = getRotatedShape(shape);
            brickShapes.add(shape);
        }
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

    private int[][] cloneShape(int[][] shape) {
        int h = shape.length;
        int w = shape[0].length;
        int[][] clone = new int[h][w];

        for (int y = 0; y < h; y++) {
            System.arraycopy(shape[y], 0, clone[y], 0, w);
        }

        return clone;
    }
}
