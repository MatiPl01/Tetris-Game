package tetris.game;

import tetris.game.enums.EventSource;
import tetris.game.enums.EventType;
import tetris.game.enums.GameMode;
import tetris.game.enums.GameState;
import tetris.game.gui.controllers.BoardContainerController;
import tetris.game.gui.controllers.MainContainerController;
import tetris.game.gui.events.MoveEvent;
import tetris.game.logic.Board;

public class GameController {
    private final Board board;
    private final MainContainerController mainContainerController;
    private final BoardContainerController boardContainerController;
    private GameState gameState = GameState.RUNNING;

    public GameController(int boardWidth,
                          int boardHeight,
                          GameMode gameMode,
                          MainContainerController mainContainerController) {
        // Assign values to attributes
        this.mainContainerController = mainContainerController;
        this.boardContainerController = mainContainerController.getBoardContainerController();
        board = new Board(boardWidth, boardHeight, gameMode);

        boardContainerController.setGameController(this);
    }

    public void newGame() {
        board.newGame();
    }

    public GameState getGameState() {
        return gameState;
    }

    public void handleMoveEvent(MoveEvent event) {
        switch (event.getEventType()) {
            case LEFT, RIGHT -> handleHorizontalMoveEvent(event.getEventType());
            case ROTATE -> handleRotateEvent();
            case DOWN -> handleDownEvent(event.getEventSource());
        }
    }

    private void handleHorizontalMoveEvent(EventType eventType) {
        board.moveBrick(eventType);
        // Refresh the animation
        boardContainerController.requestAnimationFrame(board);
    }

    private void handleRotateEvent() {
        board.rotateBrick();
        // Refresh the animation
        boardContainerController.requestAnimationFrame(board);
    }

    private void handleDownEvent(EventSource eventSource) {
        boolean canMove = board.moveBrick(EventType.DOWN);

        if (!canMove) {
            board.placeBrick();
            // Check if there are rows that can be cleared
            board.clearFullRows();
            // Spawn a new brick
            boolean isIntersecting = board.createNewBrick();
            // Check if a new brick is intersecting with another one
            // (it will intersect if there is not enough space to spawn a new brick)
            if (isIntersecting) {
                gameState = GameState.FINISHED;
                // TODO - add game over
            }
        }
        // Add 1 score if a brick was moved down by a player
        if (eventSource == EventSource.PLAYER) {
            board.getScoresObj().add(1);
        }
        // Refresh the animation
        boardContainerController.requestAnimationFrame(board);
    }
}
