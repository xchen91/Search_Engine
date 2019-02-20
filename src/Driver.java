import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
//import java.util.Arrays;
import java.util.TreeSet;

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
//		System.out.println(Arrays.toString(args));
		
		Path path, index;
		ArgumentMap argmap = new ArgumentMap(args);

		WordIndex invertedindex = new WordIndex();

		if(argmap.hasFlag("-path")) {
			path = argmap.getPath("-path");
			if (path != null) {
				//check if it has "-index" or not.
				if(argmap.hasFlag("-index")) {
					
					//traverse to all text file.
					DirectoryStreamDemo.publictxttraverse(path);
					//get output path
					index = argmap.getPath("-index");

					// if no output path, output to the default path "index.json".
					if(index == null) {
						index = argmap.getPath("-index", Paths.get("index.json"));
					}
				}
				//if no "-index", traverse all html file.
				else {
					DirectoryStreamDemo.publichtmltraverse(path);
					index = null;
				}
				
				 //traverse all path in the filelist using DirectoryStreamDemo
				for (Path file : DirectoryStreamDemo.pathlist) {
					// read the file and parse the file line by line
					TreeSet<String> stemset = TextFileStemmer.stemFile(file);
					//add each stem in the wordindex
					int position = 1;
					for (String stem : stemset) {
						invertedindex.add(stem, path, position);
						position++;
					}
				}
				if(index!=null) {
					PrettyJSONWriter.asNestedTreeMapMap(invertedindex.getWordIndex(), index);
				}
			}

			
			
		}
		else {
			path = null;
			index = Paths.get("index.json");
//			locations = Paths.get("locations.json");
			PrettyJSONWriter.asNestedTreeMapMap(invertedindex.getWordIndex(), index);	
			
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
