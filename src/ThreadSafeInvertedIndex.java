import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author tracyair
 *
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {
	/**
	 * 
	 */
	private final SimpleReadWriteLock lock;

	/**
	 * 
	 */
	public ThreadSafeInvertedIndex() {
		super();
		this.lock = new SimpleReadWriteLock();
	}

	/**
	 * the ThreadSafe version of toJSON method
	 * 
	 * @param path the output file path to print JSON file
	 * @throws IOException
	 */

	public void toJSON(Path path) throws IOException {
		this.lock.readLock().lock();
		try {
			super.toJSON(path);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of numtoJSON method
	 * 
	 * @param path the output file path to print JSON file
	 * @throws IOException
	 */
	public void locationsJSON(Path path) throws IOException {
		this.lock.readLock().lock();
		try {
			super.numtoJSON(path);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * Returns the thread safe string representation of this index.
	 */
	public String toString() {
		this.lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of add method
	 *
	 * @param element  word to clean and add to index
	 * @param location location word was found
	 * @param position the position the word at
	 */
	public void add(String element, String location, int position) {
		this.lock.writeLock().lock();
		try {
			super.add(element, location, position);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of numPositions method
	 * 
	 * @param element
	 * @return number of positions
	 */
	public int numPositions(String element) {
		this.lock.readLock().lock();
		try {
			return super.numPositions(element);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of numElements method
	 * 
	 * @return number of elements
	 */
	public int numElements() {
		this.lock.readLock().lock();
		try {
			return super.numElements();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of contains method
	 *
	 * @param element to look for
	 * @return true if the element is stored in the index
	 */
	public boolean contains(String element) {
		this.lock.readLock().lock();
		try {
			return super.contains(element);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of contains method
	 * 
	 * @param element
	 * @param location
	 * @return true if the element is in the index at the given location
	 */
	public boolean contains(String element, String location) {
		this.lock.readLock().lock();
		try {
			return super.contains(element, location);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of count method
	 * 
	 * @return number of words
	 */
	public int count() {
		this.lock.readLock().lock();
		try {
			return super.count();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of count method
	 * 
	 * @param word
	 * @return number of locations
	 */
	public int count(String word) {
		this.lock.readLock().lock();
		try {
			return super.count(word);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * the ThreadSafe version of count method
	 * 
	 * @param word
	 * @param location
	 * @return number of positions
	 */
	public int count(String word, String location) {
		this.lock.readLock().lock();
		try {
			return super.count(word, location);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	/**
	 * A general ThreadSafe version of search method given queries for searching and
	 * a flag deciding exact search or partial search
	 * 
	 * @param queries the query line for searching
	 * @param exact   a boolean checking if it is exact search or partial search
	 * @return search the search result
	 */
	public ArrayList<SearchResult> search(Collection<String> queries, boolean exact) {
		this.lock.readLock().lock();
		try {
			return exact ? exactSearch(queries) : partialSearch(queries);
		} finally {
			this.lock.readLock().unlock();
		}
	}

}
