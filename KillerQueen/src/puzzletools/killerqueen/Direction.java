package puzzletools.killerqueen;

import java.util.HashSet;
import java.util.Set;

public class Direction {
	public static final Direction NORTH = new Direction(-1, 0);
	public static final Direction SOUTH = new Direction(1, 0);
	public static final Direction EAST = new Direction(0, 1);
	public static final Direction WEST = new Direction(0, -1);
	public static final Direction NORTHEAST = new Direction(-1, 1);
	public static final Direction NORTHWEST = new Direction(-1, -1);
	public static final Direction SOUTHEAST = new Direction(1, 1);
	public static final Direction SOUTHWEST = new Direction(1, -1);
	public static final Set<Direction> CARDINALS;
	public static final Set<Direction> COMPASS;

	static {
		CARDINALS = new HashSet<>();
		CARDINALS.add(NORTH);
		CARDINALS.add(SOUTH);
		CARDINALS.add(EAST);
		CARDINALS.add(WEST);

		COMPASS = new HashSet<>();
		COMPASS.addAll(CARDINALS);
		COMPASS.add(NORTHEAST);
		COMPASS.add(NORTHWEST);
		COMPASS.add(SOUTHEAST);
		COMPASS.add(SOUTHWEST);
	}

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
