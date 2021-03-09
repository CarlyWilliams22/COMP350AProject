
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UI extends Application {

	private FolderEngine fe;
	private PlagiarismEngine algorithm;

	public UI() {

	}

	@Override
	public void start(Stage primary) throws Exception {

		Label label = new Label("Uploaded Files");
		label.setFont(new Font("Arial", 20));

		// Table for File Output
		TableColumn fileNameColumn = new TableColumn("File");
		TableView table = new TableView();
		table.setEditable(false);
		table.getColumns().add(fileNameColumn);

		Button upload = new Button("Upload Files");
		Button process = new Button("Process Files");

		FileChooser fileExplorer = new FileChooser();
		fileExplorer.setTitle("File Explorer");

		upload.setOnAction(new EventHandler<ActionEvent>() { // runs File Explorer
			@Override
			public void handle(final ActionEvent e) {
				File file = fileExplorer.showOpenDialog(primary);
				if (file != null) {
					openFile(file);
				}
			}
		});

		HBox buttons = new HBox();
		buttons.getChildren().addAll(upload, process);

		BorderPane pane = new BorderPane();
		pane.setTop(label);
		pane.setCenter(table);
		pane.setBottom(buttons);

		Group group = new Group();
		group.getChildren().add(pane);

		Scene scene = new Scene(group, 800, 500); // width x height

		primary.setMaximized(true);
		primary.setTitle("Copied Code Catcher 2021");
		primary.setScene(scene);
		primary.show();
	}

	/**
	 * Open File
	 * 
	 * @param file
	 * @see Adapted from
	 *      https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
	 */
	private void openFile(File file) {
		try {

			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);

		} catch (IOException ex) {
			Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
