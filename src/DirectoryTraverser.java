import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/*
 * TODO Generally you do NOT include a blank line between the Javadoc comment
 * and the element the comment is for!
 */

/**
 * This class demonstrates how to use a {@link DirectoryTraverser} to create a
 * recursive file listing.
 */
public class DirectoryTraverser {

	/**
	 * a private method safely starts the recursive traversal with the proper
	 * padding and keep add the text file found in that directory to the list of
	 * path
	 * @param path  to traverse
	 * @param paths list of paths needed to be added
	 * @throws IOException
	 */
	private static void privateTraverse(Path path, ArrayList<Path> paths) throws IOException {
		if (Files.isDirectory(path)) {
			try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
				for (Path file : listing) {
					DirectoryTraverser.privateTraverse(file, paths);
				}
			}
		} else if (isTextFile(path)) {
			paths.add(path);
		}
	}

	/**
	 * Check if the given path ends with .txt or .text
	 * 
	 * @param path to filter
	 * @return true if the given path ends with .txt or .text
	 */
	public static boolean isTextFile(Path path) {
		String lower = path.getFileName().toString().toLowerCase();
		return lower.endsWith(".txt") || lower.endsWith(".text");
	}

	/**
	 * Safely starts the recursive traversal with the proper padding. Users of this
	 * class can access this method, so some validation is required.
	 * 
	 * @param directory to traverse
	 * @return a list of path which contains all text file found under that
	 *         directory
	 * @throws IOException
	 */
	public static ArrayList<Path> publicTraverse(Path directory) throws IOException {
		ArrayList<Path> paths = new ArrayList<>();
		privateTraverse(directory, paths);
		return paths;
	}
}
