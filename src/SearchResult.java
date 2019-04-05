/**
 * @author tracyair
 *
 */
public class SearchResult implements Comparable<SearchResult> {
	private final String location;
	private final int wordCount;
	private final int times;

	/**
	 * @param location
	 * @param wordCount
	 * @param times
	 */
	public SearchResult(String location, int wordCount, int times) {
		this.location = location;
		this.wordCount = wordCount;
		this.times = times;
	}

	@Override
	public int compareTo(SearchResult o) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return wordCount
	 */
	public int getWordcount() {
		return wordCount;
	}

	/**
	 * @return times
	 */
	public int getOccurence() {
		return times;
	}

	/**
	 * @return score
	 */
	public double getScore() {
		return (times / wordCount);
	}

}