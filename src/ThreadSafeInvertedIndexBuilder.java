import java.io.IOException;
import java.nio.file.Path;

// TODO Need to fill in all of your Javadoc

/**
 * @author tracyair
 *
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * 
	 */
	private static ThreadSafeInvertedIndex index; // TODO Should not be static
	/**
	 * 
	 */
	private int numThreads;

	/**
	 * @param index
	 * @param numThreads
	 */
	public ThreadSafeInvertedIndexBuilder(ThreadSafeInvertedIndex index, int numThreads) {
		super(index);
		ThreadSafeInvertedIndexBuilder.index = index;
		this.numThreads = numThreads;
	}

	/**
	 * @param path
	 * @param index
	 * @param queue
	 * @throws IOException
	 */
	public void build(Path path) throws IOException { // TODO @Override
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
	private static class Task implements Runnable { // TODO Make a non-static class

		/**
		 * 
		 */
		private final Path path;

		/**
		 * @param path
		 * @param index
		 */
		public Task(Path path) {
			this.path = path;
		}

		@Override
		public void run() {
			try {
				InvertedIndexBuilder.addFile(this.path, index);
				
				/* TODO
				InvertedIndex local = new InvertedIndex();
				InvertedIndexBuilder.addFile(this.path, local);
				index.addAll(local);
				*/
				
			} catch (IOException e) {
				System.err.println("Unable to add the file to the index: " + this.path.toString());
			}

		}

	}

}