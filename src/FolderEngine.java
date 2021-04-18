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
import java.nio.file.FileStore;
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
	// TODO: make it work with a non zipped folder
	public static void main(String[] args) {
		FolderEngine testFE = new FolderEngine();
		// This set of test code is the one that doesn't work
		String testCodeFromDesktop = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles.zip";
		//testFE.unzipRecursive(testCodeFromDesktop, "Storage/");

		//Still gets stuck on Tyler Ridout's folder, even though it skips files
		String testCodeFromVal = "C:\\Users\\lloydta18\\Downloads\\SectA_stupidCopies.zip";
		//testFE.unzipRecursive(testCodeFromVal, "Storage/");
		
		// This set of nonzipped test code doesn't work
		String nonzippedTestCode = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles";
		//testFE.unzipRecursive(nonzippedTestCode, "Storage/");

		String aSingleJavaFile = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles\\French Main";

		// This set of code (sectionBCode) works
		String sectionBCode = "C:\\Users\\lloydta18\\Downloads\\SectB_OrigCodes.zip";
		//testFE.unzipRecursive(sectionBCode, "Storage\\");
		//testFE.unzipRecursiveWithOneFile(sectionBCode, "Storage\\");
		//testFE.unzipRecursiveWithTempDirs(sectionBCode, "Storage\\");
		//testFE.unzipRecursivewithMods(sectionBCode, "Storage\\");
		
		System.out.println("Trying to access cleanup method");
		testFE.cleanUpFoldersCreated();
		System.out.println("Got to the line past the cleanup method");
		

		System.out.println("\n\nPrinting out files array: ");
		for(int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i).getName());
		}

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
					//Files.createDirectories(fs.getPath(targetDir));
					File storage = new File(targetDir);
					storage.mkdir();
				}

				while (entries.hasMoreElements()) {
					ZipEntry ze = entries.nextElement();
					String tempFileNameForStudent;
					if (ze.isDirectory() && !ze.getName().contains(".settings")
							&& !ze.getName().contains("bin")) {
						System.out.println("making dir: " + targetDir + ze.getName());
						//Files.createDirectories(fs.getPath(targetDir + ze.getName()));
						File newDir = new File(targetDir + ze.getName());
						newDir.mkdir();
						tempFileNameForStudent = newDir.getName();
						//File combinedCode = new File(newDir.getName() + ".txt");
						//combinedCode.createNewFile();
						File combinedCode;
						combinedCode = File.createTempFile(newDir.getName(), null, newDir);
						System.out.println("This is the name of the combinedCode file: " + combinedCode.getName());
						System.out.println("This is the parent of the .txt file: " + combinedCode.getParent());
						// Files.createFile(fs.getPath(targetDir + ze.getName()));
					} else {
						if (ze.getName().endsWith(".zip") || ze.getName().endsWith(".java")) {
							InputStream is = zf.getInputStream(ze);
							BufferedInputStream bis = new BufferedInputStream(is);
							String uncompFileName = "";
							System.out.println("This is the value of targetDir: " + targetDir);
							//System.out.println("This is the zip entry name: " + ze.getName());
							uncompFileName = targetDir + ze.getName();
							Path uncompFilePath = fs.getPath(uncompFileName);
							Path zipFileLoc = Files.createFile(uncompFilePath);
							File fileToWrite = zipFileLoc.toFile();
							File[] filesInParent = fileToWrite.getParentFile().listFiles();
							File compilationFile = null;
							FileOutputStream fileOutput;
							for(File f : filesInParent) {
								if(f.getName().endsWith(".tmp")) {
									compilationFile = new File(f.getName());
									System.out.println("Found a tmp file! " + f.getName());
								} else {
									System.out.println("This isn't a tmp file: " + f.getName());
								}
							}
							System.out.println("File: " + fileToWrite.getName() + " has a parent: " + fileToWrite.getParent());
							if(!compilationFile.equals(null) && fileToWrite.getName().endsWith(".java")) {
								fileOutput = new FileOutputStream(compilationFile, true);
							} else {
								fileOutput = new FileOutputStream(fileToWrite);
							}
							while (bis.available() > 0) {
								fileOutput.write(bis.read());
							}
							fileOutput.close();
							System.out.println("Written: " + ze.getName());
						
							if (ze.getName().endsWith(".zip")) {
								System.out.println("Path generated: " + zipFileLoc);
								unzipRecursive(zipFileLoc.toString(),
										zipFileLoc.toString().substring(0, zipFileLoc.toString().length() - 4));
							}
							
							//System.out.println(zipFileLoc.toFile().getAbsolutePath());

//								if (zipFileLoc.toFile().getAbsolutePath().endsWith(".java")) {
//									files.add(zipFileLoc.toFile());
//								}
							if(!compilationFile.equals(null)) {
								if(files.contains(compilationFile)) {
									files.remove(compilationFile);
								}
								files.add(compilationFile);
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
	}// one file method
		

	
	public void cleanUpFoldersCreated() {
		System.out.println("MADE IT TO CLEANUP METHOD!");
		Path currDir = Paths.get("").toAbsolutePath();
		File directory = currDir.toFile();
		File [] allFilesInDir = directory.listFiles();
		for(File fl : allFilesInDir) {
			System.out.println(fl.toString());
			if(fl.toString().endsWith("Storage")) {
				deleteDir(fl);
				System.out.println("FOUND STORAGE FILE");
			}
			if(fl.toString().endsWith(".tmp")) {
				fl.delete();
				System.out.println("FOUND a tmp FILE: " + fl.toString());
			}
		}
		System.out.println("THIS IS FROM CLEANUP METHOD: " + currDir.toString());
		
	}
	
	//using article from Atta: 
	//https://attacomsian.com/blog/java-delete-directory-recursively#:~:text=Using%20Java%20I%2FO%20Package,-To%20delete%20a&text=listFiles()%20method%20to%20list,delete()%20.
	public void deleteDir(File fileToDelete) {
		File [] filesToDelete = fileToDelete.listFiles();
		if(filesToDelete != null) {
			for(File f : filesToDelete) {
				deleteDir(f);
			}
		}
		fileToDelete.delete();
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
	} //look inside nonzipped without temp files or writing to files array
	
	public void lookInsideNonZippedFolderMoreWork(String PATH, File[] fileArray, String targetDir) {
		FileSystem fs = FileSystems.getDefault();

		try {
			if (Files.notExists(fs.getPath(targetDir))) {
				//Files.createDirectories(fs.getPath(targetDir));
				File storage = new File(targetDir);
				storage.mkdir();
			}

			for (File currFile : fileArray) {
				if (currFile.getName().endsWith(".zip")) {
					unzipRecursive(PATH, targetDir);
				}
				String tempFileNameForStudent;
				if (currFile.isDirectory() && !currFile.getName().contains(".settings")
						&& !currFile.getName().contains("bin")) {
					System.out.println("making dir: " + targetDir + currFile.getName());
					//Files.createDirectories(fs.getPath(targetDir + ze.getName()));
					File newDir = new File(targetDir + currFile.getName());
					newDir.mkdir();
					tempFileNameForStudent = newDir.getName();
					//File combinedCode = new File(newDir.getName() + ".txt");
					//combinedCode.createNewFile();
					File combinedCode;
					combinedCode = File.createTempFile(newDir.getName(), null, newDir);
					System.out.println("This is the name of the combinedCode file: " + combinedCode.getName());
					System.out.println("This is the parent of the .txt file: " + combinedCode.getParent());
					lookInsideNonZippedFolder(newDir.getName(), currFile.listFiles(),
							newDir.getName());
					// Files.createFile(fs.getPath(targetDir + ze.getName()));
				} else {
					InputStream is = new FileInputStream(currFile);
					BufferedInputStream bis = new BufferedInputStream(is);
					String uncompFileName = "";
					System.out.println("This is the value of targetDir: " + targetDir);
					//System.out.println("This is the zip entry name: " + ze.getName());
					uncompFileName = targetDir + File.separator + currFile.getName();
					Path uncompFilePath = fs.getPath(uncompFileName);
//					Path zipFileLoc;
//					if(Files.notExists(uncompFilePath)) {
//						 zipFileLoc = Files.createFile(uncompFilePath);
//					} else {
//						zipFileLoc = uncompFilePath.toFile();
//					}
						
					File fileToWrite = uncompFilePath.toFile();
					File compilationFile = null;
					FileOutputStream fileOutput;
					if(fileToWrite.getParent() != null) {
						File[] filesInParent = fileToWrite.getParentFile().listFiles();
						for(File f : filesInParent) {
							if(f.getName().endsWith(".tmp")) {
								compilationFile = new File(f.getName());
								System.out.println("Found a tmp file! " + f.getName());
							} else {
								System.out.println("This isn't a tmp file: " + f.getName());
							}
						}
						System.out.println("File: " + fileToWrite.getName() + " has a parent: " + fileToWrite.getParent());
					}
					System.out.println("THIS IS IN NONZIPPED METH: " + fileToWrite.getName());
					System.out.println(compilationFile.getName());
					if(!compilationFile.equals(null) && fileToWrite.getName().endsWith(".java")) {
						fileOutput = new FileOutputStream(compilationFile, true);
					} else {
						fileOutput = new FileOutputStream(fileToWrite);
					}
					while (bis.available() > 0) {
						fileOutput.write(bis.read());
					}
					fileOutput.close();
					System.out.println("Written: " + currFile.getName());
				}//else close
			}//for close

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
