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
		assertEquals(12, ss.getScore());
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
	
	
	@Test
	void testRedStudents() {
		//create testing plagiarism engine
		PlagiarismEngine pe = new PlagiarismEngine();
			
		//System.out.println("Test student 1");
		//create test student
		Student testStud = new Student(1, "Tommy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile = new File("TommyCode.txt");
		testStud.addFile(studentTestFile);
		pe.stripFile(testStud);		
		//count the keywords in the test student file
		pe.countKeywords(testStud);
		//print the results
		//testStud.printKeywords();
		
		//System.out.println("Test student 2");
		//create test student
		Student testStud2 = new Student(2, "Timmy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile2 = new File("TimmyCode.txt");
		testStud2.addFile(studentTestFile2);
		pe.stripFile(testStud2);		
		//count the keywords in the test student file
		pe.countKeywords(testStud2);
		//print the results
		//testStud2.printKeywords();
		//System.out.println(testStud2.getScore());
				
		pe.compare(testStud, testStud2);
		//System.out.println("Score for tommy: " + testStud.getScore());
		assertEquals(1, testStud.getRedStudents().size());
		
		
		//System.out.println("Score for timmy: " + testStud2.getScore());
		assertEquals(1, testStud2.getRedStudents().size());

		
//		for(int i = 0; i < testStud.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Tommy: " + testStud.getRedStudents().get(i).getName());
//		}		
		assertEquals("Timmy", testStud.getRedStudents().get(0).getName());
		
//		for(int i = 0; i < testStud2.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Timmy: " + testStud2.getRedStudents().get(i).getName());
//		}	
		
		assertEquals("Tommy", testStud2.getRedStudents().get(0).getName());

	}
	
	
	@Test
	void testYellowStudents() {
		//create testing plagiarism engine
		PlagiarismEngine pe = new PlagiarismEngine();
			
		//System.out.println("Test student 1");
		//create test student
		Student testStud = new Student(1, "Tommy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile = new File("TommyCode.txt");
		testStud.addFile(studentTestFile);
		pe.stripFile(testStud);		
		//count the keywords in the test student file
		pe.countKeywords(testStud);
		//print the results
		//testStud.printKeywords();
		
		//System.out.println("Test student 2");
		//create test student
		Student testStud2 = new Student(2, "Timmy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile2 = new File("TimmyCode.txt");
		testStud2.addFile(studentTestFile2);
		pe.stripFile(testStud2);		
		//count the keywords in the test student file
		pe.countKeywords(testStud2);
		//print the results
		//testStud2.printKeywords();
		//System.out.println(testStud2.getScore());
				
		pe.compare(testStud, testStud2);
		//System.out.println("Score for tommy: " + testStud.getScore());
		assertEquals(0, testStud.getYellowStudents().size());
		
		
		//System.out.println("Score for timmy: " + testStud2.getScore());
		assertEquals(0, testStud2.getYellowStudents().size());

		
//		for(int i = 0; i < testStud.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Tommy: " + testStud.getRedStudents().get(i).getName());
//		}		
		//assertEquals(null, testStud.getYellowStudents().get(0).getName());
		assertThrows(IndexOutOfBoundsException.class, () -> 
				{testStud.getYellowStudents().get(0).getName();
				});
		
//		for(int i = 0; i < testStud2.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Timmy: " + testStud2.getRedStudents().get(i).getName());
//		}	
		
		//assertEquals(null, testStud2.getYellowStudents().get(0).getName());

		assertThrows(IndexOutOfBoundsException.class, () -> 
		{testStud2.getYellowStudents().get(0).getName();
		});
	}

	
	@Test
	void testGreenStudents() {
		//create testing plagiarism engine
		PlagiarismEngine pe = new PlagiarismEngine();
			
		//System.out.println("Test student 1");
		//create test student
		Student testStud = new Student(1, "Tommy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile = new File("TommyCode.txt");
		testStud.addFile(studentTestFile);
		pe.stripFile(testStud);		
		//count the keywords in the test student file
		pe.countKeywords(testStud);
		//print the results
		//testStud.printKeywords();
		
		//System.out.println("Test student 2");
		//create test student
		Student testStud2 = new Student(2, "Timmy");
		//give student a hardcoded txt file with dummy code
		File studentTestFile2 = new File("TimmyCode.txt");
		testStud2.addFile(studentTestFile2);
		pe.stripFile(testStud2);		
		//count the keywords in the test student file
		pe.countKeywords(testStud2);
		//print the results
		//testStud2.printKeywords();
		//System.out.println(testStud2.getScore());
				
		pe.compare(testStud, testStud2);
		//System.out.println("Score for tommy: " + testStud.getScore());
		assertEquals(0, testStud.getGreenStudents().size());
		
		
		//System.out.println("Score for timmy: " + testStud2.getScore());
		assertEquals(0, testStud2.getGreenStudents().size());

		
//		for(int i = 0; i < testStud.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Tommy: " + testStud.getRedStudents().get(i).getName());
//		}		
		//assertEquals(null, testStud.getYellowStudents().get(0).getName());
		assertThrows(IndexOutOfBoundsException.class, () -> 
				{testStud.getGreenStudents().get(0).getName();
				});
		
//		for(int i = 0; i < testStud2.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Timmy: " + testStud2.getRedStudents().get(i).getName());
//		}	
		
		//assertEquals(null, testStud2.getYellowStudents().get(0).getName());

		assertThrows(IndexOutOfBoundsException.class, () -> 
		{testStud2.getGreenStudents().get(0).getName();
		});
	}
}
