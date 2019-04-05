/**
 * @author tracyair
 *
 */
public class SearchResult implements Comparable<SearchResult> {
	private final String location;
	private final int wordCount;
	private int times;

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
		if (this.getScore() == o.getScore()) {
			if (this.getOccurence() == o.getOccurence()) {
				return this.getLocation().compareTo(o.getLocation());
			} else {
				return Integer.compare(this.getOccurence(), o.getOccurence());
			}
		} else {
			return Double.compare(this.getScore(), o.getScore());
		}
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

	/**
	 * @param newCount
	 */
	public void updateOccurence(int newCount) {
		this.times = this.times + newCount;
	}

}