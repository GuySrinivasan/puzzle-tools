package puzzletools.killerqueen;

public class Position {
	public static final int[] NONE = new int[] { -1, -1 };
	private static int[][][] positions;

	static {
		positions = new int[100][100][2];
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				positions[i][j][0] = i;
				positions[i][j][1] = j;
			}
		}
	}

	public static int[] of(int x, int y) {
		return positions[x][y];
	}
}
