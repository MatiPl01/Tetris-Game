package tetris.game.logic.bricks.shapes;

import tetris.game.logic.bricks.AbstractBrick;

public class SBrick extends AbstractBrick {
    // Standard size bricks (size of 4)
    public SBrick() {
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public SBrick(boolean isLarge) {
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
                {0, 0, 1, 1, 1},
                {1, 1, 1, 0, 0}
        }, 2);
    }
}