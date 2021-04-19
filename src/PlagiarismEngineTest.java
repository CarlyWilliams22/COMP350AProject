import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class PlagiarismEngineTest {

	@Test
	void testIsCommonKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isCommonKeyword("class"));
	}
	
	@Test
	void testIsCommonKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isCommonKeyword("test"));
	}
	
	@Test
	void testIsUncommonKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isUncommonKeyword("break"));
	}
	
	@Test
	void testIsUncommonKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isUncommonKeyword("test"));
	}
	
	@Test
	void testIsDataTypeKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isDataTypeKeyword("boolean"));
	}
	
	@Test
	void testIsDataTypeKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isDataTypeKeyword("test"));
	}
	
	@Test
	void testIsDataValueKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isDataValueKeyword("true"));
	}
	
	@Test
	void testIsDataValueKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isDataValueKeyword("test"));
	}
	
	@Test
	void testIsErrorHandlingKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isErrorHandlingKeyword("throw"));
	}
	
	@Test
	void testIsErrorHandlingKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isErrorHandlingKeyword("test"));
	}
	
	@Test
	void testIsItterationKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isItterationKeyword("for"));
	}
	
	@Test
	void testIsItterationKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isItterationKeyword("test"));
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
	void testIsSymbolKeywordValid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertTrue(pe.isSymbolKeyword("=="));
	}
	
	@Test
	void testIsSymbolKeywordInvalid() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertFalse(pe.isSymbolKeyword("test"));
	}
	
	@Test
	void getCommonWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(1, pe.getWeight("public"));
	}
	
	@Test
	void getUncommonWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(5, pe.getWeight("assert"));
	}
	
	@Test
	void getSelectionWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(3, pe.getWeight("selection"));
	}
	
	@Test
	void getitterationWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(3, pe.getWeight("itteration"));
	}
	
	@Test
	void getErrorHandlingWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(4, pe.getWeight("try"));
	}
	
	@Test
	void getDataValueWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(2, pe.getWeight("false"));
	}
	
	@Test
	void getDataTypeWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(2, pe.getWeight(" int"));
	}
	
	@Test
	void getSymbolWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(3, pe.getWeight("--"));
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
		s2.addKeyword("for");
		s2.addKeyword("class");
		PlagiarismEngine pe = new PlagiarismEngine();
		int score = pe.createCompScore(s1, s2);
		assertEquals(6, score);
	}
	
	@Test
	void countKeywordsTest() {
		Student s1 = new Student(0, "one");
		File f = new File("test");
		s1.setFile(f);
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.countKeywords(s1);
		assertEquals(10, s1.getScore());
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
		pe.colorPlacement(s1, s2, .5, .9);
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
		pe.colorPlacement(s1, s2, .8, .9);
		assertEquals(1, s1.getYellowNum());
		assertEquals(1, s2.getRedNum());
	}
	
	@Test
	void testColorPlacementRedAndRed(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .9, .9);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getRedNum());
	}
	
	@Test
	void testColorPlacementRedAndGreen(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .9, .5);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getGreenNum());
	}
	
	@Test
	void testColorPlacementRedAndYellow(){
		PlagiarismEngine pe = new PlagiarismEngine();
		Student s1 = new Student(0, "0");
		Student s2 = new Student(1, "1");
		pe.colorPlacement(s1, s2, .9, .8);
		assertEquals(1, s1.getRedNum());
		assertEquals(1, s2.getYellowNum());
	}
	
	@Test
	void testForAccurateComparison() {
		Student s1 = new Student(0, "one");
		File f1 = new File("Student1Test");
		s1.setFile(f1);
		Student s2 = new Student(1, "two");
		File f2 = new File("Student2Test");
		s2.setFile(f2);
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.countKeywords(s1);
		pe.countKeywords(s2);
		pe.compare(s1, s2);
		double value = s1.getCompScores().get(s2.getName());
		assertEquals(.5, value, .0001);
		value = s2.getCompScores().get(s1.getName());
		assertEquals(.5, value, .0001);
	}
	
//	@Test
//	void testCommentsAreIgnoredWhenScored() {
//		PlagiarismEngine pe = new PlagiarismEngine();
//		File f = new File("testForParseFile");
//		Student s = new Student(0, "s");
//		s.setFile(f);
//		pe.parseFile(s);
//		pe.countKeywords(s);
//		assertEquals(2, s.getScore());
//	}
	
}
