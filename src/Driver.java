import java.io.IOException;
//import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

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

	public static void main(String[] args) throws IOException {

		// store initial start time
		Instant start = Instant.now();

		// TODO Modify this method as necessary.
		Path path, index;
		ArgumentMap argmap = new ArgumentMap(args);
		InvertedIndex invertedindex = new InvertedIndex();
		if (argmap.hasFlag("-path")) {
			path = argmap.getPath("-path");
			// check valid input path
			if (path != null) {
				// check if it has "-index".
				if (argmap.hasFlag("-index")) {

					// traverse to all text file.
					DirectoryStreamDemo.publictxttraverse(path);
					// get output path, if no output path, output to the default path "index.json".
					index = argmap.getPath("-index", Paths.get("index.json"));
				}
				// if no "-index", traverse all html file.
				else {
					DirectoryStreamDemo.publictxttraverse(path);
					index = null;
				}
				// traverse all path in the pathlist using DirectoryStreamDemo
				for (Path file : DirectoryStreamDemo.pathlist) {
					// read the file and parse the file word by word
					ArrayList<String> stemlist = TextFileStemmer.stemFile(file);

					// add each word to the invertedindex
					int position = 1;

					for (String stem : stemlist) {

						invertedindex.add(stem, file, position);
						position++;
					}

				}
				// if it is txt file, convert it to json format and print the
				// locations/wordcount.
				if (index != null) {

					PrettyJSONWriter.asNestedTreeMapMap(invertedindex.getDictionary(), index);
				}

				// check if has "-locations", and output the wordcount of each txt file.
				if (argmap.hasFlag("-locations")) {
					// get the output path, or output to default path "loca5tions.json".
					Path locations = argmap.getPath("-locations", Paths.get("locations.json"));
					invertedindex.count(path);
					PrettyJSONWriter.asObject(invertedindex.getLocationsMap(), locations);
				}
			}
		} else {
			path = null;
			index = Paths.get("index.json");
			PrettyJSONWriter.asNestedTreeMapMap(invertedindex.getDictionary(), index);
		}
		// calculate time elapsed and output
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
