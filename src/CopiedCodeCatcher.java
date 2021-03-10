import java.io.File;

public class CopiedCodeCatcher {

	public static void main(String[] args) {

		UI ui = new UI();
		
		//create testing plagiarism engine
		PlagiarismEngine pe = new PlagiarismEngine();
		//use hard coded text file to check strip method
		//results found in file called "strippedSub.txt"
		File testFile = new File("FileStripTest.txt");
		pe.stripFile(testFile);
		
//		//create test student
//		Student testStud = new Student(1, "Tommy");
//		//give student a hardcoded txt file with dummy code
//		File studentTestFile = new File("TommyCode.txt");
//		testStud.addFile(studentTestFile);
//		//count the keywords in the test student file
//		pe.countKeywords(testStud);
//		//print the results
//		testStud.printKeywords();

	}

}
