package puzzletools.killerqueen;

public class BoardState {
	private String state;
	private int[][] robotPosition;
	private int[][] lastRobotPosition;
	private int[] queenPosition;
	
	public BoardState(String state) {
		this.state = state;
		String[] pieces = state.split(";");
		for (int i = 0; i < 4; i++) {
			int[] position = getPosition(pieces[i]);
			robotPosition[i][0] = position[0];
			robotPosition[i][1] = position[1];
		}
		for (int i = 4; i < 8; i++) {
			int[] position = getPosition(pieces[i]);
			lastRobotPosition[i-4][0] = position[0];
			lastRobotPosition[i-4][1] = position[1];
		}
		queenPosition = getPosition(pieces[8]);
	}
	private int[] getPosition(String pos) {
		if (pos.equals("D")) {
			return new int[] {-1,-1};
		}
		String[] coords = pos.split(",");
		return new int[] { Integer.parseInt(coords[0]), Integer.parseInt(coords[1]) };
	}
	public String getStateString() {
		return state;
	}
	public int getRobotRow(int robot) {
		return robotPosition[robot][0];
	}
	public int getRobotColumn(int robot) {
		return robotPosition[robot][1];
	}
}
