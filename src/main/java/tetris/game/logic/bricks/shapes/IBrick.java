package tetris.game.logic.bricks.shapes;

import tetris.game.logic.bricks.AbstractBrick;

public class IBrick extends AbstractBrick {
    // Standard size bricks (size of 4)
    public IBrick() {
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public IBrick(boolean isLarge) {
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {1, 1, 1, 1}
        }, 2);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {1, 1, 1, 1, 1}
        }, 2);
    }
}
