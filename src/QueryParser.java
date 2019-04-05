import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author tracyair
 * @param <SearchResult>
 *
 */
public class QueryParser<SearchResult> {
	private final InvertedIndex index;
	private final TreeMap<String, ArrayList<SearchResult>> map;

	/**
	 * @param index
	 */
	public QueryParser(InvertedIndex index) {
		this.index = index;
		this.map = new TreeMap<>();
	}

	public void parse() {

	}

}