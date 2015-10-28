package puzzletools.killerqueen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Runner {
	public static void main(String[] args) {
		// where each robot is, where the queen is, where each robot last was
		// how big is a level? who cares
		// what does a level look like
		// can have four levels one for each direction you could move
		Board board = new Board(2 /* destination row */,1 /* destination column */,0 /* target robot */);
		// queen at 2,1
		BoardState initialState = new BoardState("3,2;1,5;7,5;4,5;3,2;1,5;7,5;4,5;2,1");
		
		Map<Integer, Set<String>> solutions = new HashMap<>();
		solutions.put(0, new HashSet<>());
		
		Map<String, Boolean> isSolution = new HashMap<>();
		isSolution.put(initialState.getStateString(), false);
		
		Queue<BoardState> queue = new LinkedList<>();
		queue.add(initialState);
		Queue<BoardState> nextQueue = new LinkedList<>();
		int moves = 0;
		while(!queue.isEmpty() && moves < 10) {
			BoardState state = queue.poll();
			if (isSolution.containsKey(state.getStateString())) {
				if (isSolution.get(state.getStateString())) {
					solutions.get(moves).add(state.getStateString());
				}
			} else {
				// not yet seen, find successor states and add to queue
				isSolution.put(state.getStateString(), board.isSolution(state));
				for (BoardState successor : board.getSuccessors(state)) {
					if (!isSolution.containsKey(successor.getStateString())) {
						nextQueue.add(successor);
					}
				}
			}
			if (queue.isEmpty()) {
				if (solutions.get(moves).size() > 0) {
					break;
				}
				queue = nextQueue;
				nextQueue = new LinkedList<>();
				moves++;
				solutions.put(moves, new HashSet<>());
			}
		}
		Set<String> allResults = new HashSet<>();
		for (String solution : solutions.get(moves)) {
			allResults.add(new BoardState(solution).getStateString());
		}
		for (String result : allResults) {
			System.out.println(result);
		}
	}
}
