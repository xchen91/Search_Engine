import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	public void numtoJSON(Path path) throws IOException{
		 PrettyJSONWriter.asObject(wordCount, path);
	 }



	@Override
	public String toString() {
		return index.toString();
	}

	
	//nested add method
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
		if(!index.containsKey(element) || index.get(element)==null) {
			return 0;
		}
		else {
			return index.get(element).size();
		}
	}


	/**
	 * @return number of elements
	 */
	public int numElements() {
		if(index.isEmpty()) {
			return 0;
		}
		else {
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
	 * @return an immutable collection of elements.
	 */

	public Collection<String> getWords() {
		try {
			ArrayList<String> elements = new ArrayList<>();
			for(String element : index.keySet()) {
				elements.add(element);
			}
			Collection<String> immutablelist = Collections.unmodifiableCollection(elements);
			return immutablelist;
		}catch(UnsupportedOperationException e) {
			return Collections.unmodifiableCollection(index.keySet());
		}


	}

}
