import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * @author tracyair
 *
 */
public class InvertedIndexBuilder {

	/**
	 * @param path
	 * @param index
	 * @throws IOException
	 */
	public static void build(Path path, InvertedIndex index) throws IOException {
		for (Path file : DirectoryStreamDemo.publicTraverse(path)) {
			InvertedIndexBuilder.singleAdd(file, index);
		}
	}

	/**
	 * @param path
	 * @param index
	 * @throws IOException
	 */
	public static void singleAdd(Path path, InvertedIndex index) throws IOException {
		int position = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			String line;
			Stemmer stemmer = new SnowballStemmer(SnowballStemmer.ALGORITHM.ENGLISH);
			while ((line = reader.readLine()) != null) {
				for (String word : TextParser.parse(line)) {
					index.add(stemmer.stem(word).toString(), path.toString(), position);
					position++;
				}
			}
		}
	}

}