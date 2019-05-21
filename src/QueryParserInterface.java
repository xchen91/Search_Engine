import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author tracyair
 *
 */
public interface QueryParserInterface {
	/**
	 * This method first makes the query file into every single query line, and uses
	 * the exactSearch and patialSearch in InvertedIndex and finally, puts the query
	 * line and result into result data structure of this class.
	 * 
	 * @param path  the path of file that needs to be searched
	 * @param exact a boolean checking if it is exact search or partial search
	 * @throws IOException
	 */
	public default void parse(Path path, boolean exact) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parse(line, exact);
			}
		}
	}

	/**
	 * Parse the line and output the result
	 * 
	 * @param line  query lines
	 * @param exact a boolean checking if it is exact search or not
	 */
	public void parse(String line, boolean exact);

	/**
	 * a method that prints the search result data structure in JSON format.
	 * 
	 * @param path the path of the JSON file output
	 * @throws IOException
	 */
	public void querytoJSON(Path path) throws IOException;
}