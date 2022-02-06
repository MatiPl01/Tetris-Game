package tetris.game.others;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class MusicPlayer {
    private final static int REFRESH_INTERVAL = 1;

    Deque<AudioInputStream> playingDeque = new ArrayDeque<>();
    Set<AudioInputStream> repeatedTracks = new HashSet<>();
    private final Timeline timeline;
    private Clip currentTrack;

    public MusicPlayer() {
        timeline = new Timeline(new KeyFrame(
                Duration.millis(REFRESH_INTERVAL),
                actionEvent -> checkAudioSwitch())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void addTrack(String path) {
        addTrack(path,false);
    }

    public void addTrack(String path, boolean repeat) {
        File file = new File(path);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            playingDeque.add(audioStream);
            if (repeat) repeatedTracks.add(audioStream);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        playingDeque.clear();
        repeatedTracks.clear();
        timeline.stop();
        if (currentTrack != null) currentTrack.stop();
        currentTrack = null;
    }

    public void play() {
        if (playingDeque.size() > 0) {
            if (currentTrack == null) playNextTrack();
            currentTrack.start();
            timeline.play();
        }
    }

    public void pause() {
        currentTrack.stop();
        timeline.pause();
    }

    private void checkAudioSwitch() {
        if (currentTrack.getFramePosition() >= currentTrack.getFrameLength()) {
            playNextTrack();
            currentTrack.start();
        }
    }

    private void playNextTrack() {
        if (playingDeque.size() > 0) {
            AudioInputStream audioStream = playingDeque.poll();
            if (repeatedTracks.contains(audioStream)) playingDeque.add(audioStream);
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                currentTrack = clip;
            } catch (LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
