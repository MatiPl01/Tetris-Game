package tetris.game.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import tetris.game.GameController;
import tetris.game.enums.MusicState;
import tetris.game.others.MusicPlayer;

public class SettingsContainerController {
    private static final String MUSIC_BEGINNING_PATH = "src/main/resources/audio/tetris-audio-intro.wav";
    private static final String MUSIC_LOOPED_PATH = "src/main/resources/audio/tetris-audio-main.wav";
    private static final String PAUSE_GAME_TEXT = "Pause";
    private static final String RESUME_GAME_TEXT = "Resume";
    private static final String PAUSE_MUSIC_TEXT = "Stop";
    private static final String RESUME_MUSIC_TEXT = "Play";

    private final MusicPlayer musicPlayer = new MusicPlayer();
    private MusicState musicState = MusicState.PLAYING;
    private GameController gameController;

    @FXML
    private Button pauseGameButton;

    @FXML
    private Button pauseMusicButton;

    @FXML
    private void initialize() {
        setupMusicPlayer();
    }

    @FXML
    private void onNewGameClick() {
        newGame();
        gameController.newGame();
    }

    @FXML
    private void onPauseGameClick() {
        switch (gameController.getGameState()) {
            case PAUSED -> {
                gameController.resumeGame();
                pauseGameButton.setText(PAUSE_GAME_TEXT);
            }
            case RUNNING -> {
                gameController.pauseGame();
                pauseGameButton.setText(RESUME_GAME_TEXT);
            }
        }
    }

    @FXML
    private void onPauseMusicClick() {
        switch (musicState) {
            case PAUSED -> {
                musicPlayer.play();
                musicState = MusicState.PLAYING;
                pauseMusicButton.setText(PAUSE_MUSIC_TEXT);
            }
            case PLAYING -> {
                musicPlayer.pause();
                musicState = MusicState.PAUSED;
                pauseMusicButton.setText(RESUME_MUSIC_TEXT);
            }
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void newGame() {
        setupMusicPlayer();
        gameController.resumeGame();
        pauseGameButton.setText(PAUSE_GAME_TEXT);
        musicPlayer.play();
        musicState = MusicState.PLAYING;
        pauseMusicButton.setText(PAUSE_MUSIC_TEXT);
        pauseGameButton.setDisable(false);
        pauseMusicButton.setDisable(false);
    }

    public void gameOver() {
        musicPlayer.clear();
        pauseGameButton.setDisable(true);
        pauseMusicButton.setDisable(true);
    }

    private void setupMusicPlayer() {
        musicPlayer.clear();
        musicPlayer.addTrack(MUSIC_BEGINNING_PATH);
        musicPlayer.addTrack(MUSIC_LOOPED_PATH,true);
        musicPlayer.play();
    }
}
