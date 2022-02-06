package tetris.game.logic.bombs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import tetris.game.gui.layout.Grid;
import tetris.game.logic.Board;
import tetris.game.others.ImageLoader;
import tetris.game.others.Random;
import tetris.game.others.Time;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Bomb {
    private static final String IMAGE_PATH = "src/main/resources/images/bomb.png";
    private static final Image IMAGE = ImageLoader.loadImage(IMAGE_PATH);
    private static final int EXPLOSION_DELAY = 1000;
    private static final List<Integer> EXPLOSION_RADIUS_LIST = new ArrayList<>() {{
        add(1);
        add(2);
    }};

    private final int explosionRadius;
    private final Point position;
    private final Board board;
    private final Grid grid;

    public Bomb(Point position, Board board) {
        this.board = board;
        this.position = position;
        grid = board.getBoardContainerController().getGrid();
        explosionRadius = getRandomExplosionRadius();
        spawn();

        Time.setTimeout(this::explode, EXPLOSION_DELAY);
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void show() {
        ImageView imageView = new ImageView(IMAGE);
        int bombSize = (int) Math.min(grid.getCellHeight(), grid.getCellWidth());
        imageView.setFitHeight(bombSize);
        imageView.setFitWidth(bombSize);
        grid.get(position.x, position.y).getChildren().add(imageView);
    }

    private void spawn() {
        board.spawnBomb(position, this);
        show();
    }

    private void explode() {
        board.explodeBomb(position);
    }

    private int getRandomExplosionRadius() {
        return Random.choice(EXPLOSION_RADIUS_LIST);
    }
}
