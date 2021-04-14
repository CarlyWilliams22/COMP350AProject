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
	
	
	
}
