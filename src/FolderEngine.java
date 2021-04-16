import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private static ArrayList<File> files;

	public FolderEngine() {
		files = new ArrayList<File>();

	}
	
	//TODO: Make it work with a zip folder that has a nonzipped folder directly inside (testCodeFromDesktop)

	// TODO: Make it work with a zip folder that has a nonzipped folder directly
	// inside (testCodeFromDesktop)
	// TODO: make it actually put it in one storage folder instead of lots of
	// packages
	public static void main(String[] args) {
		FolderEngine testFE = new FolderEngine();
		// This set of test code is the one that doesn't work
		String testCodeFromDesktop = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles.zip";
		// testFE.unzipRecursive(testCodeFromDesktop, "Storage/");

		String testCodeFromVal = "C:\\Users\\lloydta18\\Downloads\\SectA_stupidCopies.zip";

		// This set of nonzipped test code works
		String nonzippedTestCode = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles";
		// testFE.unzipRecursive(nonzippedTestCode, "Storage/");

		String aSingleJavaFile = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles\\French Main";

		// This set of code (sectionBCode) works
		String sectionBCode = "C:\\Users\\lloydta18\\Downloads\\SectB_OrigCodes.zip";
		testFE.unzipRecursive(sectionBCode, "Storage\\");

		for (int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i).toString());
		}

//		try {
//			testFE.fourthTry(sectionBCode);
//		} catch (ZipException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		System.out.println("size of file array: " + files.size());
//		
//		System.out.println("\n\nPrinting out files array: ");
//		for(int i = 0; i < files.size(); i++) {
//			System.out.println(files.get(i).getName());
//		}

		// testFE.unzipThirdTry("C:\\Users\\lloydta18\\git\\COMP350Project\\COMP350AProject\\Storage");
		System.out.println("<<NORMAL TERMINATION>>");
	}

	// Based on howtodoinjava article code:
	// https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
	public void unzipRecursive(String PATH, String targetDir) {
		if (PATH.endsWith(".zip")) {
			try (ZipFile zf = new ZipFile(PATH)) {
				FileSystem fs = FileSystems.getDefault();
				Enumeration<? extends ZipEntry> entries = zf.entries();

				if (Files.notExists(fs.getPath(targetDir))) {
					Files.createDirectory(fs.getPath(targetDir));
				}

				while (entries.hasMoreElements()) {
					ZipEntry ze = entries.nextElement();
					if (ze.isDirectory()) {
						System.out.println("making dir: " + targetDir + ze.getName());
						Files.createDirectories(fs.getPath(targetDir + ze.getName()));
						// Files.createFile(fs.getPath(targetDir + ze.getName()));
					} else {
						if (ze.getName().endsWith(".zip") || ze.getName().endsWith(".java")) {
							InputStream is = zf.getInputStream(ze);
							BufferedInputStream bis = new BufferedInputStream(is);
							String uncompFileName = targetDir + ze.getName();
							Path uncompFilePath = fs.getPath(uncompFileName);
							Path zipFileLoc = Files.createFile(uncompFilePath);
							FileOutputStream fileOutput = new FileOutputStream(uncompFileName);
							while (bis.available() > 0) {
								fileOutput.write(bis.read());
							}
							if (ze.getName().endsWith(".zip")) {
								System.out.println("Path generated: " + zipFileLoc);
								unzipRecursive(zipFileLoc.toString(),
										zipFileLoc.toString().substring(0, zipFileLoc.toString().length() - 4));
							}
							fileOutput.close();
							System.out.println("Written: " + ze.getName());
							//System.out.println(zipFileLoc.toFile().getAbsolutePath());

							if (zipFileLoc.toFile().getAbsolutePath().endsWith(".java")) {
								files.add(zipFileLoc.toFile());
							}
						}
					}
				}
			} catch (FileAlreadyExistsException faee) {
				faee.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			File nonZippedFile = new File(PATH);
			File[] arrayOfFiles = { nonZippedFile };
			lookInsideNonZippedFolder(PATH, arrayOfFiles, targetDir);
		}
	}// 3rd try

	// Based on howtodoinjava article code:
		// https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
		public void unzipRecursivewithMods(String PATH, String targetDir) {
			if (PATH.endsWith(".zip")) {
				try (ZipFile zf = new ZipFile(PATH)) {
					FileSystem fs = FileSystems.getDefault();
					Enumeration<? extends ZipEntry> entries = zf.entries();

//					if(Files.notExists(fs.getPath(targetDir))) {
//						Files.createDirectory(fs.getPath(targetDir));
//					}
					String storageFolderName = targetDir;
					File storageDir = new File(storageFolderName);
					if (storageDir.createNewFile()) {
						System.out.println(storageDir.getName() + " file created");
					} else {
						System.out.println(storageDir.getName() + " file already exists");
					}

					while (entries.hasMoreElements()) {
						ZipEntry ze = entries.nextElement();
						if (ze.isDirectory()) {
							System.out.println("making dir: " + targetDir + ze.getName());
							// Files.createDirectories(fs.getPath(targetDir + ze.getName()));
							// Files.createFile(fs.getPath(targetDir + ze.getName()));
							File currDir = new File(storageDir, ze.getName());
						} else {
							if (ze.getName().endsWith(".zip") || ze.getName().endsWith(".java")) {
								InputStream is = zf.getInputStream(ze);
								BufferedInputStream bis = new BufferedInputStream(is);
								String uncompFileName = targetDir + ze.getName();
								File currFile = new File(uncompFileName);
//								if(currFile.createNewFile()) {
//									System.out.println(currFile.getName() + " file created");
//								} else {
//									System.out.println(currFile.getName() + " file already exists");
//								}
								String currFileLoc = currFile.getPath();
								// Path uncompFilePath = fs.getPath(uncompFileName);
								// Path zipFileLoc = Files.createFile(uncompFilePath);
								// FileOutputStream fileOutput = new FileOutputStream(uncompFileName);
								FileOutputStream fileOutput = new FileOutputStream(currFile);
								while (bis.available() > 0) {
									fileOutput.write(bis.read());
								}
								if (ze.getName().endsWith(".zip")) {
									// System.out.println("Path generated: " + zipFileLoc);
									System.out.println("Path generated: " + currFileLoc);
									// unzipRecursive(zipFileLoc.toString(), zipFileLoc.toString().substring(0,
									// zipFileLoc.toString().length() - 4));
									unzipRecursive(currFileLoc, currFileLoc.substring(0, currFileLoc.length() - 4));
								}
								fileOutput.close();
								System.out.println("Written: " + ze.getName());
								// System.out.println(zipFileLoc.toFile().getAbsolutePath());
								System.out.println(currFile.getAbsolutePath());

//								if(zipFileLoc.toFile().getAbsolutePath().endsWith(".java")) {
//									files.add(zipFileLoc.toFile());
//								}
								if (currFile.getAbsolutePath().endsWith(".java")) {
									files.add(currFile);
								}
							}
						}
					}
				} catch (FileAlreadyExistsException faee) {
					faee.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				File nonZippedFile = new File(PATH);
				File[] files = { nonZippedFile };
				lookInsideNonZippedFolder(PATH, files, targetDir);
			}
		}// 3rd try

		// based on StackOverflow:
		// https://stackoverflow.com/questions/981578/how-to-unzip-files-recursively-in-java
		public void fourthTry(String zipFile) throws ZipException, IOException {
			System.out.println(zipFile);
			int BUFF = 2048;
			File file = new File(zipFile);

			ZipFile zip = new ZipFile(file);
			System.out.println("Zip name: " + zip.getName());
			String newPath = zipFile.substring(0, zipFile.length() - 4);

			new File(newPath).mkdir();
			Enumeration zipFileEntries = zip.entries();

			// process each entry
			while (zipFileEntries.hasMoreElements()) {
				// grab a zip file entry
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				String currentEntry = entry.getName();
				File destFile = new File(newPath, currentEntry);
				File destinationParent = destFile.getParentFile();

				// create the parent directory structure if needed
				destinationParent.mkdirs();

				if (!entry.isDirectory()) {
					BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
					int currentByte;
					// establish buffer for writing file
					byte data[] = new byte[BUFF];

					// write the current file to disk
					FileOutputStream fos = new FileOutputStream(destFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFF);

					// read and write until last byte is encountered
					while ((currentByte = is.read(data, 0, BUFF)) != -1) {
						dest.write(data, 0, currentByte);
					}
					dest.flush();
					dest.close();
					is.close();
				}

				if (currentEntry.endsWith(".zip")) {
					// found a zipFile, try to open
					fourthTry(destFile.getAbsolutePath());
				}
			}
		}

	public void lookInsideNonZippedFolder(String PATH, File[] fileArray, String targetDir) {
		FileSystem fs = FileSystems.getDefault();

		try {
			if (Files.notExists(fs.getPath(targetDir))) {
				Files.createDirectory(fs.getPath(targetDir));
			}

			for (File currFile : fileArray) {
				if (currFile.getName().endsWith(".zip")) {
					unzipRecursive(PATH, targetDir);
				}
				if (currFile.isDirectory()) {
					System.out.println("making dir: " + targetDir + currFile.getName());
					Files.createDirectories(fs.getPath(targetDir + currFile.getName()));
					lookInsideNonZippedFolder(targetDir + currFile.getName(), currFile.listFiles(),
							targetDir + currFile.getName());
				} else {
					InputStream is = new FileInputStream(currFile);
					BufferedInputStream bis = new BufferedInputStream(is);
					Path currFilePath = fs.getPath(targetDir + currFile.getName());
					Path unzipppedFileLoc = Files.createFile(currFilePath);
					FileOutputStream fileOutput = new FileOutputStream(currFile);
					while (bis.available() > 0) {
						fileOutput.write(bis.read());
					}
					fileOutput.close();
					System.out.println("Written: " + currFile.getName());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unzips a single zip file of files in a storage folder NOTE: I left this in,
	 * but as of now, I don't use it
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
