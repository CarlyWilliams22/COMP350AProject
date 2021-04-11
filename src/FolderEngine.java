import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FolderEngine {

	private ArrayList<File> files;

	public FolderEngine() {
		files = new ArrayList<File>();

	}
	
	//TODO: Make it work with a zip folder that has a nonzipped folder directly inside (testCodeFromDesktop)
	public static void main(String[] args) {
		FolderEngine testFE = new FolderEngine();
		String testCodeFromDesktop = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles.zip";
		testFE.unzipThirdTry(testCodeFromDesktop, "Storage/");
		String testCodeFromVal = "C:\\Users\\lloydta18\\Downloads\\SectA_stupidCopies.zip";
		String nonzippedTestCode = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles";
		//testFE.unzipThirdTry(nonzippedTestCode, "Storage/");
		String aSingleJavaFile = "C:\\Users\\lloydta18\\OneDrive - Grove City College\\Desktop\\CCCTestCodeFiles\\French Main";
		String sectionBCode = "C:\\Users\\lloydta18\\Downloads\\SectB_OrigCodes.zip";
		//testFE.unzipThirdTry(sectionBCode, "Storage/");
		//testFE.unzipThirdTry("C:\\Users\\lloydta18\\git\\COMP350Project\\COMP350AProject\\Storage");
		System.out.println("<<NORMAL TERMINATION>>");
	}
	
	//Based on howtodoinjava article code: https://howtodoinjava.com/java/io/unzip-file-with-subdirectories/
	public void unzipThirdTry(String PATH, String targetDir) {
		if(PATH.endsWith(".zip")) {
			try(ZipFile zf = new ZipFile(PATH)) {
				FileSystem fs = FileSystems.getDefault();
				Enumeration<? extends ZipEntry> entries = zf.entries();
				
				//String targetDir = "Storage/";
				if(Files.notExists(fs.getPath(targetDir))) {
					Files.createDirectory(fs.getPath(targetDir));
				}
				
				
				while(entries.hasMoreElements()) {
					ZipEntry ze = entries.nextElement();
					//System.out.println("This is a zip entry: " + ze.toString());
					if(ze.isDirectory()) {
						System.out.println("making dir: " + targetDir + ze.getName());
						Files.createDirectories(fs.getPath(targetDir + ze.getName()));
					}
	//				} else if(ze.getName().endsWith(".zip")) {
	//					System.out.println("This is a zip file\n");
	//	
	//					System.out.println("Another try: " + PATH + File.separator + ze.getName());
	//				
	//					
	////					try(ZipFile zipf = new ZipFile(PATH+ "/" + ze.getName())) {
	////						FileSystem files = FileSystems.getDefault();
	////						Enumeration<? extends ZipEntry> ent = zipf.entries();
	////						while(ent.hasMoreElements()) {
	////							ZipEntry zipe = entries.nextElement();
	////							System.out.println("This is a zip entry: " + zipe.toString());
	////						}
	////					}
	//					//unzipThirdTry(PATH + File.separator + ze.getName());
	//				}
					else {
						InputStream is = zf.getInputStream(ze);
						BufferedInputStream bis = new BufferedInputStream(is);
						String uncompFileName = targetDir + ze.getName();
						Path uncompFilePath = fs.getPath(uncompFileName);
						Path zipFileLoc = Files.createFile(uncompFilePath);
						FileOutputStream fileOutput = new FileOutputStream(uncompFileName);
						while(bis.available() > 0) {
							fileOutput.write(bis.read());
	//						if(ze.getName().contains("Ridout")) {
	//							System.out.println("Still writing...");
	//						}
						}
						if(ze.getName().endsWith(".zip")) {
							System.out.println("Path generated: " + zipFileLoc);
							unzipThirdTry(zipFileLoc.toString(), zipFileLoc.toString().substring(0, zipFileLoc.toString().length() - 4));
							//System.out.println(ze.getName() + " is a zip");
						}
						fileOutput.close();
						System.out.println("Written: " + ze.getName());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//CALL a method to go inside a non zipped folder??
			File nonZippedFile = new File(PATH);
			File[] files = {nonZippedFile};
			lookInsideNonZippedFolder(PATH, files, targetDir);
		}
	}//3rd try
	
	public void lookInsideNonZippedFolder(String PATH, File[] files, String targetDir) {
		FileSystem fs = FileSystems.getDefault();
		
		//File nonZippedFile = new File(pathOfNZF);
		try {
			//String targetDir = "Storage/";
			if(Files.notExists(fs.getPath(targetDir))) {
				Files.createDirectory(fs.getPath(targetDir));
			}
			
			for(File currFile : files) {
				if(currFile.getName().endsWith(".zip")) {
					unzipThirdTry(PATH, targetDir);
				}
				if(currFile.isDirectory()) {
					System.out.println("making dir: " + targetDir + currFile.getName());
					Files.createDirectories(fs.getPath(targetDir + currFile.getName()));
					lookInsideNonZippedFolder(targetDir + currFile.getName(), currFile.listFiles(), targetDir + currFile.getName());
				} else {
					InputStream is = new FileInputStream(currFile);
					BufferedInputStream bis = new BufferedInputStream(is);
					//String uncompFileName = targetDir + currFile.getName();
					Path currFilePath = fs.getPath(targetDir + currFile.getName());
					Path unzipppedFileLoc = Files.createFile(currFilePath);
					FileOutputStream fileOutput = new FileOutputStream(currFile);
					while(bis.available() > 0) {
						fileOutput.write(bis.read());
					}
					fileOutput.close();
					System.out.println("Written: " + currFile.getName());
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void recursiveUnzip(String pathGiven, String targetLoc) {
		if(!(Files.exists(Paths.get(targetLoc)))) {
			try {
				Files.createDirectories(Paths.get(targetLoc));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try(ZipInputStream zis = new ZipInputStream(new FileInputStream(pathGiven))) {
			ZipEntry ze = zis.getNextEntry();
			while(ze != null) {
				System.out.println(ze.getName());
				Path filePath = Paths.get(targetLoc, ze.getName());
				System.out.println(filePath);
				if(!ze.isDirectory()) {
					System.out.println("Not a directory\n");
					if(ze.getName().endsWith(".zip")) {
						System.out.println(ze.getName() + " is a zip file\n");
						//recursizeUnzip(pathGiven)
					}
					//unzipFiles(zis, filePath);
				} else {
					System.out.println("Must be a directory\n");
					//Files.createDirectories(filePath);
				}
				
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//recursize unzip method
	
	
	public void unzipFiles(ZipInputStream zis, Path targetLocPath) throws IOException {
		
		try(BufferedOutputStream buffOutStream = new BufferedOutputStream(new FileOutputStream(targetLocPath.toAbsolutePath().toString()))) {
			byte[] buff = new byte[1024];
			int amountRead = 0;
			while((amountRead = zis.read(buff)) != -1) {
				buffOutStream.write(buff, 0, amountRead);
				System.out.println(amountRead);
			}
		}
	}
	
	/**
	 * Unzips a zip file of files recursively
	 * 
	 * @param PATH - zip file path
	 */
	public void unzipRecursively(String PATH) {
		File storageDir = new File("Storage\\");
		byte[] buffer = new byte[1024];
		try {
			ZipInputStream zipIS = new ZipInputStream(new FileInputStream(PATH));
			ZipEntry currEntry = zipIS.getNextEntry();
			while(currEntry != null) {
				File currFile = createNewFile(storageDir, currEntry);
				if(currEntry.isDirectory()) {
					System.out.println(currEntry.getName() + " was flagged as a directory");
					if(!currFile.isDirectory() && !currFile.mkdirs()) {
						throw new IOException("Could not create directory: " + currFile);
					} else {
						System.out.println("I guess a new directory was made");
					}
				}
				if(currEntry.getName().endsWith(".zip")) {
					System.out.println("Path: " + PATH + "/" + currEntry.getName());
					unzipRecursively(PATH + "/" + currEntry.getName());
					//System.out.println("Path : " + currFile.getCanonicalPath().toString() + "\n");
					//unzipRecursively(currFile.getCanonicalPath());
					System.out.println("Based on ending, " + currEntry.getName() + " was flagged as a zip");
				}
				//System.out.println("This is what the get name returns: " + currEntry.getName() + "\n");
				System.out.println(currFile.getName().toString() + "\n");
				currEntry = zipIS.getNextEntry();
			}
			zipIS.closeEntry();
			zipIS.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if(determineFileType(PATH).equals("application/x-zip-compressed")) {
			
			
		} 
		
	}//recursive unzip
	
	public File createNewFile(File targetDir, ZipEntry zipEntry) throws IOException {
		File createdFile = new File(targetDir, zipEntry.getName());
		
		String targetDirPath = targetDir.getCanonicalPath();
		String createdFilePath = createdFile.getCanonicalPath();
		
		//guards against zip slip vulnerability
		if(!createdFilePath.startsWith(targetDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}
		
		return createdFile;
	}
	
	/**
	 * Determines the type of a file
	 * 
	 * @param PATH - zip file path
	 */
	public String determineFileType(String PATH) {
		String contentType = null;
		try {
			contentType = Files.probeContentType(Paths.get(PATH));
			System.out.println(contentType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentType;
	}//determine file type

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
