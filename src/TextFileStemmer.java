import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
//import java.nio.file.Paths;
//import java.util.Set;
//import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

import java.io.BufferedReader;


/**
 * Utility class for parsing and stemming text and text files into sets of
 * stemmed words.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 *
 * @see TextParser
 */
public class TextFileStemmer {

	/** The default stemmer algorithm used by this class. */
	public static final SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Returns a set of cleaned and stemmed words parsed from the provided line.
	 * Uses the {@link #DEFAULT} algorithm for stemming.
	 *
	 * @param line the line of words to clean, split, and stem
	 * @return a sorted set of cleaned and stemmed words
	 *
	 * @see SnowballStemmer
	 * @see #DEFAULT
	 * @see #stemLine(String, Stemmer)
	 */
	public static ArrayList<String> stemLine(String line) {
		// THIS IS PROVIDED FOR YOU; NO NEED TO MODIFY
		return stemLine(line, new SnowballStemmer(DEFAULT));
	}

	/**
	 * Returns a set of cleaned and stemmed words parsed from the provided line.
	 *
	 * @param line    the line of words to clean, split, and stem
	 * @param stemmer the stemmer to use
	 * @return a sorted set of cleaned and stemmed words
	 *
	 * @see Stemmer#stem(CharSequence)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> stemLine(String line, Stemmer stemmer) {
		String[] parsedarray = TextParser.parse(line);
		ArrayList<String> stemlist = new ArrayList<>();
		for(String parsedstring: parsedarray) {
			stemlist.add(stemmer.stem(parsedstring).toString());
		}
		return stemlist;
	}

	/**
	 * Reads a file line by line, parses each line into cleaned and stemmed words,
	 * and then adds those words to a set.
	 *
	 * @param inputFile  the input file to parse
	 * @return a sorted set of stems from file
	 * @throws IOException if unable to read or parse file
	 *
	 * @see #stemLine(String)
	 * @see TextParser#parse(String)
	 */
	public static ArrayList<String> stemFile(Path inputFile) throws IOException {
		ArrayList<String> resultList = new ArrayList<>();
		try(BufferedReader reader = Files.newBufferedReader(inputFile)){
			String line;
			while((line = reader.readLine())!= null) {
				ArrayList<String> stemList = TextFileStemmer.stemLine(line);
				resultList.addAll(stemList);
			}
			return resultList;
		}
	}

	/**
	 * A simple main method that demonstrates this class.
	 *
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String text = "practic practical practice practiced practicer practices "
				+ "practicing practis practisants practise practised practiser "
				+ "practisers practises practising practitioner practitioners";

		System.out.println(stemLine(text));
	}
}
