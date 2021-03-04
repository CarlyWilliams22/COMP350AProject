import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class Student {

	private int ID;
	private String name;
	private int score;

	private Map<String, Integer> tokens;

	private ArrayList<File> files;
	private ArrayList<Student> greenStudents;
	private ArrayList<Student> yellowStudents;
	private ArrayList<Student> redStudents;

	public Student() {

	}

	public int getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addFile(File f) {
		files.add(f);
	}

	public ArrayList<File> getFiles() {
		return new ArrayList<File>(files);
	}

	public void addToken(String s) {
		if (tokens.containsKey(s)) {
			tokens.put(s, tokens.get(s) + 1);
		} else {
			tokens.put(s, 1);
		}

	}

	public void addGreenStudent(Student s) {
		greenStudents.add(s);
	}

	public ArrayList<Student> getGreenStudents() {
		return new ArrayList<Student>(greenStudents);
	}

	public void addYellowStudent(Student s) {
		yellowStudents.add(s);
	}

	public ArrayList<Student> getYellowStudents() {
		return new ArrayList<Student>(yellowStudents);
	}

	public void addRedStudent(Student s) {
		redStudents.add(s);
	}

	public ArrayList<Student> getRedStudents() {
		return new ArrayList<Student>(redStudents);
	}

}
