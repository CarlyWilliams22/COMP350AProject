import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
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
	void getDataValueTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(2, pe.getWeight("false"));
	}
	
	@Test
	void getDataTypeWeightTest() {
		PlagiarismEngine pe = new PlagiarismEngine();
		assertEquals(2, pe.getWeight(" int"));
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
		s1.addFile(f);
		PlagiarismEngine pe = new PlagiarismEngine();
		pe.countKeywords(s1);
		assertEquals(10, s1.getScore());
	}
	
}
