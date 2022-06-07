package fullboard;

import static java.lang.System.*;
import static org.apache.commons.io.FileUtils.*;

import java.io.*;
import java.util.*;

public class Solver {

	private String fileName;

	private Stack<GameState> initialStates = new Stack<>();

	private Stack<GameState> workingStates = new Stack<>();

	private Stack<GameState> victoryStates = new Stack<>();


	public Solver(String fileName) {
		this.fileName = fileName;
	}


	public void begin() {
		List<String> puzzleLines;
		try {
			File puzzleFile = new File(fileName);
			puzzleLines = readLines(puzzleFile);
		} catch (IOException e) {
			out.println("File not found.");
			out.println("Complete");
			return;
		}
		fillInitialStates(puzzleLines);

		Stack<GameState> temp = new Stack<>();
		while (!initialStates.isEmpty()) {
			temp.push(initialStates.pop());
		}
		initialStates = temp;

		while (!initialStates.isEmpty()) {
			solveNew(initialStates.pop());
		}
		out.println("Complete");
	}


	private void printNoSolution(GameState gs) {
		out.println("map");
		out.println("No solution");
		out.println(gs.noSolutionToString());
		out.println("endmap");
	}


	private void printSolution(Stack<GameState> optimalStates) {
		out.println("map");
		if (optimalStates.size() > 1) {
			Collections.sort(optimalStates);
			Stack<GameState> temp = new Stack<>();
			while (!optimalStates.isEmpty()) {
				temp.push(optimalStates.pop());
			}
			optimalStates = temp;
		}
		out.println(optimalStates.peek().getRounds() + " moves");
		while (!optimalStates.isEmpty()) {
			out.println("solution");
			out.print(optimalStates.pop().solutionToString());
			out.println("endsolution");
		}
		out.println("endmap");
	}


	private void solveNew(GameState gs) {
		victoryStates = new Stack<>();
		workingStates = new Stack<>();
		for (int i = 0; i < gs.getInitialEmpty(); i++) {
			GameState newState = new GameState(gs);
			newState.start(i);
			workingStates.push(newState);
		}

		while (!workingStates.isEmpty()) {
			solve(workingStates.pop());
		}
		if (victoryStates.isEmpty()) {
			printNoSolution(gs);
		} else {
			Stack<GameState> optimalStates = new Stack<GameState>();
			optimalStates.push(victoryStates.pop());
			while (!victoryStates.isEmpty()) {
				if (victoryStates.peek().getRounds() < optimalStates.peek().getRounds()) {
					while (!optimalStates.isEmpty()) {
						optimalStates.pop();
					}
					optimalStates.push(victoryStates.pop());
				} else if (victoryStates.peek().getRounds() == optimalStates.peek().getRounds()) {
					optimalStates.push(victoryStates.pop());
				} else {
					victoryStates.pop();
				}
			}
			printSolution(optimalStates);
		}
	}


	private void solve(GameState gs) {
		Stack<Direction> possibleMoves = new Stack<>();
		Position position = gs.getPosition();
		if (!gs.isBlocked(position.getX() + 1, position.getY())) {
			possibleMoves.push(Direction.RIGHT);
		}
		if (!gs.isBlocked(position.getX() - 1, position.getY())) {
			possibleMoves.push(Direction.LEFT);
		}
		if (!gs.isBlocked(position.getX(), position.getY() + 1)) {
			possibleMoves.push(Direction.DOWN);
		}
		if (!gs.isBlocked(position.getX(), position.getY() - 1)) {
			possibleMoves.push(Direction.UP);
		}
		if (possibleMoves.isEmpty() && gs.isFull()) {
			victoryStates.add(gs);
		}
		while (!possibleMoves.isEmpty()) {
			GameState newState = new GameState(gs);
			newState.move(possibleMoves.pop());
			workingStates.push(newState);
		}
	}


	private void fillInitialStates(List<String> puzzleLines) {

		GameState newState = null;
		int i = 0;
		boolean[][] configuration = null;
		Stack<Position> starts = null;

		for (String line : puzzleLines) {
			if (line.equals("map")) {
				i = 0;
				newState = new GameState();
				configuration = null;
				starts = new Stack<Position>();
			} else if (line.equals("endmap")) {
				newState.setConfiguration(configuration);
				newState.setStarts(starts);
				initialStates.push(newState);
			} else {
				if (configuration == null) {
					configuration = new boolean[line.length()][line.length()];
				}
				for (int j = 0; j < line.length(); j++) {
					if (line.charAt(j) == '▓') {
						configuration[i][j] = true;
					} else {
						configuration[i][j] = false;
						starts.push(new Position(j, i));
					}
				}
				i++;
			}
		}
	}


}
