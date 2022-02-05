package tetris.game.logic.bricks.shapes;

import javafx.scene.paint.Color;
import tetris.game.logic.bricks.AbstractBrick;

public class OBrick extends AbstractBrick {
    protected final static Color COLOR = Color.YELLOW;
    protected static final int ID = 4;

    // Standard size bricks (size of 4)
    public OBrick() {
        super(ID, COLOR);
        addDefaultShapes();
    }

    // Large size bricks (there are no squares having 5 cells)
    public OBrick(boolean isLarge) {
        super(ID, COLOR);
        addDefaultShapes();
    }

    private void addDefaultShapes() {
        generateShapes(new int[][] {
                {1, 1},
                {1, 1}
        }, 1);
    }
}
