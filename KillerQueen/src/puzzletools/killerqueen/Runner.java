package puzzletools.killerqueen;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Runner {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		// where each robot is, where the queen is, where each robot last was
		// how big is a level? who cares
		// what does a level look like
		// can have four levels one for each direction you could move
		PrintWriter writer = new PrintWriter("killerqueen_solutions.txt", "UTF-8");
		int[][] queenPossibilities = new int[][] {
			{1, 2},
			{3, 6},
			{5,4},
			{6,1},
			{1,13},
			{3,9},
			{6,10},
			{6,14},
			{9,4},
			{10,6},
			{12,7},
			{13,1},
			{14,3},
			{10,8},
			{11,13},
			{13,9},
			{14,14},
		};
		System.out.println("Queen row\tqueen column\trobot\tsize of solution\tnumber of solutions");
		System.out.println("=====");
		writer.println("Queen row\tqueen column\trobot\tsize of solution\tnumber of solutions");
		writer.println("=====");
		for (int i = 0; i < 17; i++) {
			for (int r = 0; r < 4; r++) {
				int queenRow = queenPossibilities[i][0];
				int queenColumn = queenPossibilities[i][1];
				int targetRobot = r;
				String solutions = computeSolutions(queenRow, queenColumn, targetRobot);
				System.out.println(solutions);
				System.out.println("=====");
				writer.print(solutions);
				writer.println("=====");
			}
		}
		writer.close();
	}

	private static String computeSolutions(int queenRow, int queenColumn, int targetRobot) {
		StringBuilder solutionsString = new StringBuilder();
		Board board = new Board(queenRow /* destination row */,
				queenColumn /* destination column */,
				targetRobot /* target robot */);
		// board.setSolutionStates(10977345784772L, 14275880668365L,
		// 8091127762125L);
		// board.setSolutionStates(10994525653956L, 14276417539277L,
		// 8095422729421L);
		// BoardState initialState = new
		// BoardState("3,2;1,5;7,5;4,5;3,2;1,5;7,5;4,5;"+queenRow+","+queenColumn);
		BoardState initialState = new BoardState("7,5;6,2;0,12;11,11;15;" + queenRow + "," + queenColumn);

		Map<Long, Long> next2Previous = new HashMap<>();
		Map<Long, List<Long>> paths = new HashMap<>();
		while (!next2Previous.values().contains(initialState.getTinyState())) {
			board.setSolutionStates(next2Previous.values());
			Map<Long, Long> computedSolutionMoves = computeSolutionMoves(targetRobot, board, initialState);
			if (computedSolutionMoves.size() == 0) {
				solutionsString.append(queenRow + "\t" + queenColumn + "\t" + targetRobot + "\t"
						+ 0 + "\t" + 0 + "\r\n");
				solutionsString.append("\r\n");
			}
			next2Previous.putAll(computedSolutionMoves);
			if (paths.size() == 0) {
				for (long dest : next2Previous.keySet()) {
					paths.put(dest, new ArrayList<>());
					paths.get(dest).add(dest);
				}
			}
		}
		for (long finalSolution : paths.keySet()) {
			List<Long> path = paths.get(finalSolution);
			while (next2Previous.containsKey(path.get(path.size() - 1))) {
				path.add(next2Previous.get(path.get(path.size() - 1)));
			}
			Collections.reverse(path);
		}
		solutionsString.append(queenRow + "\t" + queenColumn + "\t" + targetRobot + "\t"
				+ (paths.values().iterator().next().size() - 1) + "\t" + paths.size() + "\r\n");
		solutionsString.append("\r\n");
		System.out.println("Queen at " + queenRow + "," + queenColumn + " with robot " + targetRobot);
		System.out.println("# of solution states: " + paths.size());
		System.out.println("# of moves: " + (paths.values().iterator().next().size() - 1));
		for (List<Long> path : paths.values()) {
			System.out.println("a path:");
			for (long tinyState : path) {
				System.out.println("  " + new BoardState(tinyState).getStateString());
				solutionsString.append(new BoardState(tinyState).getStateString() + "\r\n");
			}
			System.out.println();
			solutionsString.append("\r\n");
		}
		return solutionsString.toString();
	}

	private static Map<Long, Long> computeSolutionMoves(int targetRobot, Board board, BoardState initialState) {
		Map<Long, Long> solutionMoves = new HashMap<>();
		// Map<Integer, Map<Long, Long>> solutions = new HashMap<>();
		// solutions.put(0, new HashMap<>());

		// Map<Long, Boolean> isSolution = new HashMap<>();
		Set<Long> seen = new HashSet<>();
		// isSolution.put(initialState.getStateString(), false);

		// Map<Long, Long> previousState = new HashMap<>();

		Queue<Long> queue = new LinkedList<>();
		queue.add(initialState.getTinyState());
		Queue<Long> nextQueue = new LinkedList<>();
		int moves = 0;
		long start = new Date().getTime();
		// boolean foundSolution = false;
		while (!queue.isEmpty() && moves < 14) {
			BoardState state = new BoardState(queue.poll());
			if (!seen.contains(state.getTinyState())) {
				// not yet seen, find successor states and add to queue
				// boolean solvesPuzzle = board.isSolution(state);
				// if (solvesPuzzle) {
				// foundSolution = true;
				// }
				seen.add(state.getTinyState());
				for (BoardState successor : board.getSuccessors(state)) {
					boolean robotAlive = successor.isRobotAlive(targetRobot);
					boolean unseen = !seen.contains(successor.getTinyState());
					if (unseen && robotAlive) {
						if (board.isSolution(successor)) {
							System.out.println(state.getTinyState() + " => " + successor.getTinyState());
							solutionMoves.put(successor.getTinyState(), state.getTinyState());
						}
						nextQueue.add(successor.getTinyState());
						// previousState.put(successor.getTinyState(),
						// state.getTinyState());
					}
				}
			}
			// if (isSolution.get(state.getTinyState())) {
			// solutions.get(moves).put(state.getTinyState(),
			// state.getTinyState());
			// }
			if (queue.isEmpty()) {
				long now = new Date().getTime();
				System.out.println("finished " + moves + " in " + (now - start) / 1000 + " seconds; next queue has "
						+ nextQueue.size() + " positions");
				if (solutionMoves.size() > 0) {
					break;
				}
				// if (solutions.get(moves).size() > 0) {
				// break;
				// }
				queue = nextQueue;
				nextQueue = new LinkedList<>();
				moves++;
				// solutions.put(moves, new HashMap<>());
			}
		}
		// Set<String> allResults = new HashSet<>();
		// for (Long solution : solutions.get(moves).keySet()) {
		// allResults.add(new
		// BoardState(solutions.get(moves).get(solution)).getStateString());
		// System.out.println(solution);
		// // allResults.add(new
		// //
		// BoardState(solutions.get(moves).get(solution)).getStateString(previousState));
		// }
		// for (String result : allResults) {
		// System.out.println(result);
		// }
		return solutionMoves;
	}
}
