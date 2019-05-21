import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author tracyair
 *
 */
public class WebCrawler {
	private final Collection<URL> links;
	private final ThreadSafeInvertedIndex index;
	private final WorkQueue queue;
	/**
	 * 
	 */
	int threads;

	/**
	 * @param index
	 * @param threads
	 */
	public WebCrawler(ThreadSafeInvertedIndex index, int threads) {
		this.links = new HashSet<URL>();
		this.index = index;
		this.queue = new WorkQueue(threads);
		this.threads = threads;
	}

}