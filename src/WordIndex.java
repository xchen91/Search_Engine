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

		Integer count = locationsmap.getOrDefault(path.toString(), 0);
		locationsmap.put(path.toString(), count+1);
		if(!dictionary.containsKey(element)) {
			TreeMap<String, TreeSet<Integer>> pathmap = new TreeMap<>();
			TreeSet<Integer> positionset = new TreeSet<>();
			positionset.add(position);
			pathmap.put(path.toString(), positionset);
			dictionary.put(element, pathmap);
//			locationsmap.put(path.toString(), count);
//			count++;
			return true;
		}
		else {
			if(!dictionary.get(element).containsKey(path.toString())) {
				TreeSet<Integer> positionset = new TreeSet<>();
				positionset.add(position);
				dictionary.get(element).put(path.toString(), positionset);
//				locationsmap.put(path.toString(), count);
//				count++;
				
				return true;
			}
			else{
//				if(!dictionary.get(element).get(path.toString()).contains(position)) {
				return dictionary.get(element).get(path.toString()).add(position);
//					return true;
//				}
			}
		}
		
	}
	

	


	public int numPositions(String element) {
		// TODO Auto-generated method stub
		if(!dictionary.containsKey(element) || dictionary.get(element)==null) {
			return 0;
		}
		else {
//			int count = 0;
//			for(Integer position : index.get(element)) {
//				count++;
//			}
//			return count;
			return dictionary.get(element).size();
		}
	}


	public int numElements() {
		// TODO Auto-generated method stub
		if(dictionary.isEmpty()) {
			return 0;
		}
		else {
//			int count = 0;
//			for(String element : index.keySet()) {
//				count++;
//			}
//			return count;
			return dictionary.size();
		}	
	}

	public boolean contains(String element) {
		// TODO Auto-generated method stub
		return dictionary.containsKey(element);
	}

//	@Override
//	public boolean contains(String element, int position) {
//		// TODO Auto-generated method stub
//		if(dictionary.containsKey(element) && dictionary.get(element).contains(position)) {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	

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


	public boolean contains(String element, int position) {
		// TODO Auto-generated method stub
		return false;
	}


	public Collection<Integer> getPositions(String element) {
		// TODO Auto-generated method stub
		return null;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public Collection<Integer> getPositions(String element) {
//		// TODO Auto-generated method stub
//		try {
//			ArrayList<Integer> positions = new ArrayList<>();
//			if(!dictionary.containsKey(element)) {
//				Collection<Integer> immutablelist = Collections.unmodifiableCollection(positions);
//				return immutablelist;
//			}
//			else {
//				for(Integer position : dictionary.get(element)) {
//					positions.add(position);
//				}
//				Collection<Integer> immutablelist = Collections.unmodifiableCollection(positions);
//				return immutablelist;
//			}
//
//			
//		}catch(UnsupportedOperationException e) {
//			return (Collection<Integer>) e;
//		}
//	}
	


	/*
	 * TODO Modify anything within this class as necessary. This includes the class
	 * declaration; you need to implement the Index interface!
	 */

}
