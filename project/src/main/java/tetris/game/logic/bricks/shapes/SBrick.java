package tetris.game.logic.bricks.shapes;

import javafx.scene.paint.Color;
import tetris.game.logic.bricks.AbstractBrick;

public class SBrick extends AbstractBrick {
    protected final static Color COLOR = Color.RED;
    protected static final int ID = 5;

    // Standard size bricks (size of 4)
    public SBrick() {
        super(ID, COLOR);
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public SBrick(boolean isLarge) {
        super(ID, COLOR);
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {0, 1, 1},
                {1, 1, 0}
        }, 2);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {0, 1, 1},
                {0, 1, 0},
                {1, 1, 0}
        }, 2);
    }
}
