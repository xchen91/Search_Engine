import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A special type of {@link Index} that indexes the locations words were found.
 */

public class InvertedIndex {

	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> index;
	private final TreeMap<String, Integer> wordCount;

	/**
	 * 
	 */
	public InvertedIndex() {
		index = new TreeMap<>();
		wordCount = new TreeMap<>();
	}

	/**
	 * @param path
	 * @throws IOException
	 */

	public void toJSON(Path path) throws IOException {
		PrettyJSONWriter.asNestedTreeMapMap(this.index, path);
	}

	/**
	 * @param path
	 * @throws IOException
	 */
	public void numtoJSON(Path path) throws IOException {
		PrettyJSONWriter.asObject(this.wordCount, path);
	}

	@Override
	public String toString() {
		return index.toString();
	}

	// nested add method
	/**
	 * @param element
	 * @param location
	 * @param position
	 * @return true if changes, false if no changes.
	 */
	public boolean add(String element, String location, int position) {
		Integer count = wordCount.getOrDefault(location, 0);
		wordCount.put(location, count + 1);
		index.putIfAbsent(element, new TreeMap<>());
		index.get(element).putIfAbsent(location, new TreeSet<>());
		return index.get(element).get(location).add(position);
	}

	/**
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
	 * @param element
	 * @return true if dictionary contains element
	 */
	public boolean contains(String element) {
		return index.containsKey(element);
	}

	/**
	 * @param word
	 * @param location
	 * @return boolean
	 */
	public boolean contains(String word, String location) {
		return this.index.containsKey(word) && this.index.get(word).containsKey(location);
	}

//	/**
//	 * @return an immutable collection of elements.
//	 */
//
//	public Collection<String> getWords() {
//		try {
//			ArrayList<String> elements = new ArrayList<>();
//			for (String element : index.keySet()) {
//				elements.add(element);
//			}
//			Collection<String> immutablelist = Collections.unmodifiableCollection(elements);
//			return immutablelist;
//		} catch (UnsupportedOperationException e) {
//			return Collections.unmodifiableCollection(index.keySet());
//		}
//
//	}

	/**
	 * @return number of words
	 */
	public int count() {
		return index.size();
	}

	/**
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
	 * @param queryLine
	 * @return result
	 */
	public ArrayList<SearchResult> exactSearch(TreeSet<String> queryLine) {
		ArrayList<SearchResult> result = new ArrayList<>();
		HashMap<String, SearchResult> map = new HashMap<>();
		for (String word : queryLine) {
			if (contains(word)) {
				exactSearch(word, result, map);
			}
		}
		Collections.sort(result);
		return result;
	}

	private void exactSearch(String word, ArrayList<SearchResult> result, HashMap<String, SearchResult> map) {
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
	 * @param queryLine
	 * @return result
	 */
	public ArrayList<SearchResult> partialSearch(TreeSet<String> queryLine) {
		ArrayList<SearchResult> result = new ArrayList<>();
		HashMap<String, SearchResult> map = new HashMap<>();
		for (String word : queryLine) {
			partialSearch(word, result, map);
		}
		Collections.sort(result);
		return result;
	}

	private void partialSearch(String word, ArrayList<SearchResult> result, HashMap<String, SearchResult> map) {
		for (String string : index.keySet()) {
			if (string.startsWith(word)) {
				exactSearch(string, result, map);
			}
		}
	}

}
