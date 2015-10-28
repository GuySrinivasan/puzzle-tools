package puzzletools.killerqueen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	private static final int MAX_SIZE = 16;
	private static final boolean E = true;
	private static final boolean W = false;
	private static final boolean[][] MOVE_N = new boolean[][] { //
			{ W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W }, //
			{ E, E, W, E, E, E, E, E, E, E, E, E, E, W, E, E }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E }, //
			{ E, E, E, E, E, E, E, E, E, W, E, E, E, E, E, E }, //

			{ E, E, E, E, E, E, W, E, E, E, E, E, E, E, E, E }, //
			{ W, E, E, E, W, E, E, E, E, E, E, E, E, E, E, W }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E }, //
			{ E, W, E, E, E, E, E, W, W, E, W, E, E, E, W, E }, //

			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E }, //
			{ E, E, E, E, E, E, E, W, W, E, E, E, E, E, E, E }, //
			{ E, E, E, E, W, E, W, E, E, E, E, E, E, E, E, W }, //
			{ W, E, E, E, E, E, E, E, W, E, E, E, E, W, E, E }, //

			{ E, E, E, E, E, E, E, W, E, E, E, E, E, E, E, E }, //
			{ E, W, E, E, E, E, E, E, E, E, E, E, E, E, E, E }, //
			{ E, E, E, E, E, E, E, E, E, W, E, E, E, E, W, E }, //
			{ E, E, E, W, E, E, E, E, E, E, E, E, E, E, E, E }, //
	};
	private static final boolean[][] MOVE_S = new boolean[][] { //
			{ E, E, W, E, E, E, E, E, E, E, E, E, E, W, E, E }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ E, E, E, E, E, E, E, E, E, W, E, E, E, E, E, E, }, //
			{ E, E, E, E, E, E, W, E, E, E, E, E, E, E, E, E, }, //

			{ W, E, E, E, W, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ E, W, E, E, E, E, E, W, W, E, W, E, E, E, W, E, }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //

			{ E, E, E, E, E, E, E, W, W, E, E, E, E, E, E, E, }, //
			{ E, E, E, E, W, E, W, E, E, E, E, E, E, E, E, W, }, //
			{ W, E, E, E, E, E, E, E, W, E, E, E, E, W, E, E, }, //
			{ E, E, E, E, E, E, E, W, E, E, E, E, E, E, E, E, }, //

			{ E, W, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ E, E, E, E, E, E, E, E, E, W, E, E, E, E, W, E, }, //
			{ E, E, E, W, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, }, //
	};
	private static final boolean[][] MOVE_E = new boolean[][] { //
			{ E, E, E, E, W, E, E, E, E, W, E, E, E, E, E, W, }, //
			{ E, W, E, E, E, E, E, E, E, E, E, E, E, W, E, W, }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ E, E, E, E, E, W, E, E, W, E, E, E, E, E, E, W, }, //

			{ E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ E, E, E, E, W, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ E, W, E, E, E, E, E, E, E, E, W, E, E, W, E, W, }, //
			{ E, E, E, E, E, E, W, E, W, E, E, E, E, E, E, W, }, //

			{ E, E, E, E, E, E, W, E, W, E, E, E, E, E, E, W, }, //
			{ E, E, E, W, E, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ E, E, E, E, E, W, E, E, W, E, E, E, E, E, E, W, }, //
			{ E, E, E, E, E, E, E, E, E, E, E, E, W, E, E, W, }, //

			{ E, E, E, E, E, E, E, W, E, E, E, E, E, E, E, W, }, //
			{ E, W, E, E, E, E, E, E, W, E, E, E, E, E, E, W, }, //
			{ E, E, E, W, E, E, E, E, E, E, E, E, E, E, W, W, }, //
			{ E, E, E, E, W, E, E, E, E, E, E, W, E, E, E, W, }, //
	};
	private static final boolean[][] MOVE_W = new boolean[][] { //
			{ W, E, E, E, E, W, E, E, E, E, W, E, E, E, E, E, }, //
			{ W, E, W, E, E, E, E, E, E, E, E, E, E, E, W, E, }, //
			{ W, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ W, E, E, E, E, E, W, E, E, W, E, E, E, E, E, E, }, //

			{ W, E, E, E, E, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ W, E, E, E, E, W, E, E, E, E, E, E, E, E, E, E, }, //
			{ W, E, W, E, E, E, E, E, E, E, E, W, E, E, W, E, }, //
			{ W, E, E, E, E, E, E, W, E, W, E, E, E, E, E, E, }, //

			{ W, E, E, E, E, E, E, W, E, W, E, E, E, E, E, E, }, //
			{ W, E, E, E, W, E, E, E, E, E, E, E, E, E, E, E, }, //
			{ W, E, E, E, E, E, W, E, E, W, E, E, E, E, E, E, }, //
			{ W, E, E, E, E, E, E, E, E, E, E, E, E, W, E, E, }, //

			{ W, E, E, E, E, E, E, E, W, E, E, E, E, E, E, E, }, //
			{ W, E, W, E, E, E, E, E, E, W, E, E, E, E, E, E, }, //
			{ W, E, E, E, W, E, E, E, E, E, E, E, E, E, E, W, }, //
			{ W, E, E, E, E, W, E, E, E, E, E, E, W, E, E, E, }, //
	};
	// private static final boolean[][] MOVE_N = new boolean[][] { //
	// { F, F, F, F, F, F, F, F }, //
	// { T, T, T, T, T, T, T, T }, //
	// { T, F, T, T, T, T, T, T }, //
	// { T, T, T, F, F, T, F, T }, //
	// { T, T, T, T, T, T, T, T }, //
	// { T, T, T, F, F, T, T, T }, //
	// { T, T, F, T, T, T, F, T }, //
	// { T, T, T, T, T, T, T, T }, };
	// private static final boolean[][] MOVE_S = new boolean[][] { //
	// { T, T, T, T, T, T, T, T }, //
	// { T, F, T, T, T, T, T, T }, //
	// { T, T, T, F, F, T, F, T }, //
	// { T, T, T, T, T, T, T, T }, //
	// { T, T, T, F, F, T, T, T }, //
	// { T, T, F, T, T, T, F, T }, //
	// { T, T, T, T, T, T, T, T }, //
	// { F, F, F, F, F, F, F, F }, //
	// };
	// private static final boolean[][] MOVE_E = new boolean[][] { //
	// { T, T, T, T, T, T, T, F }, //
	// { T, T, T, T, T, T, T, F }, //
	// { F, T, T, T, T, T, F, F }, //
	// { T, T, F, T, F, T, T, F }, //
	// { T, T, F, T, F, T, T, F }, //
	// { T, T, T, T, T, T, T, F }, //
	// { T, T, F, T, T, F, T, F }, //
	// { T, T, T, T, T, T, T, F }, //
	// };
	// private static final boolean[][] MOVE_W = new boolean[][] { //
	// { F, T, T, T, T, T, T, T }, //
	// { F, T, T, T, T, T, T, T }, //
	// { F, F, T, T, T, T, T, F }, //
	// { F, T, T, F, T, F, T, T }, //
	// { F, T, T, F, T, F, T, T }, //
	// { F, T, T, T, T, T, T, T }, //
	// { F, T, T, F, T, T, F, T }, //
	// { F, T, T, T, T, T, T, T }, //
	// };
	private int destRow;
	private int destColumn;
	private int targetRobot;
	private final Map<Direction, boolean[][]> moveable;

	public Board(int destRow, int destColumn, int targetRobot) {
		this.destRow = destRow;
		this.destColumn = destColumn;
		this.targetRobot = targetRobot;
		moveable = new HashMap<>();
		moveable.put(Direction.NORTH, MOVE_N);
		moveable.put(Direction.SOUTH, MOVE_S);
		moveable.put(Direction.EAST, MOVE_E);
		moveable.put(Direction.WEST, MOVE_W);
		// ne
		{
			moveable.put(Direction.NORTHEAST, new boolean[MAX_SIZE][MAX_SIZE]);
			for (int i = 0; i < MAX_SIZE; i++) {
				for (int j = 0; j < MAX_SIZE; j++) {
					moveable.get(Direction.NORTHEAST)[i][j] = (MOVE_N[i][j]
							&& MOVE_E[i + Direction.NORTH.getRowDir()][j + Direction.NORTH.getColumnDir()])
							|| (MOVE_E[i][j]
									&& MOVE_N[i + Direction.EAST.getRowDir()][j + Direction.EAST.getColumnDir()]);
				}
			}
		}
		// nw
		{
			moveable.put(Direction.NORTHWEST, new boolean[MAX_SIZE][MAX_SIZE]);
			for (int i = 0; i < MAX_SIZE; i++) {
				for (int j = 0; j < MAX_SIZE; j++) {
					moveable.get(Direction.NORTHWEST)[i][j] = (MOVE_N[i][j]
							&& MOVE_W[i + Direction.NORTH.getRowDir()][j + Direction.NORTH.getColumnDir()])
							|| (MOVE_W[i][j]
									&& MOVE_N[i + Direction.WEST.getRowDir()][j + Direction.WEST.getColumnDir()]);
				}
			}
		}
		// se
		{
			moveable.put(Direction.SOUTHEAST, new boolean[MAX_SIZE][MAX_SIZE]);
			for (int i = 0; i < MAX_SIZE; i++) {
				for (int j = 0; j < MAX_SIZE; j++) {
					moveable.get(Direction.SOUTHEAST)[i][j] = (MOVE_S[i][j]
							&& MOVE_E[i + Direction.SOUTH.getRowDir()][j + Direction.SOUTH.getColumnDir()])
							|| (MOVE_E[i][j]
									&& MOVE_S[i + Direction.EAST.getRowDir()][j + Direction.EAST.getColumnDir()]);
				}
			}
		}
		// sw
		{
			moveable.put(Direction.SOUTHWEST, new boolean[MAX_SIZE][MAX_SIZE]);
			for (int i = 0; i < MAX_SIZE; i++) {
				for (int j = 0; j < MAX_SIZE; j++) {
					moveable.get(Direction.SOUTHWEST)[i][j] = (MOVE_S[i][j]
							&& MOVE_W[i + Direction.SOUTH.getRowDir()][j + Direction.SOUTH.getColumnDir()])
							|| (MOVE_W[i][j]
									&& MOVE_S[i + Direction.WEST.getRowDir()][j + Direction.WEST.getColumnDir()]);
				}
			}
		}
	}

	public boolean isSolution(BoardState state) {
		boolean rightRow = state.getRobotRow(targetRobot) == destRow;
		boolean rightColumn = state.getRobotColumn(targetRobot) == destColumn;
		return rightRow && rightColumn;
	}

	public Set<BoardState> getSuccessors(BoardState state) {
		Set<BoardState> successors = new HashSet<>();
		for (int robot = 0; robot < 4; robot++) {
			for (Direction direction : Direction.CARDINALS) {
				successors.add(getSuccessor(state, robot, direction));
			}
		}
		return successors;
	}

	private BoardState getSuccessor(BoardState state, int robot, Direction direction) {
		// move robot in direction as far as it can go, ask state for a new
		// state with moved robot, evaluate queen movement, ask new state for a
		// new state with moved queen if necessary, return final state

		int nextRobotRow = state.getRobotRow(robot);
		int nextRobotColumn = state.getRobotColumn(robot);
		if (nextRobotRow == -1 || nextRobotColumn == -1) {
			// robot is gone; don't change state
			return state;
		}
		while (canMoveOneSpace(nextRobotRow, nextRobotColumn, direction, state)) {
			nextRobotRow += direction.getRowDir();
			nextRobotColumn += direction.getColumnDir();
		}
		BoardState movedRobot = state.moveRobot(robot, nextRobotRow, nextRobotColumn);
		// could cache everywhere queen can threaten? can simplify because if
		// queen moves it is to take this robot? nope
		BoardState afterQueen = evaluateQueen(movedRobot);
		return afterQueen;
	}

	/**
	 * after a robot or queen has moved, evaluate whether the queen should
	 * capture robots and if so return the board state after such captures
	 * 
	 * @param intermediateState
	 * @return
	 */
	private BoardState evaluateQueen(BoardState intermediateState) {
		// first check if queen threatens exactly 1 robot. if not, return state.
		// if so, ask for a new board state with queen moving, then call
		// evaluateQueen again and return the result

		boolean possibleRobot = false;
		for (int robot = 0; robot < 4 && !possibleRobot; robot++) {
			if (intermediateState.isQueenAlignedWithRobot(robot)) {
				possibleRobot = true;
			}
		}
		if (!possibleRobot) {
			return intermediateState;
		}

		Set<Integer> threatenedRobots = new HashSet<>();
		for (Direction direction : Direction.COMPASS) {
			Integer robot = findThreatenedRobot(intermediateState, direction);
			if (robot != null) {
				threatenedRobots.add(robot);
			}
			if (threatenedRobots.size() > 1) {
				break;
			}
		}
		if (threatenedRobots.size() != 1) {
			return intermediateState;
		} else {
			BoardState afterQueen = intermediateState.moveQueenCapturingRobot(threatenedRobots.iterator().next());
			return evaluateQueen(afterQueen);
		}
	}

	private Integer findThreatenedRobot(BoardState state, Direction direction) {
		int queenRow = state.getQueenRow();
		int queenColumn = state.getQueenColumn();
		while (moveable.get(direction)[queenRow][queenColumn]) {
			queenRow += direction.getRowDir();
			queenColumn += direction.getColumnDir();
			for (int robot = 0; robot < 4; robot++) {
				if (state.getRobotRow(robot) == queenRow && state.getRobotColumn(robot) == queenColumn) {
					return robot;
				}
			}
		}
		return null;
	}

	private boolean canMoveOneSpace(int row, int column, Direction direction, BoardState state) {
		boolean noWalls = moveable.get(direction)[row][column];
		boolean noPiece = state.getQueenRow() != row + direction.getRowDir()
				|| state.getQueenColumn() != column + direction.getColumnDir();
		for (int i = 0; i < 4; i++) {
			noPiece &= state.getRobotRow(i) != row + direction.getRowDir()
					|| state.getRobotColumn(i) != column + direction.getColumnDir();
		}
		return noWalls && noPiece;
	}
}
