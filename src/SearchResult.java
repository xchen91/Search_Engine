/**
 * a class that demonstrate the SearchResult data structure
 * 
 * @author tracyair
 *
 */
public class SearchResult implements Comparable<SearchResult> {
	/**
	 * The location of the search result
	 */
	private final String location;
	/**
	 * The total word count of the search result
	 */
	private final int wordCount;
	/**
	 * The times of the occurrence of the search result
	 */
	private int times;

	/**
	 * Class constructor
	 *
	 * @param location  the location of the search result
	 * @param wordCount the total word count of the search result
	 * @param times     the occurrence of the search result
	 */
	public SearchResult(String location, int wordCount, int times) {
		this.location = location;
		this.wordCount = wordCount;
		this.times = times;
	}

	/**
	 * Compares two {@link SearchResult} objects first by their scores in descending
	 * order, and if the scores are equal, then by their total word counts in
	 * descending order, and if the total words count are equal, finally by their
	 * location in alphabetical order.
	 *
	 * @param o the another search result compared to
	 */
	@Override
	public int compareTo(SearchResult o) {
		if (this.getScore() == o.getScore()) {
			if (this.getOccurence() == o.getOccurence()) {
				return this.getLocation().compareTo(o.getLocation());
			} else {
				return Integer.compare(o.getOccurence(), this.getOccurence());
			}
		} else {
			return Double.compare(o.getScore(), this.getScore());
		}
	}

	/**
	 * A method that returns the location of the search result
	 * 
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * A method that returns the total word count of the search result.
	 * 
	 * @return wordCount
	 */
	public int getWordcount() {
		return wordCount;
	}

	/**
	 * A method that returns the occurrence of the search result.
	 * 
	 * @return times
	 */
	public int getOccurence() {
		return times;
	}

	/**
	 * A method that returns the score of the search result, the score will be used
	 * in the sort method.
	 * 
	 * @return score
	 */
	public double getScore() {
		return ((double) times / wordCount);
	}

	/**
	 * A method that updates the occurs, when there are multiple words in a single
	 * line.
	 * 
	 * @param newCount a new occurs which should be added
	 */
	public void updateOccurence(int newCount) {
		this.times = this.times + newCount;
	}

}