package puzzletools.killerqueen;

public class Direction {
	public static final Direction NORTH = new Direction(0, -1);
	public static final Direction SOUTH = new Direction(0, 1);
	public static final Direction EAST = new Direction(1, 0);
	public static final Direction WEST = new Direction(-1, 0);
	
	private final int rowDir;
	private final int columnDir;
	
	public Direction(int rowDir, int columnDir) {
		this.rowDir = rowDir;
		this.columnDir = columnDir;
	}
	public int getRowDir() {
		return rowDir;
	}
	public int getColumnDir() {
		return columnDir;
	}
}
