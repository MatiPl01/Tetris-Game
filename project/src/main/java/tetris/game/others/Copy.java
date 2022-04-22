package tetris.game.others;

public final class Copy {
    public static int[][] copy2DArray(int[][] arr) {
        int h = arr.length;
        int w = arr[0].length;
        int[][] copy = new int[h][w];

        for (int y = 0; y < h; y++) {
            System.arraycopy(arr[y], 0, copy[y],0, w);
        }

        return copy;
    }
}
