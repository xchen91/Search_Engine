import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

/*
TODO Have to fix warnings:
Javadoc: Parameter prefix is not declared	DirectoryStreamDemo.java	/Project/src	line 26	Java Problem
Javadoc: Missing tag for parameter paths	DirectoryStreamDemo.java	/Project/src	line 33	Java Problem
Javadoc: Missing comment for private declaration	InvertedIndex.java	/Project/src	line 15	Java Problem
Javadoc: Missing comment for private declaration	InvertedIndex.java	/Project/src	line 16	Java Problem
Javadoc: Index cannot be resolved to a type	InvertedIndex.java	/Project/src	line 10	Java Problem
 */

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
	 * TODO description of the method here
	 * @param args description of the parameter here
	 */
	public static void main(String[] args) {
		Instant start = Instant.now();
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index = new InvertedIndex();

		if (map.hasFlag("-path") && map.getPath("-path") != null) {
			Path filePath = map.getPath("-path");
			try {
				InvertedIndexBuilder.build(filePath, index);
			} catch (IOException e) {
				System.out.println("Unable to build index from path: " + filePath);
			}
		}
		/*
		 * TODO Want to warn the user if they gave a -path flag without a value
		 */

		if (map.hasFlag("-index")) {
			Path indexPath = map.getPath("-index", Paths.get("index.json"));
			try {
				index.toJSON(indexPath);
			} catch (IOException e) {
				System.out.println("Unable to print index from path: " + indexPath);
			}
		}

		if (map.hasFlag("-locations")) {
			Path locationPath = map.getPath("-locations", Paths.get("locations.json"));
			try {
				index.numtoJSON(locationPath);
			} catch (IOException e) {
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
