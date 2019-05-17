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

/*
 * TODO
 * 
 Create a QueryParserInterface
 
 public void parse(Path path, boolean exact) throws IOException;
 public void parse(String line, boolean exact);
 public void querytoJSON(Path path) throws IOException;
 
 implement this interface in both your QueryParser and ThreadSAfeQueryParser instead of extending
 */

/**
 * @author tracyair
 *
 */
public class ThreadSafeQueryParser extends QueryParser {

	/**
	 * Initial variable InvertedIndex Object
	 */
	private final ThreadSafeInvertedIndex index;
	/**
	 * initial the data structure
	 */
	private final TreeMap<String, ArrayList<SearchResult>> map;

	/**
	 * number of threads
	 */
	private int numThreads;

	/**
	 * This ThreadSafe version of method first makes the query file into every
	 * single query line, and uses the exactSearch and patialSearch in InvertedIndex
	 * and finally, puts the query line and result into result data structure of
	 * this class.
	 * 
	 * @param index
	 * @param numThreads
	 * @param queue
	 */
	public ThreadSafeQueryParser(ThreadSafeInvertedIndex index, int numThreads) {
		super(index);
		this.index = index;
		this.map = new TreeMap<>();
		this.numThreads = numThreads;
	}

	/**
	 * @param path
	 * @param exact
	 * @throws IOException
	 */
	public void parse(Path path, boolean exact) throws IOException {
		WorkQueue queue = new WorkQueue(numThreads);
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			while ((line = reader.readLine()) != null) {
				queue.execute(new Task(line, exact));
			}
			queue.finish();
		}
	}

	/**
	 * ThreadSafe version of parsing the query line and outputing the result
	 * 
	 * @param line  query lines
	 * @param exact a boolean checking if it is exact search or not
	 */
	public void parse(String line, boolean exact) {
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		TreeSet<String> queryLine = new TreeSet<>();
		for (String string : TextParser.parse(line)) {
			String newString = stemmer.stem(string).toString();
			queryLine.add(newString);
		}
		String joined = String.join(" ", queryLine);
		
		synchronized (this.map) {
			if (!queryLine.isEmpty() && !map.containsKey(joined)) {
				map.put(joined, index.search(queryLine, exact));
			}
		}
		
		/* TODO
		synchronized (this.map) {
			if (queryLine.isEmpty() || map.containsKey(joined)) {
				return;
			}
		}
		
		List<SearchResult> local = index.search(queryLine, exact);
		
		synchronized (this.map) {
			map.put(joined, local);
		}
		*/
		
	}

	/**
	 * a method that prints the search result data structure to the JSON file.
	 * 
	 * @param path the path of the JSON file output
	 * @throws IOException
	 */
	public void querytoJSON(Path path) throws IOException {
		synchronized (this.map) {
			PrettyJSONWriter.asNestedSearchResult(map, path);
		}
	}

	/**
	 * @author tracyair
	 *
	 */
	private class Task implements Runnable {
		/**
		 * query lines for search
		 */
		private String line;
		/**
		 * flag whether exact search or partial search
		 */
		private boolean exact;

		/**
		 * @param line
		 * @param exact
		 */
		public Task(String line, boolean exact) {
			this.line = line;
			this.exact = exact;
		}

		@Override
		public void run() {
			parse(this.line, this.exact);
		}
	}

}