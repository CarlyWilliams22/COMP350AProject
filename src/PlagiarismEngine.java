import java.util.ArrayList;

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

	public void parseComments() {

	}

	public void tokenize() {

	}

	public void compare() {

	}

}
