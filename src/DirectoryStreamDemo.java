import java.io.IOException;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.nio.file.Paths;
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
	 * 
	 */
	public static ArrayList<Path> pathlist;

	
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

	private static void privatetxttraverse(Path path) throws IOException {
		/*
		 * The try-with-resources block makes sure we close the directory stream when
		 * done, to make sure there aren't any issues later when accessing this
		 * directory.
		 *
		 * Note, however, we are still not catching any exceptions. This type of try
		 * block does not have to be accompanied with a catch block. (You should,
		 * however, do something about the exception.)
		 */
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			// Efficiently iterate through the files and subdirectories.
			for (Path file : listing) {
				// Check if this is a subdirectory
				if (Files.isDirectory(file)) {
					// Recursively traverse the subdirectory.
					privatetxttraverse(file);
				}
				else {
					// Add the file size next to the name
					if(file.getFileName().toString().toLowerCase().endsWith(".txt") || file.getFileName().toString().toLowerCase().endsWith(".text")) {
						pathlist.add(file);
					}
					
				}
			}
		}
	}

	/**
	 * Safely starts the recursive traversal with the proper padding. Users of this
	 * class can access this method, so some validation is required.
	 *
	 * @param directory to traverse
	 * @throws IOException
	 */
	public static void publictxttraverse(Path directory) throws IOException {
		pathlist = new ArrayList<>();
		if (Files.isDirectory(directory)) {
			privatetxttraverse(directory);
		}
		else {
			if(directory.getFileName().toString().toLowerCase().endsWith(".txt") || directory.getFileName().toString().toLowerCase().endsWith(".text")) {
				pathlist.add(directory);
			}
		}
	}
	
	
	private static void privatehtmltraverse(Path path) throws IOException{
		try (DirectoryStream<Path> listing = Files.newDirectoryStream(path)) {
			for (Path file : listing) {
				if (Files.isDirectory(file)) {
					privatehtmltraverse(file);
				}
				else {
					if(file.toString().toLowerCase().endsWith(".html")) {
						pathlist.add(file);
					}

				}
			}
		}
		
	}
	
	
	/**
	 * @param directory
	 * @throws IOException
	 */
	public static void publichtmltraverse(Path directory) throws IOException {
		pathlist = new ArrayList<>();
		if (Files.isDirectory(directory)) {
			privatehtmltraverse(directory);
		}
		else {
			if(directory.toString().toLowerCase().endsWith(".html")) {
				pathlist.add(directory);
			}
		}
	}
	
	

	/**
	 * Recursively traverses the current directory and prints the file listing.
	 *
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {		
		Path path = Paths.get(".").toAbsolutePath().normalize();
		System.out.println(path.getFileName() + ":");
		
		publictxttraverse(path);
		publichtmltraverse(path);
	}

}
