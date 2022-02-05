package tetris.game;

import tetris.game.enums.EventSource;
import tetris.game.enums.EventType;
import tetris.game.enums.GameMode;
import tetris.game.enums.GameState;
import tetris.game.gui.controllers.BoardContainerController;
import tetris.game.gui.controllers.MainContainerController;
import tetris.game.gui.controllers.NextBricksContainerController;
import tetris.game.gui.events.MoveEvent;
import tetris.game.logic.Board;
import tetris.game.logic.bricks.Brick;

import java.util.List;

public class GameController {
    private final Board board;
    private final MainContainerController mainContainerController;
    private final BoardContainerController boardContainerController;
    private final NextBricksContainerController nextBricksContainerController;
    private GameState gameState = GameState.RUNNING;

    public GameController(int boardWidth,
                          int boardHeight,
                          GameMode gameMode,
                          MainContainerController mainContainerController) {
        // Assign values to attributes
        this.mainContainerController = mainContainerController;
        this.boardContainerController = mainContainerController.getBoardContainerController();
        this.nextBricksContainerController = mainContainerController.getNextBricksContainerController();
        board = new Board(boardWidth, boardHeight, gameMode);

        boardContainerController.setGameController(this);
        nextBricksContainerController.init(gameMode == GameMode.NORMAL ? 4 : 5);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void newGame() {
        this.gameState = GameState.RUNNING;
        board.newGame();
        boardContainerController.requestAnimationFrame(board);
        boardContainerController.startAnimation();
        // Refresh next bricks container
        refreshNextBricks();
    }

    public void gameOver() {
        this.gameState = GameState.FINISHED;
        System.out.println("Game Over"); // TODO - add game over
    }

    public void handleMoveEvent(MoveEvent event) {
        switch (event.getEventType()) {
            case LEFT, RIGHT -> handleHorizontalMoveEvent(event.getEventType());
            case ROTATE -> handleRotateEvent();
            case PLACE -> handlePlaceEvent();
            case DOWN -> handleDownEvent(event.getEventSource());
        }
        // Refresh the animation
        boardContainerController.requestAnimationFrame(board);
    }

    private void handleHorizontalMoveEvent(EventType eventType) {
        board.moveBrick(eventType);
    }

    private void handleRotateEvent() {
        board.rotateBrick();
    }

    private void handleDownEvent(EventSource eventSource) {
        boolean canMove = board.moveBrick(EventType.DOWN);

        if (!canMove) {
            board.placeBrick();
            // Check if there are rows that can be cleared
            board.clearFullRows();
            // Spawn a new brick
            spawnNewBrick();
            // Refresh next bricks container
            refreshNextBricks();
        }
        // Add 1 score if a brick was moved down by a player
        if (eventSource == EventSource.PLAYER) {
            board.getScoresObj().add(1);
        }
    }

    private void handlePlaceEvent() {
        board.placeBrick(true);
        // Spawn a new brick
        spawnNewBrick();
        // Refresh next bricks container
        refreshNextBricks();
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
}
