
/**
 * @author Nathan Beam
 * @author Tirzah Lloyd
 * @author Matthew Moody
 * @author Carly Williams
 * 
 * Copied Code Catcher Sprint 1 Working Increment
 * 
 */

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UI extends Application {

	// Constants
	private final Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
	private final double WINDOW_WIDTH = window.getWidth();
	private final double WINDOW_HEIGHT = window.getHeight();
	private final double BUTTON_WIDTH = 150;
	private final double BUTTON_HEIGHT = 40;

	private Stage primary;
	private Scene uploadScreen;
	private Scene resultsScreen;
	private FolderEngine fe; // file functionality
	private PlagiarismEngine pe; // algorithm functionality
	private FileChooser explorer;
	private ObservableList<File> files = FXCollections.observableArrayList();

	public UI() {
		fe = new FolderEngine();
		pe = new PlagiarismEngine();
		explorer = new FileChooser();
		explorer.setTitle("File Explorer");
	}

	public static void main(String args[]) {
		System.out.println("Launching...");
		System.out.println("Authors: Nathan Beam, Tirzah Lloyd, Matthew Moody, Carly Williams");
		System.out.println("Copied Code Catcher Sprint 2 Working Increment");

		launch(args);

		System.out.println("Terminating...");
		System.out.println("\n<<< STANDARD TERMINATION >>>");
	}

	@Override
	public void start(Stage stage) throws Exception {

		primary = stage;

		primary.setTitle("Copied Code Catcher");

		renderUploadScreen();
//		renderResultsScreen(primary);

	}

	/**
	 * 
	 */
	private void renderUploadScreen() {

		Label label = new Label();
		label.setText("Upload Files");
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox();
		side.setPrefSize(BUTTON_WIDTH, WINDOW_HEIGHT / 2);
		side.setSpacing(60);
		renderFileButtons();
		side.getChildren().add(label);
		side.getChildren().addAll(renderFileButtons());

		TableView<File> table = new TableView<File>();

		TableColumn<File, String> column = new TableColumn<File, String>("Files");
		column.setMinWidth(300);
		column.setCellValueFactory(new PropertyValueFactory<File, String>("Name"));
		table.getColumns().add(column);
		table.setItems(files);

		BorderPane pane = new BorderPane();
		BorderPane.setMargin(table, new Insets(40, 30, 40, 40));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		pane.setRight(side);
		pane.setCenter(table);

		uploadScreen = new Scene(pane);

		primary.setScene(uploadScreen);
		primary.show();
	}

	/**
	 * 
	 * @return
	 */
	private ObservableList<Button> renderFileButtons() {

		ObservableList<Button> buttons = FXCollections.observableArrayList();

		Button help = new Button();
		help.setText("Help");
		help.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button browseThisPC = new Button();
		browseThisPC.setText("Browse This PC");
		browseThisPC.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button browseOneDrive = new Button();
		browseOneDrive.setText("Browse OneDrive");
		browseOneDrive.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button done = new Button();
		done.setText("Done");
		done.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addFileButtonListeners(help, browseThisPC, browseOneDrive, done);

		buttons.add(help);
		buttons.add(browseThisPC);
		buttons.add(browseOneDrive);
		buttons.add(done);

		return buttons;
	}

	/**
	 * 
	 * @param help
	 * @param browseThisPC
	 * @param browseOneDrive
	 * @param done
	 */
	private void addFileButtonListeners(Button help, Button browseThisPC, Button browseOneDrive, Button done) {

		// Creates popup window
		help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("Help!");
			}
		});

		// Opens File Explorer
		browseThisPC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("PC");

				File file = explorer.showOpenDialog(primary);
				if (file != null) {
					try {
						files.add(file);
						String PATH = file.getCanonicalPath();
						fe.unzipLocally(PATH);
						pe.receiveFiles(fe.transferFiles());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		});

		// Opens OneDrive
		browseOneDrive.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(final ActionEvent event) {
				System.out.println("OneDrive");
			}
		});

		done.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("Done!");
				pe.createStudents();
				pe.parseAllFiles();
				pe.countAllKeywords();
				pe.compareAll();
				renderResultsScreen();
				primary.setScene(resultsScreen);
			}
		});
	}

	/**
	 * 
	 */
	private void renderResultsScreen() {

		Label label = new Label();
		label.setText("Students' Results");
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox();
		side.setPrefSize(BUTTON_WIDTH, WINDOW_HEIGHT / 2);
		side.setSpacing(60);
		renderFileButtons();
		side.getChildren().add(label);
		side.getChildren().addAll(renderResultsButtons());

		TableView<Student> table = new TableView<Student>();

		// Create Results Columns
		TableColumn<Student, String> nameCol = new TableColumn<Student, String>("Name");
		nameCol.setMinWidth(300);
		nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("Name"));

		TableColumn<Student, String> IDCol = new TableColumn<Student, String>("ID");
		IDCol.setMinWidth(100);
		IDCol.setCellValueFactory(new PropertyValueFactory<Student, String>("ID"));

		TableColumn<Student, String> greenCol = new TableColumn<Student, String>("Green");
		greenCol.setMinWidth(100);
		greenCol.setCellValueFactory(new PropertyValueFactory<Student, String>("GreenNum"));

		TableColumn<Student, String> yellowCol = new TableColumn<Student, String>("Yellow");
		yellowCol.setMinWidth(100);
		yellowCol.setCellValueFactory(new PropertyValueFactory<Student, String>("YellowNum"));

		TableColumn<Student, String> redCol = new TableColumn<Student, String>("Red");
		redCol.setMinWidth(100);
		redCol.setCellValueFactory(new PropertyValueFactory<Student, String>("RedNum"));

		table.getColumns().addAll(nameCol, IDCol, greenCol, yellowCol, redCol);

		table.setItems(getClassResults());

		BorderPane pane = new BorderPane();
		BorderPane.setMargin(table, new Insets(40, 30, 40, 40));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		pane.setRight(side);
		pane.setCenter(table);

		resultsScreen = new Scene(pane);

		primary.setScene(uploadScreen);
		primary.show();
	}

	/**
	 * 
	 * @return
	 */
	private ObservableList<Button> renderResultsButtons() {

		ObservableList<Button> buttons = FXCollections.observableArrayList();

		Button help = new Button();
		help.setText("Help");
		help.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button saveToThisPC = new Button();
		saveToThisPC.setText("Save to This PC");
		saveToThisPC.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button uploadToOneDrive = new Button();
		uploadToOneDrive.setText("Upload to OneDrive");
		uploadToOneDrive.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button newProject = new Button();
		newProject.setText("New Project");
		newProject.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addResultsButtonListeners(help, saveToThisPC, uploadToOneDrive, newProject);

		buttons.add(help);
		buttons.add(saveToThisPC);
		buttons.add(uploadToOneDrive);
		buttons.add(newProject);

		return buttons;
	}

	/**
	 * 
	 * @param help
	 * @param saveToThisPC
	 * @param uploadToOneDrive
	 * @param newProject
	 */
	private void addResultsButtonListeners(Button help, Button saveToThisPC, Button uploadToOneDrive,
			Button newProject) {

		// Creates popup window
		help.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("Help!");
			}
		});

		// Opens File Explorer
		saveToThisPC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("PC");
			}
		});

		// Opens OneDrive
		uploadToOneDrive.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("OneDrive");
			}
		});

		// Opens Upload Screen
		newProject.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				System.out.println("New Project!");
				primary.setScene(uploadScreen);
			}
		});
	}

	/**
	 * 
	 */
	private void renderTabPane() {

	}

	/**
	 * 
	 * @return
	 */
	private Tab renderResultsTab() {
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private Tab renderGraphTab() {
		return null;
	}

	/**
	 * 
	 */
	private void renderStudentPopup() {

	}

	/**
	 * 
	 */
	private void renderHelpPopup() {

	}

	/**
	 * 
	 * @return
	 */
	private ObservableList<Student> getClassResults() {
		ObservableList<Student> results = FXCollections.observableArrayList();
		for (Student s : pe.getStudents()) {
			results.add(s);
		}
		return results;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private Student getStudentResults(String name) {
		return null;
	}

	/**
	 * 
	 */
	private void saveResults() {
		// may want to change return value to a boolean for status check
	}

}