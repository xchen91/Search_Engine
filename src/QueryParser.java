import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author tracyair
 * @param <SearchResult>
 *
 */
public class QueryParser<SearchResult> {
	private final WordIndex index;
	private final TreeMap<String, ArrayList<SearchResult>> map;

	/**
	 * @param index
	 */
	public QueryParser(WordIndex index) {
		this.index = index;
		this.map = new TreeMap<>();
	}

}