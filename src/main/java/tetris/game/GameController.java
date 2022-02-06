package tetris.game;

import javafx.application.Platform;
import tetris.game.enums.EventSource;
import tetris.game.enums.EventType;
import tetris.game.enums.GameMode;
import tetris.game.enums.GameState;
import tetris.game.gui.controllers.*;
import tetris.game.gui.events.MoveEvent;
import tetris.game.logic.Board;
import tetris.game.logic.bricks.Brick;
import tetris.game.others.Time;

import java.util.List;

public class GameController implements Runnable {
    private static final int MAX_REFRESH_INTERVAL = 500;
    private static final int MIN_REFRESH_INTERVAL = 50;
    private static final int PAUSE_SLEEP_TIME = 100;
    private static final int MAX_SPEED_SCORE = 5000;

    private final Board board;
    private final BoardContainerController boardContainerController;
    private final NextBricksContainerController nextBricksContainerController;
    private final TimeContainerController timeContainerController;
    private final SettingsContainerController settingsContainerController;
    private final ScoreContainerController scoreContainerController;

    private GameState gameState;
    private int refreshInterval = MAX_REFRESH_INTERVAL;

    public GameController(int boardWidth,
                          int boardHeight,
                          GameMode gameMode,
                          MainContainerController mainContainerController) {
        // Assign values to attributes
        this.timeContainerController = mainContainerController.getTimeContainerController();
        this.scoreContainerController = mainContainerController.getScoreContainerController();
        this.boardContainerController = mainContainerController.getBoardContainerController();
        this.settingsContainerController = mainContainerController.getSettingsContainerController();
        this.nextBricksContainerController = mainContainerController.getNextBricksContainerController();
        board = new Board(boardWidth, boardHeight, gameMode);

        board.getScoresObj().setController(mainContainerController.getScoreContainerController());
        boardContainerController.setGameController(this);
        nextBricksContainerController.init(gameMode == GameMode.NORMAL ? 4 : 5);
    }

    @Override
    public void run() {
        while (true) {
            switch (gameState) {
                case RUNNING -> handleMoveEvent(new MoveEvent(EventSource.COMPUTER, EventType.DOWN), refreshInterval);
                case PAUSED, FINISHED -> Time.sleep(PAUSE_SLEEP_TIME);
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void newGame() {
        gameState = GameState.RUNNING;
        board.saveScores();
        board.newGame();
        timeContainerController.resetTimer();
        refreshInterval = MAX_REFRESH_INTERVAL;
        updateSpeedLabel();
        // Refresh next bricks container
        refreshNextBricks();
    }

    public void gameOver() {
        gameState = GameState.FINISHED;
        board.saveScores();
        timeContainerController.pauseTimer();
        settingsContainerController.gameOver();
        System.out.println("Game Over"); // TODO - add game over
    }

    public void pauseGame() {
        gameState = GameState.PAUSED;
        timeContainerController.pauseTimer();
    }

    public void resumeGame() {
        gameState = GameState.RUNNING;
        timeContainerController.resumeTimer();
    }

    public void handleMoveEvent(MoveEvent event) {
        handleMoveEvent(event, 0);
    }

    public void handleMoveEvent(MoveEvent event, int sleepTime) {
        switch (event.getEventType()) {
            case LEFT, RIGHT -> handleHorizontalMoveEvent(event.getEventType());
            case ROTATE -> handleRotateEvent();
            case PLACE -> handlePlaceEvent();
            case DOWN -> handleDownEvent(event.getEventSource());
        }
        // Refresh the animation
        renderNewFrame(sleepTime);
    }

    private void handleHorizontalMoveEvent(EventType eventType) {
        board.moveBrick(eventType);
    }

    private void handleRotateEvent() {
        board.rotateBrick();
    }

    private void handleDownEvent(EventSource eventSource) {
        boolean canMove = board.moveBrick(EventType.DOWN);

        if (!canMove) placeBrickHelper(false);
        // Add 1 score if a brick was moved down by a player
        if (eventSource == EventSource.PLAYER) {
            board.getScoresObj().add(1);
        }
    }

    private void handlePlaceEvent() {
        placeBrickHelper(true);
    }

    private void placeBrickHelper(boolean updateScore) {
        board.placeBrick(updateScore);
        // Check if there are rows that can be cleared
        board.clearFullRows();
        // Spawn a new brick
        spawnNewBrick();
        // Refresh next bricks container
        refreshNextBricks();
        // Update refresh interval
        updateRefreshInterval();
    }

    private void spawnNewBrick() {
        boolean isIntersecting = board.createNewBrick();
        // Check if a new brick is intersecting with another one
        // (it will intersect if there is not enough space to spawn a new brick)
        if (isIntersecting) {
            // Place only a part of a brick that fits in
            // (the last part before game over)
            board.placeBrick();
            gameOver();
        }
    }

    private void refreshNextBricks() {
        int nextBricksCount = nextBricksContainerController.getNextBricksCount();
        List<Brick> nextBricks = board.peekNextBricks(nextBricksCount);
        nextBricksContainerController.refresh(nextBricks);
    }

    private void renderNewFrame(int sleepTime) {
        long startTime = System.currentTimeMillis();
        Platform.runLater(() -> boardContainerController.requestAnimationFrame(board));
        long endTime = System.currentTimeMillis();
        Time.sleep(Math.max(0, sleepTime - (int)(endTime - startTime)));
    }

    private void updateRefreshInterval() {
        int score = board.getScoresObj().getScore();
        double ratio = Math.min(1, 1. * score / MAX_SPEED_SCORE);
        refreshInterval = (int)(MAX_REFRESH_INTERVAL - ratio * (MAX_REFRESH_INTERVAL - MIN_REFRESH_INTERVAL));
        updateSpeedLabel();
    }

    private void updateSpeedLabel() {
        Platform.runLater(() -> scoreContainerController.setSpeedMultiplier(1. * MAX_REFRESH_INTERVAL / refreshInterval));
    }
}
