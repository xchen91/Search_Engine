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

public class QueryParser implements QueryParserInterface {
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
	 * @param index a mapping of query line and the lists of search result
	 */
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.map = new TreeMap<>();
	}

	@Override
	public void parse(Path path, boolean exact) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				parse(line, exact);
			}
		}
	}

	@Override
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

	@Override
	public void querytoJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedSearchResult(map, path);
	}

}