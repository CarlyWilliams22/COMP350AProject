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

	public void unzipLocally(String PATH) {
		File currentFile;
		FileInputStream fileInput;

		byte[] buffer = new byte[1024];

		try {
			createFolder();

			fileInput = new FileInputStream(PATH);
			ZipInputStream zipInput = new ZipInputStream(fileInput);
			ZipEntry entry = zipInput.getNextEntry();

			while (entry != null) {
				String fileName = entry.getName();
				File file = new File(fileName);

				FileOutputStream fileOutput = new FileOutputStream(
						currentFile = new File("Storage\\" + file.getName()));

				int len;

				while ((len = zipInput.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, len);
				}

				files.add(currentFile);

				fileOutput.close();
				zipInput.closeEntry();

				entry = zipInput.getNextEntry();
			} // while

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private void createFolder() throws IOException {
		File folder = new File("Storage\\");

		if (!folder.exists()) {
			folder.mkdir();
		}
	}

	private void addFile(File file) {
		files.add(file);
	}

}
