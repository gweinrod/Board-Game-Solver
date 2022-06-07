package fullboard;

import static java.lang.System.*;

import java.util.*;

public class Main {

	public static void main(String[] args) {

		receiveFileName();
		// testMapFile("smallmaps.txt");

	}


	private static void receiveFileName() {
		Scanner s = new Scanner(in);
		while (s.hasNext()) {
			Solver solver = new Solver(s.next());
			solver.begin();
		}
		s.close();
	}


	// private static void testMapFile(String fileName) {
	// Solver solver = new Solver(fileName);
	// solver.begin();
	// }

}
