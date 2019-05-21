import java.io.IOException;
import java.nio.file.Path;

/**
 * @author tracyair
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * a ThreadSafeInvertedIndex data structure
	 */
	private final ThreadSafeInvertedIndex index;
	/**
	 * number of threads
	 */
	private int numThreads;

	/**
	 * Initializes the ThreadSafeInvertedIndexBuilder
	 * 
	 * @param index      a ThreadSafeInvertedIndex data structure
	 * @param numThreads number of threads
	 */
	public ThreadSafeInvertedIndexBuilder(ThreadSafeInvertedIndex index, int numThreads) {
		super(index);
		this.index = index;
		this.numThreads = numThreads;
	}

	@Override
	public void build(Path path) throws IOException {
		WorkQueue queue = new WorkQueue(this.numThreads);
		for (Path singlePath : DirectoryTraverser.publicTraverse(path)) {
			queue.execute(new Task(singlePath));
		}
		queue.finish();
		queue.shutdown();
	}

	/**
	 * @author tracyair
	 *
	 */
	private class Task implements Runnable {

		/**
		 * the file path for adding
		 */
		private final Path path;

		/**
		 * @param path the file path for adding
		 */
		public Task(Path path) {
			this.path = path;
		}

		@Override
		public void run() {
			try {
				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.addFile(this.path, local);
				index.addAll(local);
			} catch (IOException e) {
				System.err.println("Unable to add the file to the index: " + this.path.toString());
			}

		}

	}

}