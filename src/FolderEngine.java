import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private PlagiarismEngine pe;
	private ArrayList<File> files;

	public FolderEngine() {
		pe = new PlagiarismEngine();
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
	public void recursiveUnzip(String PATH) {
		int MEMORY = 2048;

		try {
			// create zip file
			ZipFile zip = new ZipFile(PATH);

			// get zip file path
			String path = PATH.substring(0, PATH.length() - 4);

			// get all zip file entries
			Enumeration zipFileEntries = zip.entries();

			// unzip each entry
			while (zipFileEntries.hasMoreElements()) {

				// get zip file
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();

				// create destination folder
				File file = new File(path, currentEntry);
				File parent = file.getParentFile();
				parent.mkdirs();
				System.out.println("Unzipping " + entry.getName());

				// write data to folder
				if (!entry.isDirectory()) {
					int len; // bytes left to write
					BufferedInputStream inputBuffer = new BufferedInputStream(zip.getInputStream(entry));
					byte buffer[] = new byte[MEMORY];

					// write the file
					FileOutputStream outputStream = new FileOutputStream(file);
					BufferedOutputStream outputBuffer = new BufferedOutputStream(outputStream, MEMORY);

					while ((len = inputBuffer.read(buffer, 0, MEMORY)) > 0) {
						outputStream.write(buffer, 0, len);
					}

					System.out.println("Unzipped to " + file.getAbsolutePath());

					outputBuffer.flush();
					outputBuffer.close();
					outputStream.close();
					inputBuffer.close();

					// unzip another zip file
					if (currentEntry.endsWith(".zip")) {
						recursiveUnzip(file.getAbsolutePath());
					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void unzipLocally(String PATH) {
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
						"C:\\Users\\moodyms18\\git\\COMP350AProject\\Storage\\" + file);
				int len;

				while ((len = zipInput.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, len);
				} // while

				fileOutput.close();
				zipInput.closeEntry();

				System.out.println("Unzipped to " + file.getAbsolutePath());

				entry = zipInput.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createStudents() {

		File folder = new File("C:\\Users\\moodyms18\\git\\COMP350AProject\\Storage");
		Student stu;
		int ID = 0;
		String name;

		System.out.println(folder.exists());
		
		for (File file : folder.listFiles()) {
			System.out.println(file.isFile());
			stu = new Student(ID, file.getName());
			stu.addFile(file);
			pe.addStudent(stu);
		}

	}

}
