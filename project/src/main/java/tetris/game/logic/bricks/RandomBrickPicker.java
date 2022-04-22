package tetris.game.logic.bricks;

import tetris.game.logic.bricks.shapes.*;
import tetris.game.others.Random;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickPicker {
    private final static int MAX_BRICKS_TO_GET = 7;

    private final List<Brick> normalBricks = new ArrayList<>();
    private final List<Brick> largeBricks = new ArrayList<>();
    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    private final boolean allowLarge;

    public RandomBrickPicker() {
        this.allowLarge = false;
        addNormalBricks();
    }

    public RandomBrickPicker(boolean allowLarge) {
        this.allowLarge = allowLarge;
        addNormalBricks();
        addLargeBricks();
    }

    public Brick getNextBrick() {
        updateNextBricks(1);
        return nextBricks.poll();
    }

    public List<Brick> peekNextBricks(int count) {
        updateNextBricks(count);
        List<Brick> peekedBricks = new ArrayList<>();
        Iterator<Brick> it = nextBricks.iterator();

        for (int i = 0; i < count; i++) {
            try {
                peekedBricks.add(it.next());
            } catch (NoSuchElementException e) {
                addNextRandomBrick();
            }
        }

        return peekedBricks;
    }

    private void addNormalBricks() {
        normalBricks.add(new IBrick());
        normalBricks.add(new JBrick());
        normalBricks.add(new LBrick());
        normalBricks.add(new OBrick());
        normalBricks.add(new SBrick());
        normalBricks.add(new TBrick());
        normalBricks.add(new ZBrick());
    }

    private void addLargeBricks() {
        largeBricks.add(new IBrick(true));
        largeBricks.add(new JBrick(true));
        largeBricks.add(new LBrick(true));
        largeBricks.add(new OBrick(true));
        largeBricks.add(new SBrick(true));
        largeBricks.add(new TBrick(true));
        largeBricks.add(new ZBrick(true));
    }

    private void updateNextBricks(int count) {
        if (count > MAX_BRICKS_TO_GET) {
            throw new IllegalArgumentException("Cannot get more than " + MAX_BRICKS_TO_GET + " bricks at once");
        }
        int countDelta = count - nextBricks.size();

        // Generate next random bricks if there are not enough bricks generated
        for (int i = 0; i < countDelta; i++) addNextRandomBrick();
    }

    private void addNextRandomBrick() {
        Brick nextBrick;

        if (allowLarge && Random.random() > .5) {
            nextBrick = largeBricks.get(ThreadLocalRandom.current().nextInt(largeBricks.size()));
        } else {
            nextBrick = normalBricks.get(ThreadLocalRandom.current().nextInt(normalBricks.size()));
        }

        nextBricks.add(nextBrick);
    }
}
