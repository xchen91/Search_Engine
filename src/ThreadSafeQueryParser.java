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
 *
 */
public class ThreadSafeQueryParser implements QueryParserInterface {

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
	 * Class Constructors
	 * 
	 * @param index
	 * @param numThreads
	 */
	public ThreadSafeQueryParser(ThreadSafeInvertedIndex index, int numThreads) {
		this.index = index;
		this.map = new TreeMap<>();
		this.numThreads = numThreads;
	}

	@Override
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

	@Override
	public void parse(String line, boolean exact) {
		Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
		TreeSet<String> queryLine = new TreeSet<>();
		for (String string : TextParser.parse(line)) {
			String newString = stemmer.stem(string).toString();
			queryLine.add(newString);
		}
		String joined = String.join(" ", queryLine);

//		synchronized (this.map) {
//			if (!queryLine.isEmpty() && !map.containsKey(joined)) {
//				map.put(joined, index.search(queryLine, exact));
//			}
//		}

		synchronized (this.map) {
			if (queryLine.isEmpty() || map.containsKey(joined)) {
				return;
			}
		}
		ArrayList<SearchResult> local = index.search(queryLine, exact);
		synchronized (this.map) {
			map.put(joined, local);
		}
	}

	@Override
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