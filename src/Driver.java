import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;


/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class Driver {


	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 * @throws IOException
	 */


	public static void main(String[] args) {
		Instant start = Instant.now();
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();

		if (map.hasFlag("-path") && map.getPath("-path") != null) {
			Path filePath = map.getPath("-path");
			try {
				InvertedIndexBuilder.build(filePath, index);
			}
			catch(IOException e) {
				System.out.println("Unable to build index from path: " + filePath);
			}
		}

		if (map.hasFlag("-index")) {
			Path indexPath = map.getPath("-index", Paths.get("index.json"));
			try {
				index.toJSON(indexPath);
			}
			catch(IOException e) {
				System.out.println("Unable to print index from path: " + indexPath);
			}
		}

		if (map.hasFlag("-locations")) {
			Path locationPath = map.getPath("-locations", Paths.get("locations.json"));
			try {
				index.numtoJSON(locationPath);
			}
			catch(IOException e) {
				System.out.println("Unable to print locations from path: " + locationPath);
			}
		}
		
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}

	/*
	 * Generally, "driver" classes are responsible for setting up and calling other
	 * classes, usually from a main() method that parses command-line parameters. If
	 * the driver were only responsible for a single class, we use that class name.
	 * For example, "PizzaDriver" is what we would name a driver class that just
	 * sets up and calls the Pizza class.
	 */
}
