import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UI extends Application {

	private FolderEngine fe;
	private PlagiarismEngine pe;
	private TableView<Student> table = new TableView<Student>();

	public UI() {
		fe = new FolderEngine();
		pe = new PlagiarismEngine();
	}

	@Override
	public void start(Stage primary) throws Exception {

		// Screen Dimensions
		Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

		// Labels
		Label fileLabel = new Label("Uploaded Files");
		fileLabel.setFont(new Font("Arial", 20));

		Label resultsLabel = new Label("Students' Results");

		// Table for File Output
		TableView fileTable = new TableView();
		TableColumn fileNameColumn = new TableColumn("File");
		fileTable.setEditable(false);
		fileTable.getColumns().add(fileNameColumn);

		TableView<Student> resultsTable = new TableView<Student>();

		// Create Results Columns
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setMinWidth(screenSize.getMaxX() / 5);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));

		TableColumn IDCol = new TableColumn("ID");
		IDCol.setMinWidth(screenSize.getMaxX() / 5);
		IDCol.setCellValueFactory(new PropertyValueFactory<Student, String>("ID"));

		TableColumn greenCol = new TableColumn("Green");
		greenCol.setMinWidth(screenSize.getMaxX() / 5);
		greenCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Green"));

		TableColumn yellowCol = new TableColumn("Yellow");
		yellowCol.setMinWidth(screenSize.getMaxX() / 5);
		yellowCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Yellow"));

		TableColumn redCol = new TableColumn("Red");
		redCol.setMinWidth(screenSize.getMaxX() / 5);
		redCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Red"));

		resultsTable.setEditable(false);
		resultsTable.getColumns().addAll(nameCol, IDCol, greenCol, yellowCol, redCol);

		resultsTable.setItems(getResults());

		Button upload = new Button("Upload Files");
		Button process = new Button("Process Files");

		Button save = new Button("Save to Computer");
		Button exit = new Button("Exit");

		VBox fileVBox = new VBox();
		fileVBox.setSpacing(10);
		fileVBox.setPadding(new Insets(10, 0, 0, 10));
		fileVBox.getChildren().addAll(fileLabel, fileTable);

		HBox fileButtons = new HBox();
		fileButtons.getChildren().addAll(upload, process);

		HBox resultsButtons = new HBox();
		resultsButtons.getChildren().addAll(save, exit);

		BorderPane filePane = new BorderPane();
		filePane.setTop(fileLabel);
		filePane.setCenter(fileVBox);
		filePane.setBottom(fileButtons);

		BorderPane resultsPane = new BorderPane();
		resultsPane.setTop(resultsLabel);
		resultsPane.setCenter(resultsTable);
		resultsPane.setBottom(resultsButtons);

		Scene mainScreen = new Scene(filePane); // width x height
		Scene resultsScreen = new Scene(resultsPane);

		FileChooser fileExplorer = new FileChooser();
		fileExplorer.setTitle("File Explorer");

		upload.setOnAction(new EventHandler<ActionEvent>() { // runs File Explorer
			@Override
			public void handle(final ActionEvent event) {
				File file = fileExplorer.showOpenDialog(primary);
				if (file != null) {
					try {
						String PATH = file.getCanonicalPath();
						fe.unzipLocally(PATH);
						pe.receiveFiles(fe.transferFiles());
						pe.printFiles();
//						fe.printFiles();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		process.setOnAction(new EventHandler<ActionEvent>() { // runs File Explorer
			@Override
			public void handle(final ActionEvent event) {
				pe.createStudents();
				pe.printStudents();
				primary.setScene(resultsScreen);
//				primary.setScene(resultsScreen);
				// parse comments
				// parse whitespace
				// tokenize
				// compare
			}
		});

		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// save
			}
		});

		exit.setOnAction(e -> System.exit(0));

		primary.setMaximized(true);
		primary.setTitle("Copied Code Catcher 2021");
		primary.setScene(mainScreen);
		primary.show();
	}

	private ObservableList<Student> getResults() {
		ObservableList<Student> results = null;
		for (Student s : pe.getStudents()) {
			results.add(s);
		}

		return results;
	}

	public static void main(String[] args) {
		launch(args);
	}

}
