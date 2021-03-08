import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class PlagiarismEngine {

	private ArrayList<Student> students;
	private static String primitiveTypeKeywords[] = {"byte", "short", "int",
			"long", "float", "double", "boolean", "char"};
	private static String commonNonprimitiveTypeKeywords[] = 
		{"String", "ArrayList", "Map"};
	private static String controlStructureKeywords[] = {"if",
			"else", "if else", "for", "while", "do", "switch", 
			"case", "break", "continue"};

	public PlagiarismEngine() {

	}
	
	public void addStudent(Student s) {
		students.add(s);
	}

	public void parseWhitespace() {

	}
	
	public void stripFile(File submission) {
		try {
			Scanner scnr = new Scanner(submission);
			File strippedSub = new File("strippedSub.txt");
			FileWriter filwrit = new FileWriter(strippedSub);
			BufferedWriter bufwrit = new BufferedWriter(filwrit);
			String currLine;
			int indexOfSlashes;
			String shortenedStr;
			while(scnr.hasNextLine()) {
				currLine = scnr.nextLine();
				if(!(currLine.isEmpty())) {
					indexOfSlashes = currLine.indexOf("//");
					if(indexOfSlashes == -1) {
						bufwrit.write(currLine + "\n");
					} else {
						if(indexOfSlashes != 0) {
							shortenedStr = currLine.substring(0, indexOfSlashes);
							bufwrit.write(shortenedStr + "\n");
						}
					}
				}
			}
			bufwrit.flush();
			bufwrit.close();
		} catch (Exception e){
			e.printStackTrace();
		} 
		
	}//stripFile method

	public void parseComments() {

	}

	public void tokenize(Student s) {

	}

	public void compare() {

	}

}
