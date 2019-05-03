import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * a class that parses the file and stores the search results
 * 
 * @author tracyair
 *
 */

public class QueryParser {
	/**
	 * Stores query line into inverted index data structure
	 */
	private final InvertedIndex index;
	/**
	 * Stores a mapping of query line and the lists of search result
	 */
	private final TreeMap<String, ArrayList<SearchResult>> map;

	/**
	 * Class constructor
	 * 
	 * @param index
	 */
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.map = new TreeMap<>();
	}

	/**
	 * This method first makes the query file into every single query line, and uses
	 * the exactSearch and patialSearch in InvertedIndex and finally, puts the query
	 * line and result into result data structure of this class.
	 * 
	 * @param path  the path of file that needs to be searched
	 * @param exact a boolean checking if it is exact search or not
	 * @throws IOException
	 */
	public void parse(Path path, boolean exact) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			while ((line = reader.readLine()) != null) {
				// TODO parse(line, exact);
				TreeSet<String> queryLine = new TreeSet<>();
				for (String string : TextParser.parse(line)) {
					String newString = stemmer.stem(string).toString();
					queryLine.add(newString);
				}
				String joined = String.join(" ", queryLine);
				if (!queryLine.isEmpty() && !map.containsKey(joined)) {
					if (exact == true) {
						map.put(joined, index.exactSearch(queryLine));
					} else {
						map.put(joined, index.partialSearch(queryLine));
					}
				}
			}
		}
	}
	
	/* TODO
	public void parse(String line, boolean exact) {
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		TreeSet<String> queryLine = new TreeSet<>();
		for (String string : TextParser.parse(line)) {
			String newString = stemmer.stem(string).toString();
			queryLine.add(newString);
		}
		String joined = String.join(" ", queryLine);
		
		if (!queryLine.isEmpty() && !map.containsKey(joined)) {
			map.put(joined, index.search(queryLine, exact));
		}
	}
	*/

	/**
	 * a method that prints the search result data structure to the JSON file.
	 * 
	 * @param path the path of the JSON file output
	 * @throws IOException
	 */
	public void querytoJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedSearchResult(map, path);
	}

}