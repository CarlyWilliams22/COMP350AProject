
/**
 * @author Nathan Beam
 * @author Tirzah Lloyd
 * @author Matthew Moody
 * @author Carly Williams
 * 
 * Copied Code Catcher Sprint 1 Working Increment
 * 
 */

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
		System.out.println("Authors: Nathan Beam, Tirzah Lloyd, Matthew Moody, Carly Williams");
		System.out.println("Copied Code Catcher Sprint 1 Working Increment");
		launch(args);
	}

	/**
	 * Starts the UI
	 */
	@Override
	public void start(Stage primary) throws Exception {

		renderFileScreen(primary);

		primary.setTitle("Copied Code Catcher 2021");
//		primary.setMaximized(true);
		primary.show();
	}

	/**
	 * Renders the File Upload Screen
	 * @param primary
	 */
	private void renderFileScreen(Stage primary) {

		Label label = new Label("File Upload");
		label.setMinSize(50, 50);
		label.setAlignment(Pos.CENTER);

		TableView uploadTable = new TableView();
		uploadTable.setMinWidth(500);
		uploadTable.setMinHeight(100);
		uploadTable.setEditable(false);

		TableColumn fileColumn = new TableColumn("File");
		fileColumn.setMinWidth(500);
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
		grid.setMinSize(600, 100);
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
			}
		});

		primary.setScene(mainScreen);
	}

	/**
	 * Renders the Students' Results Screen
	 * @param primary
	 */
	private void renderResultsScreen(Stage primary) {

		Label label = new Label("Students' Results");
		label.setMinSize(50, 50);
		label.setAlignment(Pos.CENTER);

		TableView<Student> resultsTable = new TableView<Student>();
		resultsTable.setMinWidth(500);
		resultsTable.setMinHeight(100);
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
				saveResults();
				System.out.println("Results were saved to a CSV file.");
			}
		});

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				fe.deleteFolder();
				fe.deleteFolder();
				System.out.println("\n\n\t<<<NORMAL TERMINATION>>>");
				System.exit(0);
			}
		});

		resultsTable.setItems(getResults());

		primary.setScene(resultsScreen);
	}

	/**
	 * Gets the students' results
	 * @return
	 */
	private ObservableList<Student> getResults() {
		ObservableList<Student> results = FXCollections.observableArrayList();
		for (Student s : pe.getStudents()) {
			results.add(s);
		}
		return results;
	}

	/**
	 * Writes the results to an Excel spreadsheet Code adapted
	 * 
	 * @see https://stackabuse.com/reading-and-writing-csvs-in-java/
	 */
	private void saveResults() {
		try {
			FileWriter writer = new FileWriter("CCCResults.csv");

			// write header info
			writer.append("Student");
			writer.append(",");
			writer.append("ID");
			writer.append(",");
			writer.append("Green");
			writer.append(",");
			writer.append("Yellow");
			writer.append(",");
			writer.append("Red");
			writer.append("\n");

			// write data for each student
			for (Student s : pe.getStudents()) {
				writer.append(s.getName());
				writer.append(",");
				writer.append(String.valueOf(s.getID()));
				writer.append(",");
				writer.append(String.valueOf(s.getGreenNum()));
				writer.append(",");
				writer.append(String.valueOf(s.getYellowNum()));
				writer.append(",");
				writer.append(String.valueOf(s.getRedNum()));
				writer.append("\n");
			}

			// publish
			writer.flush();
			writer.close();

		} catch (IOException e) {
			System.err.println("Error writing results to file.");
		}

	}
}
