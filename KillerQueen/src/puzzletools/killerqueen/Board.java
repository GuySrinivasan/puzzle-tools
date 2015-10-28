package puzzletools.killerqueen;

import java.util.HashSet;
import java.util.Set;

public class Board {
	private static final boolean T = true;
	private static final boolean F = false;
	private static final boolean[][] MOVE_N = new boolean[][] {
		{F,F,F,F,F,F,F,F},
		{T,T,T,T,T,T,T,T},
		{T,F,T,T,T,T,T,T},
		{T,T,T,F,F,T,F,T},
		{T,T,T,T,T,T,T,T},
		{T,T,T,F,F,T,T,T},
		{T,T,F,T,T,T,F,T},
		{T,T,T,T,T,T,T,T},
	};
	private static final boolean[][] MOVE_S = new boolean[][] {
		{T,T,T,T,T,T,T,T},
		{T,F,T,T,T,T,T,T},
		{T,T,T,F,F,T,F,T},
		{T,T,T,T,T,T,T,T},
		{T,T,T,F,F,T,T,T},
		{T,T,F,T,T,T,F,T},
		{T,T,T,T,T,T,T,T},
		{F,F,F,F,F,F,F,F},
	};
	private static final boolean[][] MOVE_E = new boolean[][] {
		{T,T,T,T,T,T,T,F},
		{T,T,T,T,T,T,T,F},
		{F,T,T,T,T,T,F,F},
		{T,T,F,T,F,T,T,F},
		{T,T,F,T,F,T,T,F},
		{T,T,T,T,T,T,T,F},
		{T,T,F,T,T,F,T,F},
		{T,T,T,T,T,T,T,F},
	};
	private static final boolean[][] MOVE_W = new boolean[][] {
		{F,T,T,T,T,T,T,T},
		{F,T,T,T,T,T,T,T},
		{F,F,T,T,T,T,T,F},
		{F,T,T,F,T,F,T,T},
		{F,T,T,F,T,F,T,T},
		{F,T,T,T,T,T,T,T},
		{F,T,T,F,T,T,F,T},
		{F,T,T,T,T,T,T,T},
	};
	private int destRow;
	private int destColumn;
	private int targetRobot;
	
	public Board(int destRow, int destColumn, int targetRobot) {
		this.destRow = destRow;
		this.destColumn = destColumn;
		this.targetRobot = targetRobot;
	}
	public boolean isSolution(BoardState state) {
		boolean rightRow = state.getRobotRow(targetRobot) == destRow;
		boolean rightColumn = state.getRobotColumn(targetRobot) == destColumn;
		return rightRow && rightColumn;
	}
	public Set<BoardState> getSuccessors(BoardState state) {
		Set<BoardState> successors = new HashSet<>();
		for (int i = 0; i < 4; i++) {
			
		}
		return new HashSet<>();
	}
}
