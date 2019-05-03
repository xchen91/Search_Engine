import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Build an inverted index data structure for given directory path
 * 
 * @author tracyair
 *
 */
public class InvertedIndexBuilder {

	/**
	 * Adds every words in all text file in this list of path and the position it
	 * was found to the inverted index.
	 * 
	 * @param path  a path contains words and index
	 * @param index an inverted index data structure
	 * @throws IOException
	 */
	public static void build(Path path, InvertedIndex index) throws IOException {
		for (Path file : DirectoryTraverser.publicTraverse(path)) {
			addFile(file, index);
		}
	}

	/**
	 * Add a single file to inverted index
	 * 
	 * @param path  a single file path containing words needed to be added to
	 *              inverted index
	 * @param index an inverted index data structure
	 * @throws IOException
	 */
	public static void addFile(Path path, InvertedIndex index) throws IOException {
		int position = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			String location = path.toString();
			while ((line = reader.readLine()) != null) {
				for (String word : TextParser.parse(line)) {
					index.add(stemmer.stem(word).toString(), location, position);
					position++;
				}
			}
		}
	}

}