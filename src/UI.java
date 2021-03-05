import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI extends Application {

	private FolderEngine fe;
	private PlagiarismEngine algorithm;

	public UI() {

	}

	@Override
	public void start(Stage primary) throws Exception {
		// TODO Auto-generated method stub
		Group group = new Group();

		Scene scene = new Scene(group, 800, 500); // width x height

		primary.setTitle("Copied Code Catcher 2021");
		primary.setScene(scene);
		primary.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
