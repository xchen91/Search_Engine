import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A special type of data structure that indexes the locations words were found.
 */

public class InvertedIndex {

	/**
	 * Stores a mapping of words to the positions the words were found.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	/**
	 * Stores a mapping of locations and their total words number
	 */
	private final TreeMap<String, Integer> wordCount;

	/**
	 * Initializes the index.
	 */
	public InvertedIndex() {
		index = new TreeMap<>();
		wordCount = new TreeMap<>();
	}

	/**
	 * a method to print the JSON format
	 * 
	 * @param path the output file path to print JSON file
	 * @throws IOException
	 */

	public void toJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedTreeMapMap(this.index, path);
	}

	/**
	 * a method to print the JSON format
	 * 
	 * @param path the output file path to print JSON file
	 * @throws IOException
	 */
	public void numtoJSON(Path path) throws IOException {
		PrettyJSONWriter.asObject(this.wordCount, path);
	}

	@Override
	public String toString() {
		return index.toString();
	}

	/**
	 * Adds the word and the location, position it was found to the index.
	 * 
	 * @param element  word to clean and add to index
	 * @param location location word was found
	 * @param position the position the word at
	 */
	public void add(String element, String location, int position) {
		index.putIfAbsent(element, new TreeMap<>());
		index.get(element).putIfAbsent(location, new TreeSet<>());

		if (index.get(element).get(location).add(position)) {
			Integer count = wordCount.getOrDefault(location, 0);
			wordCount.put(location, count + 1);
		}
	}

	/**
	 * Check the number of positions of given element in the index
	 * 
	 * @param element
	 * @return number of positions
	 */
	public int numPositions(String element) {
		if (!index.containsKey(element) || index.get(element) == null) {
			return 0;
		} else {
			return index.get(element).size();
		}
	}

	/**
	 * Check the number of elements in the index
	 * 
	 * @return number of elements
	 */
	public int numElements() {
		if (index.isEmpty()) {
			return 0;
		} else {
			return index.size();
		}
	}

	/**
	 * Tests whether the index contains the specified word.
	 * 
	 * @param element
	 * @return true if index contains element
	 */
	public boolean contains(String element) {
		return index.containsKey(element);
	}

	/**
	 * Tests whether the index contains the specified word at the specified file.
	 * 
	 * @param element
	 * @param location
	 * @return true if the element is in the index at the given location
	 */
	public boolean contains(String element, String location) {
		return this.index.containsKey(element) && this.index.get(element).containsKey(location);
	}

	/**
	 * the number of locations stored
	 * 
	 * @return number of words
	 */
	public int count() {
		return index.size();
	}

	/**
	 * the number of locations stored given specific word
	 * 
	 * @param word
	 * @return number of locations
	 */
	public int count(String word) {
		if (index.containsKey(word)) {
			return index.get(word).size();
		} else {
			return 0;
		}
	}

	/**
	 * the number of locations stored given specific word and location
	 * 
	 * @param word
	 * @param location
	 * @return number of positions
	 */
	public int count(String word, String location) {
		if (this.contains(word, location)) {
			return this.index.get(word).get(location).size();
		} else {
			return 0;
		}
	}

	/**
	 * Exact search for query words
	 * 
	 * @param queries the query line for searching
	 * @return the search result
	 */
	public ArrayList<SearchResult> exactSearch(Collection<String> queries) {
		ArrayList<SearchResult> result = new ArrayList<>();
		HashMap<String, SearchResult> map = new HashMap<>();
		for (String word : queries) {
			if (index.containsKey(word)) {
				searchProcess(word, result, map);
			}
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * A search process method for exact search and partial search
	 * 
	 * @param word   a query word for search
	 * @param result the result list for adding query word
	 * @param map    keep track and store the search process
	 */
	private void searchProcess(String word, ArrayList<SearchResult> result, HashMap<String, SearchResult> map) {
		for (String location : index.get(word).keySet()) {
			if (map.containsKey(location)) {
				map.get(location).updateOccurence(this.count(word, location));
			} else {
				SearchResult newResult = new SearchResult(location, this.wordCount.get(location),
						this.count(word, location));
				result.add(newResult);
				map.put(location, newResult);
			}
		}
	}

	/**
	 * Partial search for query words
	 * 
	 * @param queries the query line for searching
	 * @return result the search result
	 */
	public ArrayList<SearchResult> partialSearch(Collection<String> queries) {
		ArrayList<SearchResult> result = new ArrayList<>();
		HashMap<String, SearchResult> map = new HashMap<>();
		for (String word : queries) {
			for (String string : index.tailMap(word).keySet()) {
				if (string.startsWith(word)) {
					searchProcess(string, result, map);
				}
			}
		}
		Collections.sort(result);
		return result;
	}

	/**
	 * A general search method given queries for searching and a flag deciding exact
	 * search or partial search
	 * 
	 * @param queries the query line for searching
	 * @param exact   a boolean checking if it is exact search or partial search
	 * @return search the search result
	 */
	public ArrayList<SearchResult> search(Collection<String> queries, boolean exact) {
		return exact ? exactSearch(queries) : partialSearch(queries);
	}

	/**
	 * A method that add all other InvertedIndex data structures
	 * 
	 * @param other InvertedIndexs to add
	 */
	public void addAll(InvertedIndex other) {
		for (String key : other.index.keySet()) {
			if (this.index.containsKey(key) == false) {
				this.index.put(key, other.index.get(key));
			} else {
				for (String path : other.index.get(key).keySet()) {
					try {
						if (this.index.get(key).containsKey(path) && !key.isEmpty()) {
							this.index.get(key).get(path).addAll(other.index.get(key).get(path));
						} else {
							this.index.get(key).put(path, other.index.get(key).get(path));
						}
					} catch (NullPointerException e) {
						System.out.println("Unable to add all other InvertedIndex data structures");
					}
				}
			}
		}
		for (String location : other.wordCount.keySet()) {
			if (this.wordCount.containsKey(location)) {
				this.wordCount.put(location, this.wordCount.get(location) + other.wordCount.get(location));
			} else {
				this.wordCount.put(location, other.wordCount.get(location));
			}
		}
	}
}
