package tetris.game;

import tetris.game.enums.GameMode;
import tetris.game.enums.GameState;
import tetris.game.gui.controllers.BoardContainerController;
import tetris.game.gui.controllers.MainContainerController;
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

    public GameState getGameState() {
        return gameState;
    }
}
