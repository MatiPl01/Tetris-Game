package tetris.game.logic.bricks.shapes;

import tetris.game.logic.bricks.AbstractBrick;

public class OBrick extends AbstractBrick {
    // Standard size bricks (size of 4)
    public OBrick() {
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public OBrick(boolean isLarge) {
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {1, 1},
                {1, 1}
        }, 1);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        }, 1);
    }
}
