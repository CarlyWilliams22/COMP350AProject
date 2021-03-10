import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class PlagiarismEngine {

	private ArrayList<File> files;
	private ArrayList<Student> students;
	/*
	 * Keywords taken from the Wikipedia article List of Java keywords Link:
	 * https://en.wikipedia.org/wiki/List_of_Java_keywords
	 */
	private static String primitiveTypeKeywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch",
			"char", "class", "const", "continue", "default", "do", "double", "else", "enum", "extends", "final",
			"finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
			"native", "new", "non-sealed", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while", "true", "false", "null" };
	private static String commonNonprimitiveTypeKeywords[] = { "String", "ArrayList", "Map" };
	private static String controlStructureKeywords[] = { "if", "else", "if else", "for", "while", "do", "switch",
			"case", "break", "continue" };

	private final double GREEN = .25;
	private final double YELLOW = .5;
	private final double RED = 1;

	public PlagiarismEngine() {
		files = new ArrayList<File>();
		students = new ArrayList<Student>();
	}

	/**
	 * 
	 * @param files
	 */
	public void receiveFiles(ArrayList<File> files) {
		this.files = files;
	}

	/**
	 * 
	 */
	public void printFiles() {
		if (files.size() > 0) {
			for (File file : files) {
				System.out.println(file.getName());
			}
		}
	}

	/**
	 * 
	 */
	public void createStudents() {
		Student currentStudent;
		int ID = 0;
		for (File file : files) {
			students.add(currentStudent = new Student(ID, file.getName()));
			currentStudent.addFile(file);
			System.out.println(file.getName());
			ID++;
		}
	}

	public void printStudents() {
		System.out.println("\nStudents");
		for (Student s : students) {
			System.out.println(s.getID());
		}
	}

	public void parseWhitespace() {

	}

	/**
	 * 
	 * @param s
	 */
	public void stripFile(Student s) {
		for (File codeFile : s.getFiles()) {
			try {
				// create a new scanner
				Scanner scnr = new Scanner(codeFile);
				// create a file to write the results to
				File strippedSub = new File("strippedSub.txt");
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
								// create substrings with those indices
								beforeCom = currLine.substring(0, indexOfBlkComStart);
								afterCom = currLine.substring(indexOfBlkComEnd + 2);
								// write the surrounding text to screen
								bufwrit.write(beforeCom + afterCom);
							}
							// else if it doesn't have closing chars,
							// but has more lines
							else if (scnr.hasNextLine()) {
								// grab the next line
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
							}
						} // if for opening comment chars
						//if it doesn't have a block comment char
						else {
							//look for double slashes
							indexOfSlashes = currLine.indexOf("//");
							//if it doesn't have any, just write the line
							if (indexOfSlashes == -1) {
								bufwrit.write(currLine + "\n");
							} else {
								//otherwise use the index to shorten the string
								if (indexOfSlashes != 0) {
									shortenedStr = currLine.substring(
												0, indexOfSlashes);
									//write the line minus the comment
									bufwrit.write(shortenedStr + "\n");
								}
							}
						}
					}
				}
				//close scanners and writers
				bufwrit.flush();
				bufwrit.close();
				scnr.close();
				//replace the file with the stripped file in the student
				s.replaceFile(codeFile, strippedSub);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // for method

	}// stripFile method

	public void parseComments() {

	}

	/**
	 * 
	 * @param s
	 */
	public void countKeywords(Student s) {
		Scanner fileScnr;
		String currLine;
		for (File codeFile : s.getFiles()) {
			try {
				fileScnr = new Scanner(codeFile);
				while (fileScnr.hasNextLine()) {
					currLine = fileScnr.nextLine();
					for (String word : primitiveTypeKeywords) {
						if (currLine.contains(word)) {
							s.addKeyword(word);
						}
					}
					for (String word : commonNonprimitiveTypeKeywords) {
						if (currLine.contains(word)) {
							s.addKeyword(word);
						}
					}
					for (String word : controlStructureKeywords) {
						if (currLine.contains(word)) {
							s.addKeyword(word);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}// countKeywords

	/**
	 * Takes two students finds the overlap of keywords and sorts them into the
	 * appropriate categories
	 * 
	 * @param student1
	 * @param student2
	 */
	public void compare(Student student1, Student student2) {
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

		// calculate student 2s percentage
		percent2 = ((double) compScore) / (double) student1.getScore();

		// place the students in the proper columns
		// student 1
		if (percent1 < GREEN) {
			student1.addGreenStudent(student2);
		} else if (percent1 < YELLOW) {
			student1.addYellowStudent(student2);
		} else {
			student1.addRedStudent(student2);
		}
		// student 2
		if (percent2 < GREEN) {
			student2.addGreenStudent(student1);
		} else if (percent2 < YELLOW) {
			student2.addYellowStudent(student1);
		} else {
			student2.addRedStudent(student1);
		}
	}

}
