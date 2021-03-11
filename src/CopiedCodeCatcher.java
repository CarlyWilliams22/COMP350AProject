import java.io.File;
import java.util.ArrayList;

public class CopiedCodeCatcher {

	public static void main(String[] args) {

		UI ui = new UI();
		
		//create testing plagiarism engine
		PlagiarismEngine pe = new PlagiarismEngine();
		//use hard coded text file to check strip method
		//results found in file called "strippedSub.txt"
		File testFile = new File("FileStripTest.txt");
		Student ss = new Student(0, "Test");
		ss.addFile(testFile);
		pe.stripFile(ss);
		
//		System.out.println("Test student 1");
//		//create test student
//		Student testStud = new Student(1, "Tommy");
//		//give student a hardcoded txt file with dummy code
//		File studentTestFile = new File("TommyCode.txt");
//		testStud.addFile(studentTestFile);
//		pe.stripFile(testStud);		
//		//count the keywords in the test student file
//		pe.countKeywords(testStud);
//		//print the results
//		testStud.printKeywords();
//		
//		System.out.println("Test student 2");
//		//create test student
//		Student testStud2 = new Student(2, "Timmy");
//		//give student a hardcoded txt file with dummy code
//		File studentTestFile2 = new File("TimmyCode.txt");
//		testStud2.addFile(studentTestFile2);
//		pe.stripFile(testStud2);		
//		//count the keywords in the test student file
//		pe.countKeywords(testStud2);
//		//print the results
//		testStud2.printKeywords();
//		
//		pe.compare(testStud, testStud2);
////		System.out.println("Score for tommy: " + testStud.getScore());
////		
////		System.out.println("Score for timmy: " + testStud2.getScore());
//		
//		for(int i = 0; i < testStud.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Tommy: " + testStud.getRedStudents().get(i).getName());
//		}		
//		
//		for(int i = 0; i < testStud2.getRedStudents().size(); i++) {
//			System.out.println("Students in red with Timmy: " + testStud2.getRedStudents().get(i).getName());
//		}	
	}

}
