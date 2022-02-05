package tetris.game.logic.bricks.shapes;

import tetris.game.logic.bricks.AbstractBrick;

public class JBrick extends AbstractBrick {
    // Standard size bricks (size of 4)
    public JBrick() {
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public JBrick(boolean isLarge) {
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {0, 1},
                {0, 1},
                {0, 1},
                {1, 1}
        }, 4);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {0, 0, 1},
                {0, 0, 1},
                {0, 0, 1},
                {0, 0, 1},
                {0, 1, 1}
        }, 4);
    }
}
