import java.io.File;

public class CopiedCodeCatcher {

	public static void main(String[] args) {

		UI ui = new UI();
		
		FolderEngine fe = new FolderEngine();
		File testFile = new File("FileStripTest.txt");
		fe.stripFile(testFile);
		
		

	}

}
