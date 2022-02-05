package tetris.game.logic.bricks.shapes;

import javafx.scene.paint.Color;
import tetris.game.logic.bricks.AbstractBrick;

public class IBrick extends AbstractBrick {
    protected static final Color COLOR = Color.AQUA;
    protected static final int ID = 1;

    // Standard size bricks (size of 4)
    public IBrick() {
        super(ID, COLOR);
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public IBrick(boolean isLarge) {
        super(ID, COLOR);
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
