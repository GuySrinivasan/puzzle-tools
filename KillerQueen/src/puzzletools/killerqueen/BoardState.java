package puzzletools.killerqueen;

import java.util.Map;

public class BoardState {
	// private String state;
	private int[][] robotPosition;
	private boolean[] robotAlive;
	// private int[][] lastRobotPosition;
	private int[] queenPosition;
	private BoardState previous;

	public BoardState(String state) {
		this(state, null);
	}

	public BoardState(String state, BoardState history) {
		// this.state = state;
		// this.previous = history;
		robotPosition = new int[4][];
		robotAlive = new boolean[4];
		// lastRobotPosition = new int[4][];
		// queenPosition = new int[2];
		String[] pieces = state.split(";");
		for (int i = 0; i < 4; i++) {
			int[] position = getPosition(pieces[i]);
			if (position == null) {
				robotPosition[i] = Position.NONE;
			} else {
				robotPosition[i] = position;
			}
			// robotPosition[i][0] = position[0];
			// robotPosition[i][1] = position[1];
		}
		int living = Integer.parseInt(pieces[4]);
		for (int i = 0; i < 4; i++) {
			robotAlive[i] = (living >> i) % 2 == 1;
		}
		// for (int i = 4; i < 8; i++) {
		// int[] position = getPosition(pieces[i]);
		// if (position == null) {
		// lastRobotPosition[i - 4] = robotPosition[i - 4];
		// // lastRobotPosition[i - 4][0] = robotPosition[i - 4][0];
		// // lastRobotPosition[i - 4][1] = robotPosition[i - 4][1];
		// } else {
		// lastRobotPosition[i - 4] = position;
		// // lastRobotPosition[i - 4][0] = position[0];
		// // lastRobotPosition[i - 4][1] = position[1];
		// }
		// }
		queenPosition = getPosition(pieces[5]);
	}

	private int[] getPosition(String pos) {
		if (pos.equals("")) {
			return null;
		}
		String[] coords = pos.split(",");
		return Position.of(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
		// return new int[] { Integer.parseInt(coords[0]),
		// Integer.parseInt(coords[1]) };
	}

	public String getStateString() {
		String myState = regenerateStateString();
		return myState;
	}

	public String getStateString(Map<Long, Long> previousState) {
		String myState = regenerateStateString();
		if (previousState.get(getTinyState()) == null) {
			return myState;
		} else {
			return new BoardState(previousState.get(getTinyState())).getStateString(previousState) + " / " + myState;
		}
	}

	public int getRobotRow(int robot) {
		return robotAlive[robot] ? robotPosition[robot][0] : -1;
	}

	public int getRobotColumn(int robot) {
		return robotAlive[robot] ? robotPosition[robot][1] : -1;
	}

	public int getQueenRow() {
		return queenPosition[0];
	}

	public int getQueenColumn() {
		return queenPosition[1];
	}

	public BoardState moveRobot(int robot, int nextRobotRow, int nextRobotColumn) {
		// BoardState copy = new BoardState(state,
		// getHistory() + ";m" + robot + ":" + nextRobotRow + "," +
		// nextRobotColumn);
		BoardState copy = new BoardState(getTinyState());
		copy.setRobotPosition(robot, nextRobotRow, nextRobotColumn);
		return copy;
	}

	private void setRobotPosition(int robot, int nextRobotRow, int nextRobotColumn) {
		robotPosition[robot] = Position.of(nextRobotRow, nextRobotColumn);
		// lastRobotPosition[robot] = Position.of(nextRobotRow,
		// nextRobotColumn);
		// robotPosition[robot][0] = nextRobotRow;
		// robotPosition[robot][1] = nextRobotColumn;
		// lastRobotPosition[robot][0] = nextRobotRow;
		// lastRobotPosition[robot][1] = nextRobotColumn;
		// regenerateStateString();
	}

	private String regenerateStateString() {
		StringBuilder newState = new StringBuilder();
		for (int robot = 0; robot < 4; robot++) {
			if (robotPosition[robot] == Position.NONE) {
				newState.append("");
			} else {
				newState.append(robotPosition[robot][0] + "," + robotPosition[robot][1]);
			}
			newState.append(";");
		}
		int living = 0;
		for (int robot = 0; robot < 4; robot++) {
			if (robotAlive[robot]) {
				living |= (1 << robot);
			}
			// if (lastRobotPosition[robot][0] == robotPosition[robot][0]
			// && lastRobotPosition[robot][1] == robotPosition[robot][1]) {
			// newState.append("");
			// } else {
			// newState.append(lastRobotPosition[robot][0] + "," +
			// lastRobotPosition[robot][1]);
			// }
			// newState.append(";");
		}
		newState.append(living);
		newState.append(";");
		newState.append(queenPosition[0] + "," + queenPosition[1]);
		return newState.toString();
	}

	public BoardState moveQueenCapturingRobot(int robot) {
		// BoardState copy = new BoardState(state, getHistory() + ";Qx" +
		// robot);
		BoardState copy = new BoardState(getTinyState());
		copy.captureRobot(robot);
		return copy;
	}

	private void captureRobot(int robot) {
		queenPosition = robotPosition[robot];
		// queenPosition[0] = robotPosition[robot][0];
		// queenPosition[1] = robotPosition[robot][1];
		// robotPosition[robot] = Position.NONE;
		robotAlive[robot] = false;
		// robotPosition[robot][0] = -1;
		// robotPosition[robot][1] = -1;
		// regenerateStateString();
	}

	public String getHistory() {
		// return previous;
		if (previous == null) {
			return "=> ";
		} else {
			// return previous.getHistory() + state + " . ";
			return "N/A";
		}
		// return previous;
	}

	public boolean isQueenAlignedWithRobot(int robot) {
		if (robotPosition[robot] == Position.NONE) {
			return false;
		}
		// if (robotPosition[robot][0] == -1)
		// return false;
		int rowdiff = queenPosition[0] - robotPosition[robot][0];
		int coldiff = queenPosition[1] - robotPosition[robot][1];
		return rowdiff == 0 || coldiff == 0 || rowdiff == coldiff || rowdiff == -coldiff;
	}

	public boolean isRobotAlive(int robot) {
		return robotAlive[robot];
	}

	public long getTinyState() {
		long tinyState = 0;
		for (int r = 0; r < 4; r++) {
			tinyState = tinyState << 4;
			tinyState += robotPosition[r][0];
			tinyState = tinyState << 4;
			tinyState += robotPosition[r][1];
		}
		tinyState = tinyState << 4;
		tinyState += queenPosition[0];
		tinyState = tinyState << 4;
		tinyState += queenPosition[1];
		for (int r = 0; r < 4; r++) {
			tinyState = tinyState << 1;
			tinyState += robotAlive[r] ? 1 : 0;
		}
		return tinyState;
	}

	public BoardState(long tinyState) {
		robotPosition = new int[4][];
		robotAlive = new boolean[4];
		for (int r = 3; r >= 0; r--) {
			robotAlive[r] = tinyState % 2 == 1;
			tinyState = tinyState >> 1;
		}
		int queenColumn = (int) (tinyState % 16);
		tinyState = tinyState >> 4;
		int queenRow = (int) (tinyState % 16);
		tinyState = tinyState >> 4;
		queenPosition = Position.of(queenRow, queenColumn);
		for (int r = 3; r >= 0; r--) {
			int robotColumn = (int) (tinyState % 16);
			tinyState = tinyState >> 4;
			int robotRow = (int) (tinyState % 16);
			tinyState = tinyState >> 4;
			robotPosition[r] = Position.of(robotRow, robotColumn);
		}
	}
}
