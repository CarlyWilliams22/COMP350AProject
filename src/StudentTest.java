import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

public class StudentTest {
	
	@Test
	void testGetID() {
		Student student = new Student(0, "student");
		assertEquals(0, student.getID());
	}
	
	@Test
	void testGetName() {
		Student student = new Student(0, "student");
		assertEquals("student", student.getName());
	}
	
	@Test
	void testGetScore() {
		Student student = new Student(0, "student");
		student.setScore(10);
		assertEquals(10, student.getScore());
	}
	
	@Test 
	void testGetFile(){
		File file = new File("test");
		Student student = new Student(0, "student");
		student.setFile(file);
		assertEquals(file.getName(), student.getFile().getName());
	}
	
	@Test
	void addKeyword() {
		Student student = new Student(0, "student");
		student.addKeyword("test");
		student.addKeyword("test");
		student.addKeyword("test");
		int value = student.getKeywords().get("test");
		assertEquals(3, value);
		
	}
	
	@Test
	void testAddGreenStudent() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addGreenStudent(student1);
		assertEquals(1, student.getGreenNum());
	}
	
	@Test
	void testAddYellowStudent() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addYellowStudent(student1);
		assertEquals(1, student.getYellowNum());
	}
	
	@Test
	void testAddRedStudent() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addRedStudent(student1);
		assertEquals(1, student.getRedNum());
	}
	
	@Test
	void testAddCompScore() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addCompScore("student1", .5);
		double value = student.getCompScores().get(student1.getName());
		assertEquals(.5, value, .0001);
	}
	
	@Test
	void testSetGreenNum() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addGreenStudent(student1);
		student.setGreen();
		assertEquals(1, student.getGreenNum());
	}
	
	@Test
	void testSetYellowNum() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addYellowStudent(student1);
		student.setYellow();
		assertEquals(1, student.getYellowNum());
	}
	
	@Test
	void testSetRedNum() {
		Student student = new Student(0, "student");
		Student student1 = new Student(1, "student1");
		student.addRedStudent(student1);
		student.setRed();
		assertEquals(1, student.getRedNum());
	}

}
