import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A special type of {@link Index} that indexes the locations words were found.
 */
public class WordIndex implements Index<String>{
	
	private HashMap<String, HashSet<Integer>> dictionary;
	
	public WordIndex() {
		dictionary = new HashMap<>();
	}


	@Override
	public boolean add(String element, int position) {
		// TODO Auto-generated method stub
		if(!dictionary.containsKey(element)) {
			HashSet<Integer> positionset = new HashSet<>();
			positionset.add(position);
			dictionary.put(element, positionset);
					
			return true;
		}
		else {
			if(!dictionary.get(element).contains(position)) {
				dictionary.get(element).add(position);
				return true;
			}
			return false;
		}
	}

	@Override
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

	@Override
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
	@Override
	public boolean contains(String element) {
		// TODO Auto-generated method stub
		return dictionary.containsKey(element);
	}

	@Override
	public boolean contains(String element, int position) {
		// TODO Auto-generated method stub
		if(dictionary.containsKey(element) && dictionary.get(element).contains(position)) {
			return true;
		}
		else {
			return false;
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
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

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Integer> getPositions(String element) {
		// TODO Auto-generated method stub
		try {
			ArrayList<Integer> positions = new ArrayList<>();
			if(!dictionary.containsKey(element)) {
				Collection<Integer> immutablelist = Collections.unmodifiableCollection(positions);
				return immutablelist;
			}
			else {
				for(Integer position : dictionary.get(element)) {
					positions.add(position);
				}
				Collection<Integer> immutablelist = Collections.unmodifiableCollection(positions);
				return immutablelist;
			}
//			for(Integer position : dictionary.get(element)) {
//				positions.add(position);
//			}
//			Collection<Integer> immutablelist = Collections.unmodifiableCollection(positions);
//			return immutablelist;
		}catch(UnsupportedOperationException e) {
			return (Collection<Integer>) e;
		}
	}

	/*
	 * TODO Modify anything within this class as necessary. This includes the class
	 * declaration; you need to implement the Index interface!
	 */

}
