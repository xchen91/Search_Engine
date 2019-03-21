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
public class WordIndex {
	
	private static TreeMap <String, TreeMap<String, TreeSet<Integer>>> dictionary;
	private static TreeMap <String, Integer> locationsmap;
	/**
	 * 
	 */
	public WordIndex() {
		dictionary = new TreeMap<>();
		locationsmap = new TreeMap<>();
	}
	
	/**
	 * @return a TreeMap of dictionary
	 */
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> getDictionary(){
		return dictionary;
	}
	
	/**
	 * @return a TreeMap of locations
	 */
	public TreeMap<String, Integer> getLocationsMap(){
		return locationsmap;
	}
	
	public String toString() {
		return dictionary.toString(); 
	}

	//nested add method
	/**
	 * @param element
	 * @param path
	 * @param position
	 * @return true if changes, false if no changes.
	 */
	public boolean add(String element, Path path, int position) {
		if(!dictionary.containsKey(element)) {
			TreeMap<String, TreeSet<Integer>> pathmap = new TreeMap<>();
			TreeSet<Integer> positionset = new TreeSet<>();
			positionset.add(position);
			pathmap.put(path.toString(), positionset);
			dictionary.put(element, pathmap);
			return true;
		}
		else {
			if(!dictionary.get(element).containsKey(path.toString())) {
				TreeSet<Integer> positionset = new TreeSet<>();
				positionset.add(position);
				dictionary.get(element).put(path.toString(), positionset);
				return true;
			}
			else{
				return dictionary.get(element).get(path.toString()).add(position);
			}
		}
		
	}
	
	//count the total words in each file
	/**
	 * @param path
	 * @throws IOException
	 */
	public void count(Path path) throws IOException{
		for(Path file : DirectoryStreamDemo.pathlist) {
			int count = 0;
			for(String word : TextFileStemmer.stemFile(file)) {
				count++;
			}
			if(count!=0) {
				System.out.println(file.toString());
				locationsmap.put(file.toString(), count);
			}
		}
	}

	/**
	 * @param element
	 * @return number of positions
	 */
	public int numPositions(String element) {
		if(!dictionary.containsKey(element) || dictionary.get(element)==null) {
			return 0;
		}
		else {
			return dictionary.get(element).size();
		}
	}


	/**
	 * @return number of elements
	 */
	public int numElements() {
		if(dictionary.isEmpty()) {
			return 0;
		}
		else {
			return dictionary.size();
		}	
	}

	/**
	 * @param element
	 * @return true if dictionary contains element
	 */
	public boolean contains(String element) {
		return dictionary.containsKey(element);
	}

	/**
	 * @return an imutable collection of elements.
	 */
//	@SuppressWarnings("unchecked")

	public Collection<String> getElements() {
		try {
			ArrayList<String> elements = new ArrayList<>();
			for(String element : dictionary.keySet()) {
				elements.add(element);
			}
			Collection<String> immutablelist = Collections.unmodifiableCollection(elements);		
			return immutablelist;
		}catch(UnsupportedOperationException e) {
			return (Collection<String>) e;
		}
		
	}

}
