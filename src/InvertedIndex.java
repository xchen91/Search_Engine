import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
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

	/**
	 * Returns a string representation of this index.
	 */
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
	 * a method to check if index contains a word in that specific location and
	 * position.
	 * 
	 * @param element  to check
	 * @param location of the given element
	 * @param position of the given element
	 * @return true if the word is stored in that index, in that specific location
	 *         and that specific position.
	 */
	public boolean contains(String element, String location, int position) {
		return this.contains(element, location) && this.index.get(element).get(location).contains(position);
	}

	/**
	 * Get an immutable collection of elements in the index
	 * 
	 * @return an immutable collection of elements.
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(index.keySet());
	}

	/**
	 * Get an immutable collection of locations of elements in the index
	 * 
	 * @param word
	 * @return an immutable collection of locations.
	 */
	public Set<String> getLocations(String word) {
		if (index.containsKey(word)) {
			return Collections.unmodifiableSet(index.get(word).keySet());
		}
		return Collections.emptySet();
	}
}
