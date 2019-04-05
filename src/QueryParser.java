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
 * @author tracyair
 * @param <SearchResult>
 *
 */
public class QueryParser<SearchResult> {
	private final InvertedIndex index;
	private final TreeMap<String, ArrayList<SearchResult>> map;

	/**
	 * @param index
	 */
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.map = new TreeMap<>();
	}

	/**
	 * @param path
	 * @param exact
	 * @throws IOException
	 */
	public void parse(Path path, boolean exact) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			while ((line = reader.readLine()) != null) {
				TreeSet<String> querySet = new TreeSet<>();
				for (String string : TextParser.parse(line)) {
					String newString = stemmer.stem(string).toString();
					querySet.add(newString);
				}

			}
		}
	}

	public void querytoJSON(Path path) throws IOException {

	}

}