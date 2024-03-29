import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs several tree-based data structures in "pretty" JSON format where
 * newlines are used to separate elements, and nested elements are indented.
 *
 * Warning: This class is not thread-safe. If multiple threads access this class
 * concurrently, access must be synchronized externally.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Spring 2019
 */
public class PrettyJSONWriter {

	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asArray(TreeSet<Integer> elements, Writer writer, int level) throws IOException {
		writer.write("[");
		writer.write("\n");
		if (!elements.isEmpty()) {
			for (Integer element : elements.headSet(elements.last())) {
				indent(writer, level + 1);
				writer.write(element.toString());
				writer.write(",");
				writer.write("\n");
			}
			indent(writer, level + 1);
			writer.write(elements.last().toString());
			writer.write("\n");
		}
		indent(writer, level);
		writer.write("]");

	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static void asArray(TreeSet<Integer> elements, Path path) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(TreeSet, Writer, int)
	 */
	public static String asArray(TreeSet<Integer> elements) {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asObject(TreeMap<String, Integer> elements, Writer writer, int level) throws IOException {
		writer.write("{");
		writer.write("\n");
		if (!elements.isEmpty()) {
			for (String key : elements.headMap(elements.lastKey()).keySet()) {
				indent(writer, level + 1);
				quote(key, writer);
				writer.write(": ");
				writer.write(elements.get(key).toString());
				writer.write(",\n");
			}
			indent(writer, level + 1);
			quote(elements.lastKey(), writer);
			writer.write(": ");
			writer.write(elements.get(elements.lastKey()).toString());
			writer.write("\n");
		}
		indent(writer, level);
		writer.write("}");

	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static void asObject(TreeMap<String, Integer> elements, Path path) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(TreeMap, Writer, int)
	 */
	public static String asObject(TreeMap<String, Integer> elements) {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Writer writer, int level)
			throws IOException {
		writer.write("{");
		writer.write("\n");
		if (!elements.isEmpty()) {
			for (String key : elements.headMap(elements.lastKey()).keySet()) {
				indent(writer, level + 1);
				quote(key, writer);
				writer.write(": ");
				asArray(elements.get(key), writer, level + 1);
				writer.write(",");
				writer.write("\n");
			}
			indent(writer, level + 1);
			quote(elements.lastKey(), writer);
			writer.write(": ");
			asArray(elements.get(elements.lastKey()), writer, level + 1);
			writer.write("\n");
		}
		indent(writer, level);
		writer.write("}");

	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException if the writer encounters any issues
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static void asNestedObject(TreeMap<String, TreeSet<Integer>> elements, Path path) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(TreeMap, Writer, int)
	 */
	public static String asNestedObject(TreeMap<String, TreeSet<Integer>> elements) {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the sorted map of elements as a pretty JSON object using the provided
	 * {@link Writer} and indentation level.
	 * 
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indentation level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedTreeMapMap(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Writer writer,
			int level) throws IOException {
		writer.write("{");
		writer.write("\n");
		if (!elements.isEmpty()) {
			for (String key : elements.headMap(elements.lastKey()).keySet()) {
				indent(writer, level + 1);
				quote(key, writer);
				writer.write(": ");
				asNestedObject(elements.get(key), writer, level + 1);
				writer.write(",");
				writer.write("\n");
			}
			indent(writer, level + 1);
			quote(elements.lastKey(), writer);
			writer.write(": ");
			asNestedObject(elements.get(elements.lastKey()), writer, level + 1);
			writer.write("\n");
		}
		indent(writer, level);
		writer.write("}");
	}

	/**
	 * Writes the sorted map of elements as a pretty JSON object to the output index
	 * file
	 * 
	 * @param elements the elements to convert to JSON
	 * @param path     write a pretty JSON file into this path
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedTreeMapMap(TreeMap<String, TreeMap<String, TreeSet<Integer>>> elements, Path path)
			throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedTreeMapMap(elements, writer, 0);
		}
	}

	/**
	 * Writes the location of one search result and some related info as a pretty
	 * JSON format using the provided {@link Writer} and indentation level.
	 * 
	 * @param locations single search result
	 * @param writer    the writer to use
	 * @param level     the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedLocation(SearchResult locations, Writer writer, int level) throws IOException {
		DecimalFormat FORMATTER = new DecimalFormat("0.00000000");
		indent(writer, level);
		writer.write("{");
		if (locations != null) {
			writer.write("\n");
			indent(writer, level + 1);
			quote("where", writer);
			writer.write(": ");
			quote(locations.getLocation(), writer);
			writer.write(",");
			writer.write("\n");
			indent(writer, level + 1);
			quote("count", writer);
			writer.write(": ");
			Integer occurence = locations.getOccurence();
			writer.write(occurence.toString());
			writer.write(",");
			writer.write("\n");
			indent(writer, level + 1);
			quote("score", writer);
			writer.write(": ");
			writer.write(FORMATTER.format(locations.getScore()));
		}
		writer.write("\n");
		indent(writer, level);
		writer.write("}");
	}

	/**
	 * Writes a list of search result of a query line as a pretty JSON format using
	 * the provided {@link Writer} and indentation level.
	 * 
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedList(ArrayList<SearchResult> elements, Writer writer, int level) throws IOException {
		if (!elements.isEmpty()) {
			writer.write("\n");
			var iterator = elements.iterator();
			asNestedLocation(iterator.next(), writer, level + 1);
			while (iterator.hasNext()) {
				writer.write(",");
				writer.write("\n");
				asNestedLocation(iterator.next(), writer, level + 1);
			}
		}
		writer.write("\n");
		indent(writer, level);
		writer.write("]");

	}

	/**
	 * Writes a query line of search result as a pretty JSON format using the
	 * provided {@link Writer} and indentation level.
	 * 
	 * @param query    the query lines to convert to JSON
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedQuery(String query, ArrayList<SearchResult> elements, Writer writer, int level)
			throws IOException {
		indent(writer, level);
		quote(query, writer);
		writer.write(": ");
		writer.write("[");
		asNestedList(elements, writer, level);
	}

	/**
	 * Writes the sorted map of search result as a pretty JSON format using the
	 * provided {@link Writer} and indentation level.
	 *
	 * @param elements the elements to convert to JSON
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedSearchResult(TreeMap<String, ArrayList<SearchResult>> elements, Writer writer, int level)
			throws IOException {
		indent(writer, level);
		writer.write("{");
		writer.write("\n");
		if (!elements.isEmpty()) {
			Iterator<String> iterator = elements.keySet().iterator();
			String current = iterator.next();
			asNestedQuery(current, elements.get(current), writer, level + 1);
			while (iterator.hasNext()) {
				current = iterator.next();
				writer.write(",");
				writer.write("\n");
				asNestedQuery(current, elements.get(current), writer, level + 1);
			}
		}
		writer.write("\n");
		indent(writer, level);
		writer.write("}");
	}

	/**
	 * Writes the query line and a list of SearchResult of that line as a pretty
	 * JSON format to the output file
	 * 
	 * @param elements the elements to convert to JSON
	 * @param path     write a pretty JSON file into this path
	 * @throws IOException if the writer encounters any issues
	 */
	public static void asNestedSearchResult(TreeMap<String, ArrayList<SearchResult>> elements, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedSearchResult(elements, writer, 0);
		}
	}

	/**
	 * Writes the {@code \t} tab symbol by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException
	 */
	public static void indent(Writer writer, int times) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException
	 */
	public static void quote(String element, Writer writer) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException
	 *
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException {
		// THIS IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		quote(element, writer);
	}

	/**
	 * A simple main method that demonstrates this class.
	 *
	 * @param args unused
	 */
	public static void main(String[] args) {
		// Modify as needed to debug!
		TreeSet<Integer> elements = new TreeSet<>();
		System.out.println("Empty:");
		System.out.println(asArray(elements));

		elements.add(65);
		System.out.println("\nSingle:");
		System.out.println(asArray(elements));

		elements.add(66);
		elements.add(67);
		System.out.println("\nSimple:");
		System.out.println(asArray(elements));
	}
}
