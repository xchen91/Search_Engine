import java.io.IOException;


import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * This class demonstrates how to use a {@link DirectoryStream} to create a
 * recursive file listing.
 *
 * @see java.nio.file.Path
 * @see java.nio.file.Paths
 * @see java.nio.file.Files
 * @see java.nio.file.DirectoryStream
 */
public class DirectoryStreamDemo {
	
	
	/**
	 * Outputs the name of the file or subdirectory, with proper indentation to help
	 * indicate the hierarchy. If a subdirectory is encountered, will recursively
	 * list all the files in that subdirectory.
	 *
	 * The recursive version of this method is private. Users of this class will
	 * have to use the public version (see below).
	 *
	 * @param prefix the padding or prefix to put in front of the file or
	 *               subdirectory name
	 * @param path   to retrieve the listing, assumes a directory and not a file is
	 *               passed
	 * @throws IOException
	 */

	private static void privateTraverse(Path path, ArrayList<Path> paths) throws IOException {
		if(Files.isDirectory(path)) {
			try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)){
				for(Path file : listing) {
					DirectoryStreamDemo.privateTraverse(file, paths);
				}
			}
		}
		else {
			if(path.getFileName().toString().toLowerCase().endsWith(".txt")||path.getFileName().toString().toLowerCase().endsWith(".text")) {
				paths.add(path);
			}
		}
		
	}

	/**
	 * Safely starts the recursive traversal with the proper padding. Users of this
	 * class can access this method, so some validation is required.
	 *
	 * @param directory to traverse
	 * @return an ArrayList of text files
	 * @throws IOException
	 */
	public static ArrayList<Path> publicTraverse(Path directory) throws IOException {
		ArrayList<Path> paths = new ArrayList<>();
		privateTraverse(directory, paths);
		return paths;
	}
	

}
