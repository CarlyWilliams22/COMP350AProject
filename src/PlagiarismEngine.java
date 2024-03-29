import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class PlagiarismEngine {

	private ArrayList<File> files; // projects to process
	private ArrayList<Student> students; //students being compared
	private Map<String, Integer> wordUse; //keeps track over the word usage over the 
	private Map<String, Double> weight; //weights assigned to the keywords

	/*
	 * Many of these keywords taken from the Wikipedia article List of Java keywords
	 */
	// Link: https://en.wikipedia.org/wiki/List_of_Java_keywords
	private static String keywords[] = { "class", "import", "public", "private", "new", "package", "return", "static",
			"abstract", "assert", "continue", "private", "protected", "break", "const", "enum", "extends", "final",
			"implements", "instanceof", "interface", "native", "non-sealed", "strictfp", "super", "synchronized",
			"this", "transient", "volatile", "catch", "finally", "throw", "try", "throws", "true", "false", "null",
			"String", "ArrayList", "Map", "boolean", "byte", "char", "double", "float", " int", "long", "short", "void",
			"selection", "iteration", "IOException", "FileInputStream", "FileOutputStream", "ArrayList", "File", "==",
			">=", "<=", "!=", ">", "<", "&&", "||", "++", "+=", "--", "-=", "=", "BufferedInputStream",
			"BufferedOutputStream", "DataInputStream", "DataOutputStream", "EOFException", "System.out.println",
			"System.out.print", "Random" };

	//Used to decide if a word is a selection keyword
	private static String selectionKeywords[] = { "case", "else", "goto", "if", "switch", "default", };

	//Used to decide if a word is a iteration keyword
	private static String iterationKeywords[] = { "do", "for", "while" };

	private final double GtoY = .75; // Green to yellow threshold
	private final double YtoR = .90; // Yellow to red threshold

	public PlagiarismEngine() {
		files = new ArrayList<File>();
		students = new ArrayList<Student>();
		wordUse = new HashMap<String, Integer>();
		weight = new HashMap<String, Double>();
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
		Student currentStudent;	//current student object that gets created
		String currentStudentName; //name of the student currently being created
		//List of student names in the project
		ArrayList<String> studentNames = new ArrayList<String>();
		boolean dataEntered; //has the student been created
		int studentNum; //The number of students with the same name
		int currIndex;	//index for for loop
		int nameLen;//length of the original name
		int ID = 0;//current student ID
		
		//removes confusing numbers off the end of the temp file names
		for (File file : files) {
			String nameOfCurrFile = file.getName();
			for(currIndex = 0; currIndex < file.getName().length(); currIndex++) {
				if(Character.isDigit(nameOfCurrFile.charAt(currIndex))) {
					break;
				}
			}
			
			//gets unique student name
			currentStudentName = file.getName().substring(0, currIndex);
			System.out.println("This is the student name: " + currentStudentName);
			dataEntered = false;
			studentNum = 2;
			nameLen = currentStudentName.length();
			//while the student hasn't been created
			while(!dataEntered) {
				//if the Array list of names does not contain the current students name then make the student
				if(!studentNames.contains(currentStudentName)) {
					students.add(currentStudent = new Student(ID, currentStudentName));
					currentStudent.setFile(file);
					studentNames.add(currentStudentName);
					dataEntered = true;
				}
				//if the current student name already exists then tack a number on the end
				else {
					currentStudentName = currentStudentName.substring(0, nameLen) + 
							" (" + Integer.toString(studentNum) + ")";
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

	public void addStudent(Student s) {
		students.add(s);
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
			File strippedSub = new File("Storage\\" + codeFile.getName());

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
					} // end if statement for block comment chars
					// if it doesn't have a block comment char
					else {
						// look for double slashes
						indexOfSlashes = currLine.indexOf("//");
						// if it doesn't have any, just write the line
						if (indexOfSlashes == -1) {
							bufwrit.write(currLine + "\n");
						} else {
							// otherwise use the index to shorten the string
							if (indexOfSlashes != 0) {
								shortenedStr = currLine.substring(0, indexOfSlashes);
								// write the line minus the comment
								bufwrit.write(shortenedStr + "\n");
							}
						}
					}
				}
			}
			// close scanners and writers
			bufwrit.flush();
			filwrit.flush();
			bufwrit.close();
			filwrit.close();
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
		Scanner fileScnr/*Scans the whole file*/, lineScnr;//Scans one line
		String currLine/*Current line being scanned*/, currToken;//Current chunk of string being searched
		//get the file from the student
		File codeFile = s.getFile();
		try {
			//get the File
			fileScnr = new Scanner(codeFile);
			while (fileScnr.hasNextLine()) {
				//get the line
				currLine = fileScnr.nextLine();
				lineScnr = new Scanner(currLine);
				while (lineScnr.hasNext()) {
					//get the token
					currToken = lineScnr.next();
					//check the token for keywords
					for (String word : keywords) {
						if (currToken.contains(word)) {
							//if word is an iteration keyword assign it the value "iteration"
							if (isIterationKeyword(word)) {
								word = "iteration";
							//if word is an selection keyword assign it the value "selection"							
							} else if (isSelectionKeyword(word)) {
								word = "selection";
							}
							s.addKeyword(word);
						}
					}
				}
				//close line scanner
				lineScnr.close();
			}
			//close file scanner
			fileScnr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Fills the wordUse map when called
	 * */
	public void findWordUsage() {
		//Number of students who used the word
		int numUsed;
		
		//Checks for every keyword
		for(String keyword: keywords) {
			numUsed = 0;
			//if student used the words increase numUsed
			for(Student s: students) {
				if(s.getKeywords().containsKey(keyword)) {
					numUsed++;
				}
			}
			if(numUsed > 0) {
				wordUse.put(keyword, numUsed);
			}
		}
	}
	
	public Map<String, Integer> getWordUse(){
		return wordUse;
	}

	
	/**
	 * Populates the weight map with the appropriate values
	 * */
	public void assignWeights() {
		//Iterates through all the keywords used by the students
		Iterator<Map.Entry<String, Integer>> iterator = wordUse.entrySet().iterator();
		//current keyword
		String keyword;
		//number of students who used the keyword
		int value;
		//the weight assigned to the keyword
		double currWeight;
		//total number of students in the project
		int totalStudents = students.size();
		
		while(iterator.hasNext()) {
			Map.Entry<String, Integer> set = (Map.Entry<String, Integer>) iterator.next();
			keyword = set.getKey();
			value = set.getValue();
			//weight is assigned %100 - %of students who used the keyword
			//if %100 percent of the students used the keyword then give it a default value of 10
			currWeight = (100-100*((double)value/(double)totalStudents));
			if(currWeight == 0) {
				currWeight = 10;
			}
			weight.put(keyword,currWeight);
		}
	}
	
	/**
	 * returns the weight map
	 * used for JUnit tests
	 * */
	public Map<String, Double> getWeight(){
		return weight;
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
	 * @param s1
	 * @param s2
	 * @return
	 */
	public int createCompScore(Student s1, Student s2) {
		//Iterates through all the keywords used by student 1
		Iterator<Map.Entry<String, Integer>> keywordIterator = s1.getKeywords().entrySet().iterator();
		//map of the words used by student 2
		Map<String, Integer> student2Dictionary = s2.getKeywords();
		//current comparison score
		int score = 0;
		//weight of the keyword
		Double weight = (double)0;
		//Current keyword
		String keyword;

		while (keywordIterator.hasNext()) {
			Map.Entry<String, Integer> word = (Map.Entry<String, Integer>) keywordIterator.next();
			keyword = word.getKey();
			//if both students used the word get the weight and multiply it by the smaller number of uses between 
			//the two then add it to the score
			if (student2Dictionary.containsKey(keyword)) {
				weight = getWeight().get(keyword);

				if ((int) word.getValue() < student2Dictionary.get(word.getKey())) {
					score += (int) word.getValue() * weight;
				} else {
					score += student2Dictionary.get(word.getKey()) * weight;
				}

			}
		}

		return score;
	}
	
	public void createScores() {
		//score of the current student
		int score;
		//number of times the student used a keyword
		int total;
		//Current keyword
		String keyword;
		Iterator<Map.Entry<String, Integer>> keywordIterator;
		for(Student s: students) {
			score = 0;
			keywordIterator = s.getKeywords().entrySet().iterator();
			/*while there are entries left in the map multiply the total times it was used 
			 * by the weight of the keyword and then add it to the score*/
			while(keywordIterator.hasNext()) {
				Map.Entry<String, Integer> word = (Map.Entry<String, Integer>) keywordIterator.next();
				keyword = word.getKey();
				total = word.getValue();
				score += total*weight.get(keyword);
			}
			s.setScore(score);
		}
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
	public boolean isIterationKeyword(String keyword) {
		for (String word : iterationKeywords) {
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
