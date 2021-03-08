import java.io.File;

public class CopiedCodeCatcher {

	public static void main(String[] args) {

		UI ui = new UI();
		
		PlagiarismEngine pe = new PlagiarismEngine();
		File testFile = new File("FileStripTest.txt");
		pe.stripFile(testFile);
		
		

	}

}
