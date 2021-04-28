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

	private static long averageSizeOfFile;
	private static int numOfFiles;
	public ArrayList<File> unprocessedFiles;

	public FolderEngine() {
		files = new ArrayList<File>();
		unprocessedFiles = new ArrayList<File>();
		averageSizeOfFile = 2500;
		numOfFiles = 1;

	}
	
	//TODO: Make it work with a zip folder that has a nonzipped folder directly inside (testCodeFromDesktop)
	// TODO: make it work with a non zipped folder
	public static void main(String[] args) {
		FolderEngine testFE = new FolderEngine();
		// This set of test code is the one that doesn't work
		String testCodeFromDesktop = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles.zip";
		//testFE.unzipRecursive(testCodeFromDesktop, "Storage/");

		//Still gets stuck on Tyler Ridout's folder, even though it skips files
		String testCodeFromVal = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCC Test Code Folders from Valentine\\SectA_stupidCopies.zip";
		try {
			testFE.unzipRecursive(testCodeFromVal, "Storage/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// This set of nonzipped test code doesn't work
		String nonzippedTestCode = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles";
		//testFE.unzipRecursive(nonzippedTestCode, "Storage/");

		String aSingleJavaFile = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles\\French Main";

		// This set of code (sectionBCode) works
		String sectionBCode = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCC Test Code Folders from Valentine\\SectB_OrigCodes.zip";
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
	
	// Loosely based on howtodoinjava article code:
	// https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
	public void unzipRecursive(String PATH, String targetDir) throws IOException {
		//check to see if folder passed in is a zip folder
		if (PATH.endsWith(".zip")) {
			//create a new zip file object with the path passed in
			try (ZipFile zf = new ZipFile(PATH)) {
				//create a file system object
				FileSystem fs = FileSystems.getDefault();
				
				//get all the entries in the zip file object
				Enumeration<? extends ZipEntry> entries = zf.entries();

				//if the storage folder doesn't exist, make it
				if (Files.notExists(fs.getPath(targetDir))) {
					File storage = new File(targetDir);
					storage.mkdir();
				}

				//iterate over the entries in the zip folder
				while (entries.hasMoreElements()) {
					try {
						//get next entry
						ZipEntry ze = entries.nextElement();
						long sizeOfCurrentFile = ze.getSize();
						if(sizeOfCurrentFile != 0) {
							if(sizeOfCurrentFile < (4*averageSizeOfFile)) {
								System.out.println("Size of: " + ze.getName() + " file: " + sizeOfCurrentFile);
								averageSizeOfFile *= numOfFiles;
								averageSizeOfFile += sizeOfCurrentFile;
								numOfFiles++;
								averageSizeOfFile /= numOfFiles;
								System.out.println("Average size so far: " + averageSizeOfFile);
							} else {
								System.out.println("File is too large");
								System.out.println("Size of file: " + sizeOfCurrentFile);
								throw new IOException("File is too large. Cannot process. Skipping.");
							}
							
						}
						//holds the potential unzipped parent of the zip entry that's gotten ignored
						File unzippedParent;
						
						//check for nonzipped parent folder that's getting ignored
						if(ze.getName().contains("/")) {
							//find the name of the potential parent folder using the index of the slash
							String possibleParentFolder = ze.getName().substring(0, ze.getName().indexOf("/") + 1);
							//if the possible parent dir doesn't exist, create it
							if (Files.notExists(fs.getPath(targetDir + possibleParentFolder))) {
								unzippedParent = new File(targetDir + File.separator + possibleParentFolder);
								unzippedParent.mkdir();
								//System.out.println("Path of parent folder made: " + unzippedParent.getAbsolutePath());
							}
							//System.out.println("Possible parent: " + possibleParentFolder);					
						}
						//if the zip entry is a directory and not a .settings or bin folder
						if (ze.isDirectory() && !ze.getName().contains(".settings")
								&& !ze.getName().contains("bin")) {
							//create a new directory
							File newDir = new File(targetDir + File.separator + ze.getName());
							newDir.mkdir();
							//System.out.println("making dir: " + newDir.getName());
							//find the name for the temp file
							String nameForTempFile = newDir.getAbsolutePath().substring(newDir.getAbsolutePath().indexOf("Storage"));
							//create a temp file in the dir to hold that student's code
							File combinedCode;
							combinedCode = File.createTempFile(nameForTempFile, null, newDir.getAbsoluteFile());
							//System.out.println("This is the name of the combinedCode file: " + combinedCode.getName());
							//System.out.println("This is the parent of the .txt file: " + combinedCode.getParent());
						} else {
							//if it's a zip folder or a java file
							if (ze.getName().endsWith(".zip") || ze.getName().endsWith(".java")) {
								//get the input stream and make a buffered input stream
								InputStream is = zf.getInputStream(ze);
								BufferedInputStream bis = new BufferedInputStream(is);
								//get the name of the file
								String uncompFileName = "";
								//System.out.println("This is the value of targetDir: " + targetDir);
								//System.out.println("This is the zip entry name: " + ze.getName());
								uncompFileName = targetDir + File.separator + ze.getName();
								//get the path of the file name
								Path uncompFilePath = fs.getPath(uncompFileName);
								Path zipFileLoc;
								//if the file associated with that path doesn't exist, create it
								if(Files.notExists(uncompFilePath)) {
									zipFileLoc = Files.createFile(uncompFilePath);
								} else {
									zipFileLoc = uncompFilePath;
								}
								//get the file that needs to be written
								File fileToWrite = zipFileLoc.toFile();
								//System.out.println("This is the fileToWrite name: " + fileToWrite.getName());
								
								//this section solves the problem of only writing to a single temporary file for every student
								//even if their java files are multiple layers in or in a src folder
								int firstIndexOfSlash;
								int secondIndexOfSlash;
								//default index is the length of the path
								int indexToUse = fileToWrite.getAbsolutePath().length();
								//get the index of the storage folder in the path
								int indexOfStorage = fileToWrite.getAbsolutePath().indexOf("Storage");
								//if there is a slash beyond the storage folder
								if(fileToWrite.getAbsolutePath().substring(indexOfStorage).contains("\\")) {
									
									//System.out.println("Made it inside slash if statement");
									//System.out.println("FileToWrite before getting first slash: " + fileToWrite.getAbsolutePath());
									//get the index of the first slash after the storage folder
									firstIndexOfSlash = fileToWrite.getAbsolutePath().indexOf("\\", indexOfStorage);
									//System.out.println("FileToWrite after getting first slash: " + fileToWrite.getAbsolutePath());
									//System.out.println("FirstIndexOfSlash value: " + firstIndexOfSlash);
									//if after the storage folder slash there is a second slash, get that one too
									if(fileToWrite.getAbsolutePath().substring(firstIndexOfSlash + 1, fileToWrite.getAbsolutePath().length()).contains("\\")) {
										//System.out.println("FileToWrite before getting second slash: " + fileToWrite.getAbsolutePath());
										secondIndexOfSlash = fileToWrite.getAbsolutePath().indexOf("\\", firstIndexOfSlash+1);
										//System.out.println("SecondIndexOfSlash value: " + secondIndexOfSlash);
										//System.out.println("FileToWrite after getting second slash: " + fileToWrite.getAbsolutePath());
										//the index of the second slash will be used later to find the head folder of the file
										indexToUse = secondIndexOfSlash;
										//System.out.println("IndexToUse value: " + indexToUse);
									}
								}
								
								//System.out.println("Index of storage: " + fileToWrite.getAbsolutePath().indexOf("Storage"));
								//System.out.println("Index to use: " + indexToUse);
								//System.out.println("size of FileToWrite path: " + fileToWrite.getAbsolutePath().length());
								//System.out.println("file path of FileToWrite: " + fileToWrite.getAbsolutePath());
								//get the name of the head folder using the storage index and the second index gathered
								String headFolderName = fileToWrite.getAbsolutePath().substring(
										fileToWrite.getAbsolutePath().indexOf("Storage"), 
										indexToUse);
								//System.out.println(headFolderName);
								//create a head folder file object
								File headFolder = new File(fileToWrite.getAbsolutePath().substring(
										fileToWrite.getAbsolutePath().indexOf("Storage"), 
										indexToUse));
								//System.out.println("Head folder: " + headFolder.getAbsolutePath());
								//find all the files in that head folder
								File[] filesInParent = headFolder.listFiles();
								//default initialize the compilation file with the file that's being written
								File compilationFile = fileToWrite;
								FileOutputStream fileOutput;
								//iterate over files in the head folder
								if(filesInParent.length > 0) {
									for(File f : filesInParent) {
										//if a temp file is found, use that as the compilation file 
										//for all the student's java files
										if(f.getName().endsWith(".tmp")) {
											compilationFile = new File(f.getName());
											//System.out.println("Found a tmp file! " + f.getName());
										} else {
											//System.out.println("This isn't a tmp file: " + f.getName());
										}
									}
								}
								//System.out.println("File: " + fileToWrite.getName() + " has a parent: " + fileToWrite.getParent());
	
								//if the file is a zip folder, don't write it to the compilation file
								if(fileToWrite.getName().endsWith(".zip")) {
									fileOutput = new FileOutputStream(fileToWrite);
								} else {
									fileOutput = new FileOutputStream(compilationFile, true);
								}
								//write the file
								while (bis.available() > 0) {
									//System.out.println("writing from " + fileToWrite.getName());
									fileOutput.write(bis.read());
								}
								
								fileOutput.close();	//close the output stream
								//System.out.println("Written: " + ze.getName());
							
								//if the file is a zip folder, make a recursive call
								if (ze.getName().endsWith(".zip")) {
									//System.out.println("Path generated: " + zipFileLoc);
									unzipRecursive(zipFileLoc.toString(),
											zipFileLoc.toString().substring(0, zipFileLoc.toString().length() - 4));
								}
								
								//System.out.println(zipFileLoc.toFile().getAbsolutePath());
	
								//if the compilation file contains stuff, add it to the files array
								if(!compilationFile.equals(null)) {
									//if it's already in the files array, remove it and re-add it
									if(files.contains(compilationFile)) {
										files.remove(compilationFile);
									}
									files.add(compilationFile);
								}
							}//close of if statement for zip/java
						}//close of else for directory
	//					catch(IOException fileTooLarge) {
	//						System.out.println(fileTooLarge.getMessage());
	//					}
					}//close try
					catch (Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
				}//close of while more entries loop
			//catch any exceptions
			} catch (FileAlreadyExistsException faee) {
				faee.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}//if it's not a zip file, use the nonzippedfolder method 
		else {
			File nonZippedFile = new File(PATH);
			File[] arrayOfFiles = { nonZippedFile };
			lookInsideNonZippedFolderMoreWork(PATH, arrayOfFiles, targetDir);
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
				File[] filesInStorage = fl.listFiles();
				for(File fis : filesInStorage) {
					fis.delete();
				}
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
		File currFileName = null;

		try {
			if (Files.notExists(fs.getPath(targetDir))) {
				Files.createDirectory(fs.getPath(targetDir));
			}
		
			for (File currFile : fileArray) {
				currFileName = currFile;
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
			if(!unprocessedFiles.contains(currFileName)) {
				unprocessedFiles.add(currFileName);
			}
		}
	} //look inside nonzipped without temp files or writing to files array
	
	public void lookInsideNonZippedFolderMoreWork(String PATH, File[] fileArray, String targetDir) {
		FileSystem fs = FileSystems.getDefault();
		File currFileName = null;

		try {
			if (Files.notExists(fs.getPath(targetDir))) {
				//Files.createDirectories(fs.getPath(targetDir));
				File storage = new File(targetDir);
				storage.mkdir();
			}

			for (File currFile : fileArray) {
				currFileName = currFile;
				if (currFile.getName().endsWith(".zip")) {
					unzipRecursive(PATH, targetDir);
				}
				//String tempFileNameForStudent;
				if (currFile.isDirectory() && !currFile.getName().contains(".settings")
						&& !currFile.getName().contains("bin")) {
					System.out.println("making dir in nonzipped meth: " + targetDir + currFile.getName());
					//Files.createDirectories(fs.getPath(targetDir + ze.getName()));
					File newDir = new File(targetDir + File.separator + currFile.getName());
					newDir.mkdir();
					//tempFileNameForStudent = newDir.getName();
					//File combinedCode = new File(newDir.getName() + ".txt");
					//combinedCode.createNewFile();
					File combinedCode;
					combinedCode = File.createTempFile(newDir.getName(), null, newDir);
					System.out.println("This is the name of the combinedCode file: " + combinedCode.getName());
					System.out.println("This is the parent of the .txt file: " + combinedCode.getParent());
					lookInsideNonZippedFolderMoreWork(newDir.getName(), currFile.listFiles(),
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
					System.out.println("This is the uncompFilePath: " + uncompFilePath.toString());
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
						if(compilationFile == null) {
							if(fileToWrite.getParentFile().getParentFile() != null) {
								File grandparent = fileToWrite.getParentFile().getParentFile();
								File[] filesInGParent = grandparent.listFiles();
								for(File f : filesInGParent) {
									if(f.getName().endsWith(".tmp")) {
										compilationFile = new File(f.getName());
										System.out.println("Found a tmp file! " + f.getName());
									} else {
										System.out.println("This isn't a tmp file: " + f.getName());
									}
								}
							}
						}
						System.out.println("File: " + fileToWrite.getName() + " has a parent: " + fileToWrite.getParent());
					}
					System.out.println("THIS IS IN NONZIPPED METH: " + fileToWrite.getName());
					//System.out.println(compilationFile.getName());
					if(!(compilationFile == null) && fileToWrite.getName().endsWith(".java")) {
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
			if(!unprocessedFiles.contains(currFileName)) {
				unprocessedFiles.add(currFileName);
			}
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
	
	/**
	 * Clears files for new set of data
	 */
	public void clearFiles() {
		files.clear();
	}

	ArrayList<File> getUnprocessedFiles(){
		return unprocessedFiles;
	}
	
}