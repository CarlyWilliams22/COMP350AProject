import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UI extends Application {

	private FolderEngine fe;
	private PlagiarismEngine pe;
	private TableView<Student> table = new TableView<Student>();

	public UI() {
		fe = new FolderEngine();
		pe = new PlagiarismEngine();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * 
	 */
	@Override
	public void start(Stage primary) throws Exception {

		renderFileScreen(primary);

		primary.setTitle("Copied Code Catcher 2021");
//		primary.setMaximized(true);
		primary.show();
	}

	/**
	 * 
	 * @param primary
	 */
	private void renderFileScreen(Stage primary) {

		Label label = new Label("Copied Code Catcher");
		label.setMinSize(50, 50);
		label.setAlignment(Pos.CENTER);

		TableView uploadTable = new TableView();
		uploadTable.setMinWidth(500);
		uploadTable.setMinHeight(500);
		uploadTable.setEditable(false);

		TableColumn fileColumn = new TableColumn("File");
		fileColumn.setMinWidth(100);
		fileColumn.setCellValueFactory(new PropertyValueFactory<File, String>("Name"));
		uploadTable.getColumns().add(fileColumn);

		Button upload = new Button("Upload");
		upload.setMinSize(100, 25);

		Button process = new Button("Process");
		process.setMinSize(100, 25);

		HBox fileButtons = new HBox();
		fileButtons.setSpacing(50);
		fileButtons.setAlignment(Pos.CENTER);
		fileButtons.getChildren().addAll(upload, process);

		GridPane grid = new GridPane();
		grid.setMinSize(600, 300);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);

		grid.add(label, 0, 0);
		grid.add(uploadTable, 0, 1);
		grid.add(fileButtons, 0, 2);

		Scene mainScreen = new Scene(grid);

		FileChooser fileExplorer = new FileChooser();
		fileExplorer.setTitle("File Explorer");

		upload.setOnAction(new EventHandler<ActionEvent>() { // runs File Explorer
			@Override
			public void handle(final ActionEvent event) {
				File file = fileExplorer.showOpenDialog(primary);
				if (file != null) {
					try {
						ObservableList<File> files = FXCollections.observableArrayList();
						files.add(file);
						uploadTable.setItems(files);

						String PATH = file.getCanonicalPath();
						fe.unzipLocally(PATH);
						pe.receiveFiles(fe.transferFiles());
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
				pe.parseAllFiles();
				pe.countAllKeywords();
				fe.deleteFolder();
				pe.compareAll();
				renderResultsScreen(primary);
				System.out.println("\n\nDone!");
			}
		});

		primary.setScene(mainScreen);
	}

	/**
	 * 
	 * @param primary
	 */
	private void renderResultsScreen(Stage primary) {

		Label label = new Label("Student Results");
		label.setMinSize(50, 50);
		label.setAlignment(Pos.CENTER);

		TableView<Student> resultsTable = new TableView<Student>();
		resultsTable.setMinWidth(500);
		resultsTable.setMinHeight(500);
		resultsTable.setEditable(false);

		// Create Results Columns
		TableColumn nameCol = new TableColumn("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));

		TableColumn IDCol = new TableColumn("ID");
		IDCol.setMinWidth(100);
		IDCol.setCellValueFactory(new PropertyValueFactory<Student, String>("ID"));

		TableColumn greenCol = new TableColumn("Green");
		greenCol.setMinWidth(100);
		greenCol.setCellValueFactory(new PropertyValueFactory<Student, String>("GreenNum"));

		TableColumn yellowCol = new TableColumn("Yellow");
		yellowCol.setMinWidth(100);
		yellowCol.setCellValueFactory(new PropertyValueFactory<Student, String>("YellowNum"));

		TableColumn redCol = new TableColumn("Red");
		redCol.setMinWidth(100);
		redCol.setCellValueFactory(new PropertyValueFactory<Student, String>("RedNum"));

		resultsTable.setEditable(false);
		resultsTable.getColumns().addAll(nameCol, IDCol, greenCol, yellowCol, redCol);

		Button save = new Button("Save to Computer");
		save.setMinSize(100, 25);

		Button exit = new Button("Exit");
		exit.setMinSize(100, 25);

		HBox fileButtons = new HBox();
		fileButtons.setSpacing(50);
		fileButtons.setAlignment(Pos.CENTER);
		fileButtons.getChildren().addAll(save, exit);

		GridPane grid = new GridPane();
		grid.setMinSize(600, 300);
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setAlignment(Pos.CENTER);

		grid.add(label, 0, 0);
		grid.add(resultsTable, 0, 1);
		grid.add(fileButtons, 0, 2);
		Scene resultsScreen = new Scene(grid);

		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				// save
			}
		});

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				fe.deleteFolder();
				fe.deleteFolder();
				System.exit(0);
			}
		});

		resultsTable.setItems(getResults());

		primary.setScene(resultsScreen);
	}

	/**
	 * 
	 * @return
	 */
	private ObservableList<Student> getResults() {
		ObservableList<Student> results = FXCollections.observableArrayList();
		for (Student s : pe.getStudents()) {
			results.add(s);
		}
		return results;
	}

	private static void writeToTextFile(String fileName, ObservableList<Student> students) throws IOException {
		FileWriter myWriter = new FileWriter(fileName);
		for (Student student : students) {
			myWriter.write(student.getName() + ", " + student.getID() + ", " + student.getGreenNum() + ", "
					+ student.getYellowNum() + ", " + student.getRedNum() + "\n");
		}
		myWriter.close();
	}

}
