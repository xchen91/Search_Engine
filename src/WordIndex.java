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
	public WordIndex() {
		dictionary = new TreeMap<>();
		locationsmap = new TreeMap<>();
	}
	
	public TreeMap<String, TreeMap<String, TreeSet<Integer>>> getDictionary(){
		return dictionary;
	}
	
	public TreeMap<String, Integer> getLocationsMap(){
		return locationsmap;
	}
	
	public String toString() {
		return dictionary.toString(); 
	}

	//nested add method
	public boolean add(String element, Path path, int position) {
		// TODO Auto-generated method stub
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

	public int numPositions(String element) {
		// TODO Auto-generated method stub
		if(!dictionary.containsKey(element) || dictionary.get(element)==null) {
			return 0;
		}
		else {
			return dictionary.get(element).size();
		}
	}


	public int numElements() {
		// TODO Auto-generated method stub
		if(dictionary.isEmpty()) {
			return 0;
		}
		else {
			return dictionary.size();
		}	
	}

	public boolean contains(String element) {
		// TODO Auto-generated method stub
		return dictionary.containsKey(element);
	}

	@SuppressWarnings("unchecked")

	public Collection<String> getElements() {
		// TODO Auto-generated method stub
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




	/*
	 * TODO Modify anything within this class as necessary. This includes the class
	 * declaration; you need to implement the Index interface!
	 */

}
