import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.IllegalFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private static ArrayList<File> files;

	private static long averageSizeOfFile;
	private static long medianSizeOfFiles;
	private static double standardDeviationOfFiles;
	private static int numOfFiles;
	public ArrayList<File> unprocessedFiles;
	

	/**
	 * Constructor for the Folder Engine object.
	 * Instantiates the arraylists for both processed and unprocessed files.
	 */
	public FolderEngine() {
		files = new ArrayList<File>();
		unprocessedFiles = new ArrayList<File>();
	}	
	

	/**
	 * Recursive unzipping method. Framework is very loosely based on code from a howtodoinjava code.
	 * (URL for howtodoinjava article: https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/)
	 * However, it is very tailored and added on to in order to meet the needs of the CCC.
	 * @param PATH -- the canonical path to the zip file
	 * @param targetDir	-- A string representing the name of the folder in which to store the files gathered
	 */
	public void unzipRecursive(String PATH, String targetDir) {
		//File object for files that can't be processed
		File badFile = null;
		// create a new zip file object with the path passed in
		try (ZipFile zf = new ZipFile(PATH)) {
			// create a file system object
			FileSystem fs = FileSystems.getDefault();

			// get all the entries in the zip file object
			Enumeration<? extends ZipEntry> entries = zf.entries();

			// if the storage folder doesn't exist, make it
			if (Files.notExists(fs.getPath(targetDir))) {
				File storage = new File(targetDir);
				storage.mkdir();
			}
			
			//if this is the first entry,
			//get the median and standard deviation of all the files in the folder
			if(medianSizeOfFiles == 0 && standardDeviationOfFiles == 0) {
				medianSizeOfFiles = getMedianSize(zf.entries());
				standardDeviationOfFiles = getSD(zf.entries());
			}

			// iterate over the entries in the zip folder
			while (entries.hasMoreElements()) {
				try {
					// get next entry
					ZipEntry ze = entries.nextElement();
					//get the size of the file
					long sizeOfCurrentFile = ze.getSize();
					//if the file has a size
					if (sizeOfCurrentFile != 0) {
						//if the size of the file is greater than 3 standard deviations above the median
						//throw it out
						if (sizeOfCurrentFile > (medianSizeOfFiles + (3*standardDeviationOfFiles))) {
							badFile = new File(ze.getName());
							//throw exception
							throw new IOException("File is too large. Cannot process. Skipping.");
						}
					}
					
					// holds the potential unzipped parent of the zip entry that's gotten ignored
					File unzippedParent;

					// check for nonzipped parent folder that's getting ignored
					if (ze.getName().contains("/")) {
						// find the name of the potential parent folder using the index of the slash
						String possibleParentFolder = ze.getName().substring(0, ze.getName().indexOf("/") + 1);
						// if the possible parent dir doesn't exist, create it
						if (Files.notExists(fs.getPath(targetDir + possibleParentFolder))) {
							unzippedParent = new File(targetDir + File.separator + possibleParentFolder);
							unzippedParent.mkdir();
						}
					}
					// if the zip entry is a directory and not a .settings or bin folder
					if (ze.isDirectory() && !ze.getName().contains(".settings") 
							&& !ze.getName().contains("bin")) {
						// create a new directory
						File newDir = new File(targetDir + File.separator + ze.getName());
						newDir.mkdir();
						// find the name for the temp file
						String nameForTempFile = newDir.getAbsolutePath()
								.substring(newDir.getAbsolutePath().indexOf("Storage"));
						// create a temp file in the dir to hold that student's code
						File combinedCode = File.createTempFile(nameForTempFile, null, newDir.getAbsoluteFile());
					} else {
						// if it's a zip folder or a java file
						if (ze.getName().endsWith(".zip") || ze.getName().endsWith(".java")) {
							// get the input stream and make a buffered input stream
							InputStream is = zf.getInputStream(ze);
							BufferedInputStream bis = new BufferedInputStream(is);
							// get the name of the file
							String uncompFileName = targetDir + File.separator + ze.getName();
							
							// get the path of the file name
							Path uncompFilePath = fs.getPath(uncompFileName);
							//create a variable to hold path to the zip file
							Path zipFileLoc;
							// if the file associated with that path doesn't exist, create it
							if (Files.notExists(uncompFilePath)) {
								zipFileLoc = Files.createFile(uncompFilePath);
							} else {
								zipFileLoc = uncompFilePath;
							}
							// get the file that needs to be written
							File fileToWrite = zipFileLoc.toFile();
							
							// this section solves the problem of only writing to a single temporary file
							// for every student
							// even if their java files are multiple layers in or in a src folder
							int firstIndexOfSlash;
							int secondIndexOfSlash;
							//default index is the length of the path
							int indexToUse = fileToWrite.getAbsolutePath().length();
							// get the index of the storage folder in the path
							int indexOfStorage = fileToWrite.getAbsolutePath().indexOf("Storage");
							// if there is a slash beyond the storage folder
							if (fileToWrite.getAbsolutePath().substring(indexOfStorage).contains("\\")) {						
								// get the index of the first slash after the storage folder
								firstIndexOfSlash = fileToWrite.getAbsolutePath().indexOf("\\", indexOfStorage);
								
								// if after the storage folder slash there is a second slash, get that one too
								if (fileToWrite.getAbsolutePath()
										.substring(firstIndexOfSlash + 1, fileToWrite.getAbsolutePath().length())
										.contains("\\")) {
									secondIndexOfSlash = fileToWrite.getAbsolutePath().indexOf("\\",
											firstIndexOfSlash + 1);
									
									// the index of the second slash will be used later 
									// to find the head folder of the file
									indexToUse = secondIndexOfSlash;
								}
							}
							
							// get the name of the head folder using the storage index and the second index
							// gathered
							String headFolderName = fileToWrite.getAbsolutePath()
									.substring(fileToWrite.getAbsolutePath().indexOf("Storage"), indexToUse);
														
							// create a head folder file object
							File headFolder = new File(fileToWrite.getAbsolutePath()
									.substring(fileToWrite.getAbsolutePath().indexOf("Storage"), indexToUse));
														
							// find all the files in that head folder
							File[] filesInParent = headFolder.listFiles();
							
							// default initialize the compilation file with the file that's being written
							File compilationFile = fileToWrite;
							// create an output stream
							FileOutputStream fileOutput;
							// iterate over files in the head folder
							if (filesInParent.length > 0) {
								for (File f : filesInParent) {
									// if a temp file is found, use that as the compilation file
									// for all the student's java files
									if (f.getName().endsWith(".tmp")) {
										compilationFile = new File(f.getName());
									} 
								}
							}

							// if the file is a zip folder, don't write it to the compilation file
							if (fileToWrite.getName().endsWith(".zip")) {
								fileOutput = new FileOutputStream(fileToWrite);
							} else {
								//otherwise, append the content to the compliation file
								fileOutput = new FileOutputStream(compilationFile, true);
							}
							// write the file
							while (bis.available() > 0) {
								fileOutput.write(bis.read());
							}

							//close all output and input streams
							fileOutput.close(); // close the output stream
							bis.close(); //close the buffered input stream
							is.close(); //close the input stream
							
							// if the file is a zip folder, make a recursive call
							if (ze.getName().endsWith(".zip")) {
								unzipRecursive(zipFileLoc.toString(),
										zipFileLoc.toString().substring(0, zipFileLoc.toString().length() - 4));
							}

							// if the compilation file contains stuff, add it to the files array
							if (!compilationFile.equals(null)) {
								// if it's already in the files array, remove it and re-add it
								if (files.contains(compilationFile)) {
									files.remove(compilationFile);
								}
								files.add(compilationFile);
							}
						} // close of if statement for zip/java
						else if(ze.getName().endsWith(".txt")) {
							badFile = new File(ze.getName());
							//throw exception
							throw new IOException("Cannot process .txt files");
						}
					} // close of else for directory
				} // close try
				catch(IOException ioe) {
					System.out.println(ioe.getMessage());
					ioe.printStackTrace();
					//add unprocessed file to array
					if (!unprocessedFiles.contains(badFile)) {
						unprocessedFiles.add(badFile);
					}
				}
				catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					//add unprocessed file to array
					if (!unprocessedFiles.contains(badFile)) {
						unprocessedFiles.add(badFile);
					}
				}
			} // close of while more entries loop
		}// catch any exceptions
		catch (FileAlreadyExistsException faee) {
			faee.printStackTrace();
			if (!unprocessedFiles.contains(badFile)) {
				unprocessedFiles.add(badFile);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			if (!unprocessedFiles.contains(badFile)) {
				unprocessedFiles.add(badFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (!unprocessedFiles.contains(badFile)) {
				unprocessedFiles.add(badFile);
			}
		} 
	}//zipped folder method
	
	/**
	 * Method to get the median size of all the files in a zip file.
	 * @param zipEntries -- Enumeration of zip entries
	 * @return median of the entries
	 */
	public long getMedianSize(Enumeration<? extends ZipEntry> zipEntries) {
		//create an array list of the zip entries
		ArrayList<ZipEntry> zipE = new ArrayList<ZipEntry>();
		int numOfEntries;	//total number of entries
		int midpointEntry1;	//middle or first of the 2 in the middle
		int midpointEntry2;	//second middle entry (for data sets with an even number of entries)
		long avgOfMids;		//average of the two middle entries (for data sets with even # of entries)
		
		//iterate over the zip entries
		while (zipEntries.hasMoreElements()) {
			try {
				// get next entry and add it to the array list
				ZipEntry ze = zipEntries.nextElement();
				zipE.add(ze);

			}//try
			catch(Exception e) {
				e.printStackTrace();
			}
		}//while
		
		//get the number of entries
		numOfEntries = zipE.size();
		//get the first middle entry
		midpointEntry1 = numOfEntries / 2;
		//if it's an even sized data set
		if(numOfEntries % 2 == 0) {
			//get the other entry in the middle
			midpointEntry2 = (numOfEntries / 2) - 1;
		} else {
			//otherwise set the second midpoint to 0
			midpointEntry2 = 0;
		}
		
		//if there are two middle entries, average them
		if(midpointEntry2 != 0) {
			avgOfMids = zipE.get(midpointEntry1).getSize() + zipE.get(midpointEntry2).getSize();
			avgOfMids /= 2;			
		} else {
			//otherwise just use the midpoint as the median
			avgOfMids = zipE.get(midpointEntry1).getSize();
		}
		
		//return the median
		return avgOfMids;	
	}//getMedSize
	
	
	/**
	 * Method to get the standard deviation of all the files in a zip file.
	 * @param zipEntries -- Enumeration of zip entries
	 * @return standard deviation of the entries
	 */
	public double getSD(Enumeration<? extends ZipEntry> zipEntries) {
		//array list to hold zip entries
		ArrayList<ZipEntry> zipE = new ArrayList<ZipEntry>();
		//array list to hold calculated numbers midway through the SD calculation
		ArrayList<Integer> zipEMidCalc = new ArrayList<Integer>();
		long avg = 0;	//initialize average size of files
		int numFiles = 0;	//initialize number of files
		long sizeOfCurrentFile;	//size of the current file
		double standardDev;	//standard deviation
		
		//iterate over the zip entries
		while (zipEntries.hasMoreElements()) {
			File currFile = null;
			try {
				// get next entry
				ZipEntry ze = zipEntries.nextElement();
				zipE.add(ze);	//add it to the arraylist of zip entries
				
				//initialize the current file being processed
				currFile = new File(ze.getName());
				
				//get the size of the current file
				sizeOfCurrentFile = ze.getSize();
				
				//if the current file has a size
				if (sizeOfCurrentFile != 0) {					
					//keep track of the current average size of files
					avg *= numFiles;
					avg += sizeOfCurrentFile;
					numFiles++;
					avg /= numFiles;				
				}//if
			}//try
			catch(Exception e) {
				//catch any errors and add to the unprocessed files array
				e.printStackTrace();
				if (!unprocessedFiles.contains(currFile)) {
					unprocessedFiles.add(currFile);
				}
			}
		}//while
		
		//for every zip entry,
		//square its difference with the mean
		//store results in the "middle of calculations" array
		for(int i = 0; i < zipE.size(); i++) {
			zipEMidCalc.add(i, (int)Math.pow((double)(avg - zipE.get(i).getSize()), 2));
		}
		
		//reset avg and numfiles
		avg = 0;
		numFiles = 0;
		
		//iterate over the halfway calculated numbers
		for(int i = 0; i < zipEMidCalc.size(); i++) {
			//calculate the average of the calculated numbers
			sizeOfCurrentFile = zipEMidCalc.get(i);
			if (sizeOfCurrentFile != 0) {
				avg *= numFiles;
				avg += sizeOfCurrentFile;
				numFiles++;
				avg /= numFiles;				
			}//if
		}
		
		//take the square root of the average of the calculated numbers to get the SD
		standardDev = Math.sqrt(avg);
		//return the standard deviation
		return standardDev;
		
	}//getStandardDev

	/**
	 * Clean up method to delete the folders created during runtime.
	 */
	public void cleanUpFoldersCreated() {		
		//get the current directory
		Path currDir = Paths.get("").toAbsolutePath();
		//convert the current directory path to a file
		File directory = currDir.toFile();
		//get a list of all the files in the current directory
		File[] allFilesInDir = directory.listFiles();
		
		//iterate over all the files in the current directory
		for (File fl : allFilesInDir) {
			//if the file is the storage folder
			if (fl.toString().endsWith("Storage")) {				
				//call the delete method on the storage folder
				deleteDir(fl);				
			}
			//if the file is a temporary file
			if (fl.toString().endsWith(".tmp")) {
				//delete the temporary file
				fl.delete();				
			}
		}
	}//cleanUpFoldersCreated method
	
	
	/**
	 * Method that clears the storage folder in between zip uploads.
	 * This allows students with the same name that appear in two folders 
	 * to be treated as two separate students.
	 */
	public void cleanUpStorageFolder() {		
		//get the current directory
		Path currDir = Paths.get("").toAbsolutePath();
		//convert the current directory path to a file
		File directory = currDir.toFile();
		//get a list of all the files in the current directory
		File[] allFilesInDir = directory.listFiles();
		
		//iterate over all the files in the current directory
		for (File fl : allFilesInDir) {			
			//find the storage folder
			if (fl.toString().endsWith("Storage")) {
				//delete all the files in the storage folder
				deleteDir(fl);				
			}
		}
		
		//remake the storage folder
		FileSystem fs = FileSystems.getDefault();
		if (Files.notExists(fs.getPath("Storage\\"))) {
			File storage = new File("Storage\\");
			storage.mkdir();
		}
	}//cleanup storage folder method

	/**
	 * Recursive helper method for the cleanup methods. 
	 * Empties a directory by deleting its contents, 
	 * and then deletes the directory itself.
	 * Code is based on an article from Atta.
	 * (URL for Atta article: https://attacomsian.com/blog/java-delete-directory-recursively#:~:text=Using%20Java%20I%2FO%20Package,-To%20delete%20a&text=listFiles()%20method%20to%20list,delete()%20.)
	 * @param fileToDelete -- File object of the directory to delete
	 */
	public void deleteDir(File fileToDelete) {
		//get the contents of the file/directory
		File[] filesToDelete = fileToDelete.listFiles();
		
		//if the file is a directory and has contents
		if (filesToDelete != null) {
			//for each file
			for (File f : filesToDelete) {				
				//recursively call the method on the contents
				deleteDir(f);
			}
		}
		//delete the file or directory after it's empty
		fileToDelete.delete();
	}//deleteDir method


	/**
	 * @see https://www.geeksforgeeks.org/java-program-to-read-content-from-one-file-and-write-it-into-another-file/
	 * @param f
	 */
	public void uploadJavaFile(File f, String targetDir) {
		try {

			FileSystem fs = FileSystems.getDefault();

			if (Files.notExists(fs.getPath(targetDir))) {
				File storage = new File(targetDir);
				storage.mkdir();
			}

			FileReader reader = new FileReader(f.getCanonicalPath());
			String contents = "";
			int buffer;
			while ((buffer = reader.read()) != -1) {
				contents += (char) buffer;
			}

			FileWriter writer = new FileWriter("Storage\\" + f.getName());
			writer.write(contents);

			reader.close();
			writer.flush();
			writer.close();

			files.add(f);
		} catch (IOException e) {
			if (!unprocessedFiles.contains(f)) {
				unprocessedFiles.add(f);
			}
			e.printStackTrace();
		} 

	}

	/**
	 * Method to handle the upload of a nonzipped folder.
	 * @param filePath -- the canonical path to the zip file
	 * @param targetDir -- A string representing the name of the folder in which to store the files gathered
	 */
	//method to handle an unzipped folder
	public void uploadRegularFolder(String filePath, String targetDir) {
		//create a new file based on the path given in
		File f = new File(filePath);
			
		try {
			//list all the contents of the folder
			File[] filesInRegFolder = f.listFiles();
					
			//iterate over the contents of the folder
			for(File fInRegFolder : filesInRegFolder) {
				//if it doesn't start with . or is a bin file, process it 
				if(!fInRegFolder.getName().startsWith(".") && !fInRegFolder.getName().equals("bin")) {
					//send zip folders to the recursive unzip method
					if(fInRegFolder.getName().endsWith(".zip")) {
						try {
							unzipRecursive(fInRegFolder.getCanonicalPath(), "Storage\\");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					//send java files to the method handling single java files
					else if(fInRegFolder.getName().endsWith(".java")) {
						uploadJavaFile(fInRegFolder, "Storage\\");
					} 
					//make a recursive call for all other folders
					else {
						try {
							uploadRegularFolder(fInRegFolder.getCanonicalPath(), "Storage\\");
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
			}
		} catch (Exception e) {
			//catch and report any errors
			if (!unprocessedFiles.contains(f)) {
				unprocessedFiles.add(f);
			}
			e.printStackTrace();
		}
	}//regular folder method
	

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
	 * Clears files for new set of data
	 */
	public void clearFiles() {
		files.clear();
	}

	/**
	 * Gets the files that weren't processed due to errors
	 * A common error is that the file is too large
	 * Other errors might include that the file doesn't contain a .java file
	 * @return unprocessed file arraylist
	 */
	ArrayList<File> getUnprocessedFiles() {
		return unprocessedFiles;
	}

}