package puzzletools.killerqueen;

public class BoardState {
	private String state;
	private int[][] robotPosition;
	private int[][] lastRobotPosition;
	private int[] queenPosition;
	private String previous;

	public BoardState(String state) {
		this(state, "");
	}

	public BoardState(String state, String history) {
		this.state = state;
		this.previous = history;
		robotPosition = new int[4][2];
		lastRobotPosition = new int[4][2];
		queenPosition = new int[2];
		String[] pieces = state.split(";");
		for (int i = 0; i < 4; i++) {
			int[] position = getPosition(pieces[i]);
			robotPosition[i][0] = position[0];
			robotPosition[i][1] = position[1];
		}
		for (int i = 4; i < 8; i++) {
			int[] position = getPosition(pieces[i]);
			if (position == null) {
				lastRobotPosition[i - 4][0] = robotPosition[i - 4][0];
				lastRobotPosition[i - 4][1] = robotPosition[i - 4][1];
			} else {
				lastRobotPosition[i - 4][0] = position[0];
				lastRobotPosition[i - 4][1] = position[1];
			}
		}
		queenPosition = getPosition(pieces[8]);
	}

	private int[] getPosition(String pos) {
		if (pos.equals("")) {
			return null;
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

	public int getQueenRow() {
		return queenPosition[0];
	}

	public int getQueenColumn() {
		return queenPosition[1];
	}

	public BoardState moveRobot(int robot, int nextRobotRow, int nextRobotColumn) {
		BoardState copy = new BoardState(state,
				getHistory() + ";m" + robot + ":" + nextRobotRow + "," + nextRobotColumn);
		copy.setRobotPosition(robot, nextRobotRow, nextRobotColumn);
		return copy;
	}

	private void setRobotPosition(int robot, int nextRobotRow, int nextRobotColumn) {
		robotPosition[robot][0] = nextRobotRow;
		robotPosition[robot][1] = nextRobotColumn;
		lastRobotPosition[robot][0] = nextRobotRow;
		lastRobotPosition[robot][1] = nextRobotColumn;
		regenerateStateString();
	}

	private void regenerateStateString() {
		StringBuilder newState = new StringBuilder();
		for (int robot = 0; robot < 4; robot++) {
			newState.append(robotPosition[robot][0] + "," + robotPosition[robot][1]);
			newState.append(";");
		}
		for (int robot = 0; robot < 4; robot++) {
			if (lastRobotPosition[robot][0] == robotPosition[robot][0]
					&& lastRobotPosition[robot][1] == robotPosition[robot][1]) {
				newState.append("");
			} else {
				newState.append(lastRobotPosition[robot][0] + "," + lastRobotPosition[robot][1]);
			}
			newState.append(";");
		}
		newState.append(queenPosition[0] + "," + queenPosition[1]);
		state = newState.toString();
	}

	public BoardState moveQueenCapturingRobot(int robot) {
		BoardState copy = new BoardState(state, getHistory() + ";Qx" + robot);
		copy.captureRobot(robot);
		return copy;
	}

	private void captureRobot(int robot) {
		queenPosition[0] = robotPosition[robot][0];
		queenPosition[1] = robotPosition[robot][1];
		robotPosition[robot][0] = -1;
		robotPosition[robot][1] = -1;
		regenerateStateString();
	}

	public String getHistory() {
		return previous;
	}

	public boolean isQueenAlignedWithRobot(int robot) {
		if (robotPosition[robot][0] == -1)
			return false;
		int rowdiff = queenPosition[0] - robotPosition[robot][0];
		int coldiff = queenPosition[1] - robotPosition[robot][1];
		return rowdiff == 0 || coldiff == 0 || rowdiff == coldiff || rowdiff == -coldiff;
	}
}
