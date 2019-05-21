import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details. Project3a test passed!
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */

public class Driver {

	/**
	 * Parses the command-line arguments to build a inverted index data structure
	 * 
	 * @param args the command-line arguments to build
	 */
	public static void main(String[] args) {
		Instant start = Instant.now();
		ArgumentMap map = new ArgumentMap(args);
		InvertedIndex index;
		QueryParserInterface query;
		InvertedIndexBuilder builder;
		int threads = 0;

		if (map.hasFlag("-threads")) {
			String numThreads = map.getString("-threads", "5");
			try {
				threads = Integer.parseInt(numThreads);
			} catch (NumberFormatException e) {
				threads = 5;
			}
			if (threads < 1) {
				threads = 5;
			}
			index = new ThreadSafeInvertedIndex();
			query = new ThreadSafeQueryParser((ThreadSafeInvertedIndex) index, threads);
			builder = new ThreadSafeInvertedIndexBuilder((ThreadSafeInvertedIndex) index, threads);
		} else {
			index = new InvertedIndex();
			query = new QueryParser(index);
			builder = new InvertedIndexBuilder(index);
		}

		if (map.hasFlag("-path")) {
			if (map.getPath("-path") != null) {
				Path filePath = map.getPath("-path");
				try {
					builder.build(filePath);
				} catch (IOException e) {
					System.out.println("Unable to build index from path: " + filePath);
				}
			} else {
				System.out.println("The given path is without a value");
			}
		}

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

		if (map.hasFlag("-query") && map.getPath("-query") != null) {
			Path queryPath = map.getPath("-query");
			try {
				query.parse(queryPath, map.hasFlag("-exact"));
			} catch (IOException e) {
				System.out.println("Unable to build the search from path: " + queryPath);
			}
		}

		if (map.hasFlag("-results")) {
			Path resultPath = map.getPath("-results", Paths.get("results.json"));
			try {
				query.querytoJSON(resultPath);

			} catch (IOException e) {
				System.out.println("Unable to print the search result from path: " + resultPath);
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
