package tetris.game.logic.bricks.shapes;

import javafx.scene.paint.Color;
import tetris.game.logic.bricks.AbstractBrick;

public class JBrick extends AbstractBrick {
    protected final static Color COLOR = Color.BLUEVIOLET;
    protected static final int ID = 2;

    // Standard size bricks (size of 4)
    public JBrick() {
        super(ID, COLOR);
        addDefaultShapes();
    }

    // Large size bricks (size of 5)
    public JBrick(boolean isLarge) {
        super(ID, COLOR);
        if (isLarge) addLargeShapes();
        else addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {1, 0, 0, 0},
                {1, 1, 1, 1}
        }, 4);
    }

    private void addLargeShapes() {
        generateShapes(new int[][] {
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 1, 1, 1, 1}
        }, 4);
    }
}
