import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO Need description
 * @author tracyair
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {
	/**
	 * a simple read and write lock
	 */
	private final SimpleReadWriteLock lock;

	/**
	 * Initializes the ThreadSafeInvertedIndex
	 */
	public ThreadSafeInvertedIndex() {
		super();
		this.lock = new SimpleReadWriteLock();
	}

	@Override
	public void toJSON(Path path) throws IOException {
		this.lock.readLock().lock();
		try {
			super.toJSON(path);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public void numtoJSON(Path path) throws IOException {
		this.lock.readLock().lock();
		try {
			super.numtoJSON(path);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public String toString() {
		this.lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public void add(String element, String location, int position) {
		this.lock.writeLock().lock();
		try {
			super.add(element, location, position);
		} finally {
			this.lock.writeLock().unlock();
		}
	}

	@Override
	public int numPositions(String element) {
		this.lock.readLock().lock();
		try {
			return super.numPositions(element);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public int numElements() {
		this.lock.readLock().lock();
		try {
			return super.numElements();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String element) {
		this.lock.readLock().lock();
		try {
			return super.contains(element);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public boolean contains(String element, String location) {
		this.lock.readLock().lock();
		try {
			return super.contains(element, location);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public int count() {
		this.lock.readLock().lock();
		try {
			return super.count();
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public int count(String word) {
		this.lock.readLock().lock();
		try {
			return super.count(word);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public int count(String word, String location) {
		this.lock.readLock().lock();
		try {
			return super.count(word, location);
		} finally {
			this.lock.readLock().unlock();
		}
	}

	@Override
	public ArrayList<SearchResult> exactSearch(Collection<String> QueryLine) {

		lock.readLock().lock();
		try {
			return super.exactSearch(QueryLine);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public ArrayList<SearchResult> partialSearch(Collection<String> QueryLine) {

		lock.readLock().lock();
		try {
			return super.partialSearch(QueryLine);
		} finally {
			lock.readLock().unlock();
		}
	}

	@Override
	public void addAll(InvertedIndex other) {
		lock.writeLock().lock();
		try {
			super.addAll(other);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
