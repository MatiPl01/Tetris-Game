package tetris.game.logic.bricks.shapes;

import tetris.game.logic.bricks.AbstractBrick;

public class TBrick extends AbstractBrick {
    // Standard size bricks (size of 4)
    public TBrick() {
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public TBrick(boolean isLarge) {
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