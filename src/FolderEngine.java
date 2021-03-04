import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FolderEngine {

	private PlagiarismEngine pe;
	private ArrayList<File> errorFiles;

	public FolderEngine() {
		pe = new PlagiarismEngine();
		errorFiles = new ArrayList<File>();
	}

	public void unzip(String source) {
		int MEMORY = 2048;

		try {
			// create zip file
			ZipFile zip = new ZipFile(source);

			// get zip file path
			String path = source.substring(0, source.length() - 4);

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
						unzip(file.getAbsolutePath());
					}
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void recursiveUnzip() {

	}

	public void createStudents() {

	}

}
