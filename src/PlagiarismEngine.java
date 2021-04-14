import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class PlagiarismEngine {

	private ArrayList<File> files; // projects to process
	private ArrayList<Student> students;

	// Keywords taken from the Wikipedia article List of Java keywords Link:
	// https://en.wikipedia.org/wiki/List_of_Java_keywords
	private static String primitiveTypeKeywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
			"char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
			"finally", "float", "for", "goto", "if", "implements", "import", "instanceof", " int", "interface", "long",
			"native", "new", "non-sealed", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while", "true", "false", "null" };
	private static String commonNonprimitiveTypeKeywords[] = { "String", "ArrayList", "Map" };
	private final double GtoY = .70; // Green to yellow threshold
	private final double YtoR = .85; // Yellow to red threshold

	public PlagiarismEngine() {
		files = new ArrayList<File>();
		students = new ArrayList<Student>();
	}

	/**
	 * Receives files from the Folder Engine
	 * 
	 * @param files
	 */
	public void receiveFiles(ArrayList<File> files) {
		this.files = files;
	}

	/**
	 * Creates a student for each file
	 */
	public void createStudents() {
		Student currentStudent;
		int ID = 0;
		for (File file : files) {
			students.add(currentStudent = new Student(ID, file.getName()));
			currentStudent.addFile(file);
			ID++;
		}
	}

	/**
	 * Prints the ID of the students for debugging purposes
	 */
	public void printStudents() {
		System.out.println("\nStudents");
		for (Student s : students) {
			System.out.println(s.getID());
		}
	}

	/**
	 * Returns a deep copy of the students
	 * 
	 * @return
	 */
	public ArrayList<Student> getStudents() {
		return new ArrayList<Student>(students);
	}

	/**
	 * Removes comments and excess white space from a file
	 * 
	 * @param s - a student
	 */
	private void parseFile(Student s) {

		for (File codeFile : s.getFiles()) {
			try {

				// create a new scanner
				Scanner scnr = new Scanner(codeFile);

				// create a file to write the results to
				// System.out.println(codeFile.getName());
				File strippedSub = new File("Storage\\" + s.getID() + codeFile.getName());

				// create a file writer and buffer writer for writing
				FileWriter filwrit = new FileWriter(strippedSub);
				BufferedWriter bufwrit = new BufferedWriter(filwrit);

				String currLine; // holds current line
				int indexOfSlashes; // holds index of double slash
				String shortenedStr; // holds the stripped line
				String nextLine; // holds next line

				int indexOfBlkComStart;
				int indexOfBlkComEnd;

				String beforeCom;
				String afterCom;

				boolean firstComment = true;

				// iterate over each line in the file
				while (scnr.hasNextLine()) {
					// grab the next line
					currLine = scnr.nextLine();
					// if it's not empty
					if (!(currLine.isEmpty())) {
						// if the current line contains opening comment chars
						if (currLine.contains("/*")) {
							// if the same line contains closing comment chars
							if (currLine.contains("*/")) {
								// find index of each
								indexOfBlkComStart = currLine.indexOf("/*");
								indexOfBlkComEnd = currLine.indexOf("*/");
								if (!firstComment) {
									// create substrings with those indices
									beforeCom = currLine.substring(0, indexOfBlkComStart);
									afterCom = currLine.substring(indexOfBlkComEnd + 2);
									// write the surrounding text to screen
									bufwrit.write(beforeCom + afterCom);
								} else {
									bufwrit.write(currLine + "\n");
									firstComment = false;
								}

							}
							// else if it doesn't have closing chars,
							// but has more lines
							else if (scnr.hasNextLine()) {
								// grab the next line
								if (!firstComment) {
									nextLine = scnr.nextLine();
									// see if it contains the closing chars
									while (!(nextLine.contains("*/"))) {
										// if the scanner has more
										if (scnr.hasNextLine()) {
											// grab the next line
											nextLine = scnr.nextLine();
										} else {
											// otherwise, break out of loop
											break;
										}
									}
								} else {
									bufwrit.write(currLine + "\n");
									firstComment = false;
								}
							}
						} // end if statement for block comment chars
							// if it doesn't have a block comment char
						else {
							// look for double slashes
							indexOfSlashes = currLine.indexOf("//");
							// if it doesn't have any, just write the line
							if (indexOfSlashes == -1) {
								bufwrit.write(currLine + "\n");
							} else {
								if (!firstComment) {
									// otherwise use the index to shorten the string
									if (indexOfSlashes != 0) {
										shortenedStr = currLine.substring(0, indexOfSlashes);
										// write the line minus the comment
										bufwrit.write(shortenedStr + "\n");
									}
								} else {
									bufwrit.write(currLine + "\n");
									firstComment = false;
								}
							}
						}
					}
				}
				// close scanners and writers
				bufwrit.flush();
				bufwrit.close();
				scnr.close();

				// replace the file with the stripped file in the student
				s.replaceFile(codeFile, strippedSub);
				s.setName(strippedSub.getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // for method

	}// stripFile method

	/**
	 * Calls the parseFile function for all the students
	 */
	public void parseAllFiles() {
		for (int i = 0; i < students.size(); i++) {
			parseFile(students.get(i));
//			for (File codeFile : students.get(i).getFiles()) {
//				System.out.println(codeFile.getName());
//			}
		}
	}

	/**
	 * 
	 * @param s
	 */
	private void countKeywords(Student s) {
		Scanner fileScnr;
		String currLine;
		int numKeywords = 0;
		for (File codeFile : s.getFiles()) {
			try {
				fileScnr = new Scanner(codeFile);
//				System.out.println(codeFile.getName());
				while (fileScnr.hasNextLine()) {
					currLine = fileScnr.nextLine();
					for (String word : primitiveTypeKeywords) {
						if (currLine.contains(word)) {
							s.addKeyword(word);
							numKeywords++;
						}
					}
					for (String word : commonNonprimitiveTypeKeywords) {
						if (currLine.contains(word)) {
							s.addKeyword(word);
							numKeywords++;
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		s.setScore(numKeywords);
	}// countKeywords

	/**
	 * Calls the countKeywords function for all the students
	 */
	public void countAllKeywords() {
		for (int i = 0; i < students.size(); i++) {
			countKeywords(students.get(i));
		}
	}

	/**
	 * Takes two students finds the overlap of keywords and sorts them into the
	 * appropriate categories
	 * 
	 * @param student1
	 * @param student2
	 */
	private void compare(Student student1, Student student2) {
		// Used to walk through the words in student 1's code
		Iterator<Map.Entry<String, Integer>> keywordIterator = student1.getKeywords().entrySet().iterator();
		// Copy of student 2's dictionary
		Map<String, Integer> student2Dictionary = student2.getKeywords();
		// How many words do the two java codes have in common
		int compScore = 0;
		// the percentage of keyword overlap
		double percent1, percent2;

		while (keywordIterator.hasNext()) {
			// Current entry being compared
			Map.Entry<String, Integer> word = (Map.Entry<String, Integer>) keywordIterator.next();
			// checks if student 2 has also used this word
			if (student2Dictionary.containsKey(word.getKey())) {
				// Finds the overlap of times this word is used
				if ((int) word.getValue() < student2Dictionary.get(word.getKey())) {
					compScore += (int) word.getValue();
				} else {
					compScore += student2Dictionary.get(word.getKey());
				}
			}
		}
		// calculate student 1s percentage
		percent1 = ((double) compScore) / (double) student2.getScore();
		// add the comparison score to the correct student
		student1.addCompScore(student2.getName(), percent1);

		// calculate student 2s percentage
		percent2 = ((double) compScore) / (double) student1.getScore();
		// add the comparison score to the correct student
		student2.addCompScore(student1.getName(), percent2);

		// place the students in the proper columns
		// student 1
		if (percent1 <= GtoY) {
			student1.addGreenStudent(student2);
		} else if (percent1 <= YtoR) {
			student1.addYellowStudent(student2);
		} else {
			student1.addRedStudent(student2);
		}
		// student 2
		if (percent2 <= GtoY) {
			student2.addGreenStudent(student1);
		} else if (percent2 <= YtoR) {
			student2.addYellowStudent(student1);
		} else {
			student2.addRedStudent(student1);
		}
	}

	/**
	 * Compares all the students in the students ArrayList
	 */
	public void compareAll() {
		// The student that the remaining students will be compared to
		int currStudent = 0;
		// Keeps track of who we have compared to
		// The final student in the ArrayList can be skipped
		while (currStudent < students.size() - 1) {
			// Compares the rest of the students to the current students
			for (int i = currStudent + 1; i < students.size(); i++) {
				compare(students.get(currStudent), students.get(i));
			}
			// move to the next student
			currStudent++;
		}
	}

	/**
	 * Prints files for debugging purposes
	 */
	private void printFiles() {
		if (files.size() > 0) {
			for (File file : files) {
				System.out.println(file.getName());
			}
		}
	}

}
