import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * @author tracyair
 *
 */
public class WebCrawler {
	/**
	 * 
	 */
	private final Collection<URL> links;
	/**
	 * 
	 */
	private final ThreadSafeInvertedIndex index;
	/**
	 * 
	 */
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

	/**
	 * @param seed
	 * @param limit
	 */
	public void crawl(URL seed, int limit) {
		links.add(seed);
		queue.execute(new Task(seed, limit));
		queue.finish();
		queue.shutdown();
	}

	/**
	 * @author tracyair
	 *
	 */
	private class Task implements Runnable {
		/**
		 * 
		 */
		private final URL singleURL;
		/**
		 * 
		 */
		private final int limit;

		/**
		 * @param url
		 * @param limit
		 */
		public Task(URL url, int limit) {

			this.singleURL = url;
			this.limit = limit;
		}

		@Override
		public void run() {
			try {
				var HTML = HtmlFetcher.fetchHTML(this.singleURL, 3);
				if (HTML == null) {
					return;
				}
				InvertedIndex local = new InvertedIndex();
				Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
				int start = 1;
				for (String s : TextParser.parse(HtmlCleaner.stripHtml(HTML))) {
					local.add(stemmer.stem(s).toString(), this.singleURL.toString(), start);
					start++;
				}
				synchronized (links) {
//					links.add(singleURL);
					if (links.size() < limit) {
						ArrayList<URL> Alllinks = HtmlCleaner.listLinks(this.singleURL, HTML);

						for (URL link : Alllinks) {
							if (links.size() >= limit) {
								break;
							} else if (links.contains(link) == false) {
								links.add(link);
								queue.execute((new Task(link, limit)));
							}
						}
					}
				}
				index.addAll(local);
			} catch (IOException e) {
				System.out.println("Unable to crawl with the url provided");
			}

		}

	}

}