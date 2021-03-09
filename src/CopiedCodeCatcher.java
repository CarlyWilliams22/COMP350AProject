import java.io.File;

public class CopiedCodeCatcher {

	public static void main(String[] args) {

		UI ui = new UI();
		
		PlagiarismEngine pe = new PlagiarismEngine();
		File testFile = new File("FileStripTest.txt");
		pe.stripFile(testFile);
		
		Student testStud = new Student(1, "Tommy");
		//File studentTestFile = new File("TommyCode.txt");
		//testStud.addFile(studentTestFile);
		//pe.tokenize(testStud);
		//testStud.printTokens();

	}

}
