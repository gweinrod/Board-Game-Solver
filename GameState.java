package fullboard;

import static java.lang.System.*;

import java.util.*;

public class GameState implements Comparable<GameState> {

	private boolean[][] originalConfiguration;

	private boolean[][] configuration;

	private Stack<Direction> moves;

	private Stack<Position> starts;

	private Position position;

	private Position start;

	private int empty;

	private int initialEmpty;

	private int rounds;


	public GameState(GameState gs) {
		// clone each line of the matrix
		boolean[][] newConfiguration = new boolean[gs.configuration.length][gs.configuration.length];
		for (int i = 0; i < gs.configuration.length; i++) {
			newConfiguration[i] = gs.configuration[i].clone();
		}
		this.configuration = newConfiguration;

		// clone the stack of moves
		Stack<Direction> newMoves = new Stack<Direction>();
		newMoves.addAll(gs.moves);
		this.moves = newMoves;

		if (gs.position != null) {
			this.position = new Position(gs.position); // clone one level deep
		}

		this.originalConfiguration = gs.originalConfiguration; // original configuration never changes
		this.starts = gs.starts; // list of starts never changes
		this.start = gs.start; // nor does the start

		this.empty = gs.empty; // integer primitive does not need to be copied
		this.rounds = gs.rounds; // integer
		this.initialEmpty = gs.initialEmpty; // integer
	}


	public GameState() {
		rounds = 0;
		moves = new Stack<Direction>();

	}


	public void setConfiguration(boolean[][] configuration) {
		this.configuration = configuration;
		this.originalConfiguration = configuration;
	}


	public void setStarts(Stack<Position> starts) {
		this.starts = starts;
		this.empty = starts.size();
		this.initialEmpty = empty;
	}


	public void start(int emptyIndex) {
		Position start = starts.pop();
		this.configuration[start.getY()][start.getX()] = true;
		position = start;
		this.start = start;
		empty--;
	}


	public void move(Direction d) {
		rounds++;
		switch (d) {
		case RIGHT:
			while (!configuration[position.getY()][position.getX() + 1]) {
				position.setX(position.getX() + 1);
				configuration[position.getY()][position.getX()] = true;
				moves.add(d);
				empty--;
			}
			break;
		case LEFT:
			while (!configuration[position.getY()][position.getX() - 1]) {
				position.setX(position.getX() - 1);
				configuration[position.getY()][position.getX()] = true;
				moves.add(d);
				empty--;
			}
			break;
		case DOWN:
			while (!configuration[position.getY() + 1][position.getX()]) {
				position.setY(position.getY() + 1);
				configuration[position.getY()][position.getX()] = true;
				moves.add(d);
				empty--;
			}
			break;
		case UP:
			while (!configuration[position.getY() - 1][position.getX()]) {
				position.setY(position.getY() - 1);
				configuration[position.getY()][position.getX()] = true;
				moves.add(d);
				empty--;
			}
			break;
		}
	}


	public Position getPosition() {
		return position;
	}


	public boolean isBlocked(int x, int y) {
		if (x < configuration.length && y < configuration.length) {
			return configuration[y][x];
		} else {
			return true;
		}
	}


	public boolean isFull() {
		if (empty > 0) {
			return false;
		} else {
			return true;
		}
	}


	public String solutionToString() {

		Stack<Direction> moves = new Stack<>();
		moves.addAll(this.moves);

		char[][] charConfiguration = new char[configuration.length][configuration.length];
		for (int i = 0; i < configuration.length; i++) {
			for (int j = 0; j < configuration.length; j++) {
				if (originalConfiguration[i][j]) {
					charConfiguration[i][j] = '▓';
				}
			}
		}

		Stack<Direction> temp = new Stack<Direction>();
		while (!moves.isEmpty()) {
			temp.push(moves.pop());
		}
		moves = temp;

		charConfiguration[start.getY()][start.getX()] = 'S';

		Position cursor = new Position(start);
		while (!moves.isEmpty()) {
			switch (moves.peek()) {
			case RIGHT:
				cursor.setX(cursor.getX() + 1);
				charConfiguration[cursor.getY()][cursor.getX()] = '→';
				break;
			case LEFT:
				cursor.setX(cursor.getX() - 1);
				charConfiguration[cursor.getY()][cursor.getX()] = '←';
				break;
			case DOWN:
				cursor.setY(cursor.getY() + 1);
				charConfiguration[cursor.getY()][cursor.getX()] = '↓';
				break;
			case UP:
				cursor.setY(cursor.getY() - 1);
				charConfiguration[cursor.getY()][cursor.getX()] = '↑';
				break;
			}
			moves.pop();
		}

		charConfiguration[cursor.getY()][cursor.getX()] = 'F';

		StringBuilder boardConfiguration = new StringBuilder();
		for (int i = 0; i < charConfiguration.length; i++) {
			for (int j = 0; j < charConfiguration.length; j++) {
				boardConfiguration.append(charConfiguration[i][j]);
			}
			if (i < configuration.length) {
				boardConfiguration.append(getProperty("line.separator"));
			}
		}
		return boardConfiguration.toString();
	}


	public String noSolutionToString() {
		StringBuilder boardConfiguration = new StringBuilder();
		int i = 0;
		while (i < configuration.length) {
			int j = 0;
			while (j < configuration.length) {
				if (originalConfiguration[i][j] == true) {
					boardConfiguration.append('▓');
				} else {
					boardConfiguration.append(' ');
				}
				j++;
			}
			i++;
			if (i < configuration.length) {
				boardConfiguration.append(getProperty("line.separator"));
			}
		}
		return boardConfiguration.toString();
	}


	public int getRounds() {
		return rounds;
	}


	public int getSize() {
		return configuration.length;
	}


	public int getInitialEmpty() {
		return initialEmpty;
	}


	public boolean[][] getConfiguration() {
		return configuration;
	}


	public boolean[][] getOriginalConfiguration() {
		return originalConfiguration;
	}


	@Override
	public int compareTo(GameState gs) {
		return solutionToString().compareTo(gs.solutionToString());
	}

}
