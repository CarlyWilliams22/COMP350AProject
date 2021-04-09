import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private ArrayList<File> files;

	public FolderEngine() {
		files = new ArrayList<File>();

	}

	/**
	 * Unzips a single zip file of files in a storage folder
	 * 
	 * @param PATH - zip file path
	 */
	public void unzipLocally(String PATH) {
		File currentFile;
		FileInputStream fileInput;

		byte[] buffer = new byte[1024];

		try {
			// Creates the storage folder
			createFolder();

			// Setup to access each entry in the zip file
			fileInput = new FileInputStream(PATH);
			ZipInputStream zipInput = new ZipInputStream(fileInput);
			ZipEntry entry = zipInput.getNextEntry();

			// Unzips
			while (entry != null) {
				String fileName = entry.getName();
				File file = new File(fileName);

				// Writes unzipped file to storage folder
				FileOutputStream fileOutput = new FileOutputStream(
						currentFile = new File("Storage\\" + file.getName()));

				int len;

				while ((len = zipInput.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, len);
				}

				// Adds file to list for processing
				files.add(currentFile);

				fileOutput.close();
				zipInput.closeEntry();

				// get the next file
				entry = zipInput.getNextEntry();
			} // while

		} catch (FileNotFoundException e) {
			System.err.println("Works only on a single zip folder with regular files.");
//			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transfers a deep copy of files
	 * 
	 * @return deep copy of files
	 */
	public ArrayList<File> transferFiles() {
		return new ArrayList<File>(files);
	}

	/**
	 * Print files for debugging purposes
	 */
	public void printFiles() {
		if (files.size() > 0) {
			for (File file : files) {
				System.out.println(file.getName());
			}
		}
	}

	/**
	 * Partially deletes the contents of the storage folder
	 */
	public void deleteFolder() {
		File folder = new File("Storage\\");

		if (folder.exists()) {

			File[] files = folder.listFiles();

			if (files != null) {

				for (File f : files) {
					f.delete();
				}
			}
			folder.delete();
		}
	}

	/**
	 * Creates the storage folder
	 * 
	 * @throws IOException
	 */
	private void createFolder() throws IOException {
		File folder = new File("Storage\\");

		if (!folder.exists()) {
			folder.mkdir();
		}
	}

}
