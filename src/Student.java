import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student {

	private int ID;
	private String name;
	private int red;
	private int yellow;
	private int green;
	private int score;
	private File file;

	private Map<String, Integer> keywords;
	private Map<String, Double> compScores;

	private ArrayList<Student> greenStudents;
	private ArrayList<Student> yellowStudents;
	private ArrayList<Student> redStudents;

	public Student(int ID, String name) {
		this.ID = ID;
		this.name = name;
		this.greenStudents = new ArrayList<Student>();
		this.yellowStudents = new ArrayList<Student>();
		this.redStudents = new ArrayList<Student>();
		this.keywords = new HashMap<String, Integer>();
		this.compScores = new HashMap<>();
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

	public void setFile(File f) {
		file = f;
	}

	public File getFile() {
		return file;
	}

	public void addKeyword(String s) {
		if (keywords.containsKey(s)) {
			keywords.put(s, keywords.get(s) + 1);
		} else {
			keywords.put(s, 1);
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

	public void printKeywords() {
		for (Map.Entry<String, Integer> entry : keywords.entrySet()) {
			System.out.println("keyword: " + entry.getKey() + "; times appeared: " + entry.getValue());
		}
	}

	public Map<String, Integer> getKeywords() {
		return keywords;
	}

	/**
	 * Will store the comp scores for all the students compared to this student
	 *
	 * @param key
	 * @param value
	 */
	public void addCompScore(String key, Double value) {
		compScores.put(key, value);
	}
	
	public Map<String, Double> getCompScores(){
		return compScores;
	}

	public void setGreen() {
		green = greenStudents.size();
	}
	
	public int getGreenNum() {return greenStudents.size();}
	
	public void setYellow() {
		yellow = yellowStudents.size();
	}

	public int getYellowNum() {return yellowStudents.size();}
	
	public void setRed() {
		red = redStudents.size();
	}

	public int getRedNum() {return redStudents.size();}
}
