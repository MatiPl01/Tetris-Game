package tetris.game.logic.bricks;

import javafx.scene.paint.Color;
import tetris.game.enums.Rotation;

import java.util.List;

public interface Brick {
    List<int[][]> getShapes();

    int[][] getCurrentShape();

    int[][] getNextShape();

    void printAllShapes();

    int getCurrentWidth();

    int getCurrentHeight();

    int getID();

    Color getColor();

    void rotate(Rotation rotation);
}
