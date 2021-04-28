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

	/*
	 * Many of these keywords taken from the Wikipedia article List of Java keywords
	 */
	// Link: https://en.wikipedia.org/wiki/List_of_Java_keywords
	private static String keywords[] = { "class", "import", "public", "private", "new", "package", "return", "static",
			"abstract", "assert", "continue", "private", "protected", "break", "const", "enum", "extends", "final",
			"implements", "instanceof", "interface", "native", "non-sealed", "strictfp", "super", "synchronized",
			"this", "transient", "volatile", "catch", "finally", "throw", "try", "throws", "true", "false", "null",
			"String", "ArrayList", "Map", "boolean", "byte", "char", "double", "float", " int", "long", "short", "void",
			"selection", "itteration", "IOException", "FileInputStream", "FileOutputStream", "ArrayList", "File", "==",
			">=", "<=", "!=", ">", "<", "&&", "||", "++", "+=", "--", "-=", "=", "BufferedInputStream",
			"BufferedOutputStream", "DataInputStream", "DataOutputStream", "EOFException", "System.out.println",
			"System.out.print", "Random" };

	private static String commonKeywords[] = { "class", "import", "public", "private", "new", "package", "return",
			"static", "System.out.println", "System.out.print" };
	private final int COMMON_WEIGHT = 1;

	private static String uncommonKeywords[] = { "abstract", "assert", "continue", "protected", "break", "const",
			"enum", "extends", "final", "implements", "instanceof", "interface", "native", "non-sealed", "strictfp",
			"super", "synchronized", "this", "transient", "volatile" };
	private final int UNCOMMON_WEIGHT = 5;

	private static String selectionKeywords[] = { "case", "else", "goto", "if", "switch", "default", };
	private final int SELECTION_WEIGHT = 3;

	private static String itterationKeywords[] = { "do", "for", "while" };
	private final int ITTERATION_WEIGHT = 3;

	private static String errorHandlingKeywords[] = { "catch", "finally", "throw", "try", "throws", "IOException" };
	private final int ERROR_HANDLING_WEIGHT = 4;

	private static String dataValueKeywords[] = { "true", "false", "null" };
	private final int DATA_VALUE_WEIGHT = 2;

	private static String dataTypeKeywords[] = { "String", "ArrayList", "Map", "boolean", "byte", "char", "double",
			"float", " int", "long", "short", "void", "ArrayList", "File", "FileInputStream", "FileOutputStream",
			"BufferedInputStream", "BufferedOutputStream", "DataInputStream", "DataOutputStream", "EOFException",
			"Random" };
	private final int DATA_TYPE_WEIGHT = 2;

	// TODO add to weight calc
	private static String symbolKeywords[] = { "==", ">=", "<=", "!=", ">", "<", "&&", "||", "++", "+=", "--", "-=",
			"=" };
	private final int SYMBOL_WEIGHT = 3;

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
		String currentStudentName;
		ArrayList<String> studentNames = new ArrayList<String>();
		boolean dataEntered;
		int studentNum;
		int currIndex;
		int nameLen;
		int ID = 0;
		for (File file : files) {
			String nameOfCurrFile = file.getName();
			for(currIndex = 0; currIndex < file.getName().length(); currIndex++) {
				if(Character.isDigit(nameOfCurrFile.charAt(currIndex))) {
					break;
				}
			}
			currentStudentName = file.getName().substring(0, currIndex);
			System.out.println("This is the student name: " + currentStudentName);
			dataEntered = false;
			studentNum = 2;
			nameLen = currentStudentName.length();
			while(!dataEntered) {
				if(!studentNames.contains(currentStudentName)) {
					students.add(currentStudent = new Student(ID, currentStudentName));
					currentStudent.setFile(file);
					studentNames.add(currentStudentName);
					dataEntered = true;
				}
				else {
					currentStudentName = currentStudentName.substring(0, nameLen) + Integer.toString(studentNum);
					studentNum++;
				}
			}
			
			ID++;
		}
	}

	/**
	 * Removes all of the students from memory
	 */
	public void clearStudents() {
		students.clear();
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
	 * Returns a student with matching name
	 * 
	 * @param name
	 * @return
	 */
	public Student getStudent(String name) {
		Student get = null;

		for (Student s : students) {
			if (s.getName().equals(name)) {
				get = s;
			}
		}
		return get;
	}

	// TODO FIX
	/**
	 * Removes comments and excess white space from a file TODO refactor
	 * 
	 * @param s - a student
	 */
	public void parseFile(Student s) {

		File codeFile = s.getFile();
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
			s.setFile(strippedSub);
			//s.setName(strippedSub.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// stripFile method

	/**
	 * Calls the parseFile function for all the students
	 */
	public void parseAllFiles() {
		for (int i = 0; i < students.size(); i++) {
			parseFile(students.get(i));
		}
	}

	/**
	 * 
	 * @param s
	 */
	public void countKeywords(Student s) {
		Scanner fileScnr, lineScnr;
		String currLine, currToken;
		int weight = 0;
		File codeFile = s.getFile();
		try {
			fileScnr = new Scanner(codeFile);
			while (fileScnr.hasNextLine()) {
				currLine = fileScnr.nextLine();
				lineScnr = new Scanner(currLine);
				while (lineScnr.hasNext()) {
					currToken = lineScnr.next();
					for (String word : keywords) {
						if (currToken.contains(word)) {
							if (isItterationKeyword(word)) {
								word = "itteration";
							} else if (isSelectionKeyword(word)) {
								word = "selection";
							}
							s.addKeyword(word);
							weight += getWeight(word);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		s.setScore(weight);
	}

	/**
	 * Calls the countKeywords function for all the students
	 */
	public void countAllKeywords() {
		for (int i = 0; i < students.size(); i++) {
			countKeywords(students.get(i));
		}
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public int getWeight(String keyword) {
		if (isCommonKeyword(keyword)) {
			return COMMON_WEIGHT;
		} else if (isUncommonKeyword(keyword)) {
			return UNCOMMON_WEIGHT;
		} else if (keyword.equals("selection")) {
			return SELECTION_WEIGHT;
		} else if (keyword.equals("itteration")) {
			return ITTERATION_WEIGHT;
		} else if (isDataTypeKeyword(keyword)) {
			return DATA_TYPE_WEIGHT;
		} else if (isDataValueKeyword(keyword)) {
			return DATA_VALUE_WEIGHT;
		} else if (isSymbolKeyword(keyword)) {
			return SYMBOL_WEIGHT;
		} else {
			return ERROR_HANDLING_WEIGHT;
		}
	}

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public int createCompScore(Student s1, Student s2) {
		Iterator<Map.Entry<String, Integer>> keywordIterator = s1.getKeywords().entrySet().iterator();
		Map<String, Integer> student2Dictionary = s2.getKeywords();
		int score = 0;
		int weight = 0;
		String keyword;

		while (keywordIterator.hasNext()) {
			Map.Entry<String, Integer> word = (Map.Entry<String, Integer>) keywordIterator.next();
			keyword = word.getKey();
			if (student2Dictionary.containsKey(keyword)) {
				weight = getWeight(keyword);

				if ((int) word.getValue() < student2Dictionary.get(word.getKey())) {
					score += (int) word.getValue() * weight;
				} else {
					score += student2Dictionary.get(word.getKey()) * weight;
				}

			}
		}

		return score;
	}

	/**
	 * Takes two students finds the overlap of keywords and sorts them into the
	 * appropriate categories
	 * 
	 * @param student1
	 * @param student2
	 */
	public void compare(Student student1, Student student2) {
		// How many words do the two java codes have in common
		int compScore;
		// the percentage of keyword overlap
		double percent1, percent2;

		compScore = createCompScore(student1, student2);

		// calculate student 1s percentage
		int score = student2.getScore();
		percent1 = ((double) compScore) / (double) score;
		// add the comparison score to the correct student
		student1.addCompScore(student2.getName(), percent1);

		// calculate student 2s percentage
		score = student1.getScore();
		percent2 = ((double) compScore) / (double) score;
		// add the comparison score to the correct student
		student2.addCompScore(student1.getName(), percent2);

		colorPlacement(student1, student2, percent1, percent2);

	}

	/**
	 * 
	 * @param student1
	 * @param student2
	 * @param percent1
	 * @param percent2
	 */
	public void colorPlacement(Student student1, Student student2, double percent1, double percent2) {
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
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isCommonKeyword(String keyword) {
		for (String word : commonKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isUncommonKeyword(String keyword) {
		for (String word : uncommonKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isSelectionKeyword(String keyword) {
		for (String word : selectionKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isItterationKeyword(String keyword) {
		for (String word : itterationKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isErrorHandlingKeyword(String keyword) {
		for (String word : errorHandlingKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isDataValueKeyword(String keyword) {
		for (String word : dataValueKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isDataTypeKeyword(String keyword) {
		for (String word : dataTypeKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public boolean isSymbolKeyword(String keyword) {
		for (String word : symbolKeywords) {
			if (word.equals(keyword))
				return true;
		}
		return false;
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
	 * Clears files for new set of data
	 */
	public void clearFiles() {
		files.clear();
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
