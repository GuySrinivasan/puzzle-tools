package puzzletools.killerqueen;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Runner {
	public static void main(String[] args) {
		// where each robot is, where the queen is, where each robot last was
		// how big is a level? who cares
		// what does a level look like
		// can have four levels one for each direction you could move
		int queenRow = 11;
		int queenColumn = 13;
		Board board = new Board(queenRow /* destination row */,
				queenColumn /* destination column */, 1 /* target robot */);
//		BoardState initialState = new BoardState("3,2;1,5;7,5;4,5;3,2;1,5;7,5;4,5;"+queenRow+","+queenColumn);
		BoardState initialState = new BoardState("7,5;6,2;0,12;11,11;7,5;6,2;0,12;11,11;"+queenRow+","+queenColumn);

		Map<Integer, Map<String, BoardState>> solutions = new HashMap<>();
		solutions.put(0, new HashMap<>());

		Map<String, Boolean> isSolution = new HashMap<>();
		// isSolution.put(initialState.getStateString(), false);

		Queue<BoardState> queue = new LinkedList<>();
		queue.add(initialState);
		Queue<BoardState> nextQueue = new LinkedList<>();
		int moves = 0;
		long start = new Date().getTime();
		while (!queue.isEmpty() && moves < 14) {
			BoardState state = queue.poll();
			if (!isSolution.containsKey(state.getStateString())) {
				// not yet seen, find successor states and add to queue
				isSolution.put(state.getStateString(), board.isSolution(state));
				for (BoardState successor : board.getSuccessors(state)) {
					if (!isSolution.containsKey(successor.getStateString())) {
						nextQueue.add(successor);
					}
				}
			}
			if (isSolution.get(state.getStateString())) {
				solutions.get(moves).put(state.getStateString(), state);
			}
			if (queue.isEmpty()) {
				long now = new Date().getTime();
				System.out.println("finished " + moves + " in " + (now-start)/1000 + " seconds; next queue has " + nextQueue.size() + " positions");
				if (solutions.get(moves).size() > 0) {
					break;
				}
				queue = nextQueue;
				nextQueue = new LinkedList<>();
				moves++;
				solutions.put(moves, new HashMap<>());
			}
		}
		Set<String> allResults = new HashSet<>();
		for (String solution : solutions.get(moves).keySet()) {
			allResults.add(solutions.get(moves).get(solution).getHistory());
		}
		for (String result : allResults) {
			System.out.println(result);
		}
	}
}
