import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlagiarismEngineTest {

//	@Test
//	void testPlagiarismEngine() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testReceiveFiles() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testPrintFiles() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCreateStudents() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testPrintStudents() {
//		fail("Not yet implemented");
//	}

//	@Test
//	void testStripFile() {
//		PlagiarismEngine pe = new PlagiarismEngine();
//		File testFile = new File("FileStripTest.txt");
//		Student ss = new Student(0, "Test");
//		ss.addFile(testFile);
//		pe.stripFile(ss);
//		
//		FileAssert("The files differ", Files. (ss.getFiles().get(0), "utf-8"), new File("strippedSub.txt"));
//	}
	

//	@Test
//	void testStripAll() {
//		fail("Not yet implemented");
//	}
//
	@Test
	void testCountKeywords() {
		PlagiarismEngine pe = new PlagiarismEngine();		
		File timmyCode = new File("TimmyCode.txt");
		Student ss = new Student(73, "Test");
		ss.addFile(timmyCode);
		pe.stripFile(ss);
		pe.countKeywords(ss);
		assertEquals(13, ss.getScore());
	}
//
//	@Test
//	void testAllStudentKeywords() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCompare() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCompareAll() {
//		fail("Not yet implemented");
//	}

}
