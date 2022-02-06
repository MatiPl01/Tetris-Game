package tetris.game.logic.bombs;

import javafx.application.Platform;
import tetris.game.GameController;
import tetris.game.logic.Board;
import tetris.game.others.Random;
import tetris.game.others.Time;

import java.awt.*;
import java.util.List;

public class BombSpawner implements Runnable {
    private static final int PAUSE_SLEEP_TIME = 100;
    private final static int MIN_BOMB_SPAWN_INTERVAL = 2000;
    private final static int MAX_BOMB_SPAWN_INTERVAL = 20000;
    private final GameController gameController;
    private final Board board;
    public BombSpawner(Board board,
                       GameController gameController) {
        this.gameController = gameController;
        this.board = board;
    }

    @Override
    public void run() {
        while (true) {
            switch (gameController.getGameState()) {
                case RUNNING -> {
                    int interval = getRandomInterval();
                    Time.sleep(interval);
                    spawnBomb();
                }
                case PAUSED, FINISHED -> Time.sleep(PAUSE_SLEEP_TIME);
            }
        }
    }

    private int getRandomInterval() {
        return Random.randInt(MIN_BOMB_SPAWN_INTERVAL, MAX_BOMB_SPAWN_INTERVAL);
    }

    private Point getRandomBrickPosition() {
        List<Point> fallenBricksPositions = board.getFallenBricksPositions();
        return Random.choice(fallenBricksPositions);
    }

    private void spawnBomb() {
        Platform.runLater(() -> {
            Point position = getRandomBrickPosition();
            if (position != null && !board.getBombs().containsKey(position)) {
                new Bomb(position, board);
            };
        });
    }
}
