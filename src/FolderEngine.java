import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private ArrayList<File> files;

	public FolderEngine() {
		files = new ArrayList<File>();
	}

	/**
	 * Unzip
	 * 
	 * Recursively unzips files
	 * 
	 * @param source - absolute file path of zip file
	 * @see Adapted from https://thetopsites.net/article/58771386.shtml
	 */
	public void unzipRecursively(String PATH) {
		File currentFile;
		int MEMORY = 2048;
		//int uniquePrefix = 1;

		try {
			// create zip file
			ZipFile zip = new ZipFile(PATH);

			// get zip file path
			String path = PATH.substring(0, PATH.length() - 4);

			// get all zip file entries
			Enumeration zipFileEntries = zip.entries();
//			int i = 0;
//			while(zipFileEntries.hasMoreElements()) {
//				System.out.println("ZipFileEntry #" + i + " " + zipFileEntries.nextElement());
//				i++;
//			}
			
			// unzip each entry
			while (zipFileEntries.hasMoreElements()) {
				
				// get zip file
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				//uniquePrefix++;
				
				System.out.println("These are the files: " + files.toString());
//				if(files.contains(entry)) {
//					System.out.println("Made it in the if statement!!!");
//					currentEntry = uniquePrefix + currentEntry;
//				}

				System.out.println("This is the value of entry.getName(): " + currentEntry);
				System.out.println("This is the value of entry.toString: " + entry.toString());

				// create destination folder
				File file = new File(path, currentEntry);
				File parent = file.getParentFile();
				parent.mkdirs();
				System.out.println("Unzipping " + entry.getName());
//				currentFile = new File(file.getParent() + file.getName());
				currentFile = new File(file.getName());

				// write data to folder
				if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
					int len; // bytes left to write
					BufferedInputStream inputBuffer = new BufferedInputStream(zip.getInputStream(entry));
					byte buffer[] = new byte[MEMORY];

					// write the file
//					FileOutputStream outputStream = new FileOutputStream(
//							currentFile = new File("Storage\\" + file.getName()));
					FileOutputStream outputStream = new FileOutputStream(
							currentFile);
					BufferedOutputStream outputBuffer = new BufferedOutputStream(outputStream, MEMORY);

					while ((len = inputBuffer.read(buffer, 0, MEMORY)) > 0) {
						outputStream.write(buffer, 0, len);
					}

					System.out.println(files.size());
					files.add(currentFile);
					System.out.println(files.size());

					System.out.println("Unzipped to " + currentFile.getAbsolutePath());

					outputBuffer.flush();
					outputBuffer.close();
					outputStream.close();
					inputBuffer.close();

				}
				
				// unzip another zip file
				if (currentEntry.endsWith(".zip")) {
					unzipRecursively(currentFile.getCanonicalPath());
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void unzipLocally(String PATH) {
		File currentFile;
		FileInputStream fileInput;

		byte[] buffer = new byte[1024];

		try {
			fileInput = new FileInputStream(PATH);
			ZipInputStream zipInput = new ZipInputStream(fileInput);
			ZipEntry entry = zipInput.getNextEntry();

			while (entry != null) {
				String fileName = entry.getName();
				File file = new File(fileName);
				System.out.println("Unzipping " + fileName);

				FileOutputStream fileOutput = new FileOutputStream(
						currentFile = new File("Storage\\" + file.getName()));
				int len;

				while ((len = zipInput.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, len);
				} // while

				System.out.println(files.size());
				files.add(currentFile);
				System.out.println(files.size());

				fileOutput.close();
				zipInput.closeEntry();

				System.out.println("Unzipped to " + currentFile.getCanonicalPath());

				entry = zipInput.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addFile(File file) {
		files.add(file);
	}

	public ArrayList<File> transferFiles() {
		return new ArrayList<File>(files);
	}

	public void printFiles() {
		if (files.size() > 0) {
			for (File file : files) {
				System.out.println(file.getName());
			}
		}
	}

}
