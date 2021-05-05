import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;


class PlagiarismEngineTest {
	
	@Test
	void testIsIterationKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isIterationKeyword("for"));
	}
	
	@Test
	void testIsIterationKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isIterationKeyword("test"));
	}
	
	@Test
	void testIsSelectionKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isSelectionKeyword("if"));
	}
	
	@Test
	void testIsSelectionKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isSelectionKeyword("test"));
	}
	
	@Test
	void getWordUsageTest() {
		Student s1 = new Student(0, "one");
		s1.addKeyword("public");
		s1.addKeyword("public");
		s1.addKeyword("selection");
		s1.addKeyword("catch");
		s1.addKeyword("class");
		Student s2 = new Student(1, "two");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("selection");
		s2.addKeyword("iteration");
		s2.addKeyword("class");
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.addStudent(s1);
		pe.addStudent(s2);
		pe.findWordUsage();
		int value = pe.getWordUse().get("public");
		assertEquals(2, value);
		value = pe.getWordUse().get("selection");
		assertEquals(2, value);
		value = pe.getWordUse().get("catch");
		assertEquals(1, value);
		value = pe.getWordUse().get("class");
		assertEquals(2, value);
		value = pe.getWordUse().get("iteration");
		assertEquals(1, value);
	}

	@Test
	void testSetWeights() {
		double delta = .0001;
		Student s1 = new Student(0, "one");
		s1.addKeyword("public");
		s1.addKeyword("public");
		s1.addKeyword("selection");
		s1.addKeyword("catch");
		s1.addKeyword("class");
		Student s2 = new Student(1, "two");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("selection");
		s2.addKeyword("iteration");
		s2.addKeyword("class");
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.addStudent(s1);
		pe.addStudent(s2);
		pe.findWordUsage();
		pe.assignWeights();
		Double value = pe.getWeight().get("public");
		assertEquals(10, value, delta);
		value = pe.getWeight().get("catch");
		assertEquals(50, value, delta);
	}
	
	@Test
	void testCreateCompScore() {
		Student s1 = new Student(0, "one");
		s1.addKeyword("public");
		s1.addKeyword("public");
		s1.addKeyword("selection");
		s1.addKeyword("catch");
		s1.addKeyword("class");
		Student s2 = new Student(1, "two");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("selection");
		s2.addKeyword("class");
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.addStudent(s1);
		pe.addStudent(s2);
		pe.findWordUsage();
		pe.assignWeights();
		int score = pe.createCompScore(s1, s2);
		assertEquals(40, score);
	}
	
	@Test
	void testCreateScore() {
		Student s1 = new Student(0, "one");
		s1.addKeyword("public");
		s1.addKeyword("public");
		s1.addKeyword("selection");
		s1.addKeyword("catch");
		s1.addKeyword("class");
		Student s2 = new Student(1, "two");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("public");
		s2.addKeyword("iteration");
		s2.addKeyword("class");
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.addStudent(s1);
		pe.addStudent(s2);
		pe.findWordUsage();
		pe.assignWeights();
		pe.createScores();
		assertEquals(130, s1.getScore());
	}
	
	@Test
	void countKeywordsTest() {
		Student s1 = new Student(0, "one");
		File f = new File("test");
		s1.setFile(f);
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.countKeywords(s1);
		int value = s1.getKeywords().get("public");
		assertEquals(2, value);
		value = s1.getKeywords().get("selection");
		assertEquals(1, value);
		value = s1.getKeywords().get("catch");
		assertEquals(1, value);
		value = s1.getKeywords().get("class");
		assertEquals(1, value);
	}
	
	@Test
	void testCreateStudents() {
		File f = new File("test");
		PlagiarismEngine pe = new PlagiarismEngine();
		ArrayList<File> fs = new ArrayList<File>();
		fs.add(f);
		pe.receiveFiles(fs);
		pe.createStudents();
		Student student = pe.getStudents().get(0);
		assertEquals(0, student.getID());
		assertEquals("test", student.getName());
		assertEquals(f, student.getFile());
	}
	
	@Test
	void testColorPlacementGreenAndGreen(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .5, .5);
		assertEquals(1, s1.getGreenNum());
		assertEquals(1, s2.getGreenNum());
	}
	
	@Test
	void testColorPlacementGreenAndYellow(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .5, .8);
		assertEquals(1, s1.getGreenNum());
		assertEquals(1, s2.getYellowNum());
	}
	
	@Test
	void testColorPlacementGreenAndRed(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .5, .91);
		assertEquals(1, s1.getGreenNum());
		assertEquals(1, s2.getRedNum());
	}
	
	@Test
	void testColorPlacementYellowAndYellow(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .8, .8);
		assertEquals(1, s1.getYellowNum());
		assertEquals(1, s2.getYellowNum());
	}
	
	@Test
	void testColorPlacementYellowAndGreen(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .8, .5);
		assertEquals(1, s1.getYellowNum());
		assertEquals(1, s2.getGreenNum());
	}
	
	@Test
	void testColorPlacementYellowAndRed(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .8, .91);
		assertEquals(1, s1.getYellowNum());
		assertEquals(1, s2.getRedNum());
	}
	
	@Test
	void testColorPlacementRedAndRed(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .91, .91);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getRedNum());
	}
	
	@Test
	void testColorPlacementRedAndGreen(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .91, .5);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getGreenNum());
	}
	
	@Test
	void testColorPlacementRedAndYellow(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .91, .8);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getYellowNum());
	}
	
	@Test
	void testForAccurateComparison() {
		//TODO fix
		Student s1 = new Student(0, "one");
		File f1 = new File("Student1Test");
		s1.setFile(f1);
		Student s2 = new Student(1, "two");
		File f2 = new File("Student2Test");
		s2.setFile(f2);
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.countKeywords(s1);
		pe.countKeywords(s2);
		pe.addStudent(s1);
		pe.addStudent(s2);
		pe.findWordUsage();
		pe.assignWeights();
		pe.createScores();
		pe.compare(s1, s2);
		double value = s1.getCompScores().get(s2.getName());
		assertEquals(.2142, value, .0001);
		value = s2.getCompScores().get(s1.getName());
		assertEquals(.23076, value, .0001);
	}
	
	@Test
	void testCommentsAreIgnoredWhenScored() {
		PlagiarismEngine pe = new PlagiarismEngine();
		File f = new File("testForParseFile");
		Student s = new Student(0, "s");
		s.setFile(f);
		pe.parseFile(s);
		pe.countKeywords(s);
		int value = s.getKeywords().get("public");
		assertEquals(2, value);
	}
	
}
