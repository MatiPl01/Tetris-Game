package tetris.game.logic.bricks.shapes;

import javafx.scene.paint.Color;
import tetris.game.logic.bricks.AbstractBrick;

public class TBrick extends AbstractBrick {
    protected final static Color COLOR = Color.BEIGE;
    protected static final int ID = 6;

    // Standard size bricks (size of 4)
    public TBrick() {
        super(ID, COLOR);
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public TBrick(boolean isLarge) {
        super(ID, COLOR);
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {1, 1, 1},
                {0, 1, 0}
        }, 4);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {1, 1, 1, 1, 1},
                {0, 0, 1, 0, 0},
                {0, 0, 1, 0, 0}
        }, 4);
    }
}
