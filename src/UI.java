import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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

		renderFileScreen(primary);

		primary.setTitle("Copied Code Catcher 2021");
		primary.setMaximized(true);
		primary.show();
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

	private void renderFileScreen(Stage primary) {

		Label label = new Label("Copied Code Catcher");
		label.setMinSize(50, 50);
		label.setAlignment(Pos.CENTER);

		TableView uploadTable = new TableView();
		uploadTable.setMinWidth(200);
		uploadTable.setMinHeight(200);
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
						fe.unzipRecursively(PATH);
						pe.receiveFiles(fe.transferFiles());
						pe.printFiles();
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
				pe.stripAll();
				pe.allStudentKeywords();
				pe.compareAll();
				renderResultsScreen(primary);
				System.out.println("\n\nDone!");
			}
		});

		primary.setScene(mainScreen);
	}

	private void renderResultsScreen(Stage primary) {

		Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();

		Label resultsLabel = new Label("Students' Results");
		resultsLabel.setFont(new Font("Arial", 20));

		HBox resultsHBox = new HBox();
		resultsHBox.getChildren().add(resultsLabel);
		resultsHBox.setAlignment(Pos.BASELINE_CENTER);

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
		greenCol.setCellValueFactory(new PropertyValueFactory<Student, String>("GreenNum"));

		TableColumn yellowCol = new TableColumn("Yellow");
		yellowCol.setMinWidth(screenSize.getMaxX() / 5);
		yellowCol.setCellValueFactory(new PropertyValueFactory<Student, String>("YellowNum"));

		TableColumn redCol = new TableColumn("Red");
		redCol.setMinWidth(screenSize.getMaxX() / 5);
		redCol.setCellValueFactory(new PropertyValueFactory<Student, String>("RedNum"));

		resultsTable.setEditable(false);
		resultsTable.getColumns().addAll(nameCol, IDCol, greenCol, yellowCol, redCol);

		Button save = new Button("Save to Computer");
		save.setMinSize(100, 25);
		Button exit = new Button("Exit");
		exit.setMinSize(100, 25);

		VBox resultsVBox = new VBox();
		resultsVBox.getChildren().addAll(resultsLabel, resultsTable);
		resultsVBox.setAlignment(Pos.CENTER);

		HBox resultsButtons = new HBox();
		resultsButtons.setSpacing(50);
		resultsButtons.setAlignment(Pos.CENTER);
		resultsButtons.getChildren().addAll(save, exit);

		BorderPane resultsPane = new BorderPane();
		resultsPane.setTop(resultsHBox);
		resultsPane.setCenter(resultsVBox);
		resultsPane.setBottom(resultsButtons);

		Scene resultsScreen = new Scene(resultsPane);

		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {

				// save
			}
		});

		exit.setOnAction(e -> System.exit(0));

		resultsTable.setItems(getResults());

		primary.setScene(resultsScreen);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
