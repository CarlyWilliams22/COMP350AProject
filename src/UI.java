
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
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI extends Application {

	// Constants
	private final Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
	private final double WINDOW_WIDTH = window.getWidth();
	private final double WINDOW_HEIGHT = window.getHeight();
	private final double BUTTON_WIDTH = 150;
	private final double BUTTON_HEIGHT = 40;

	private FolderEngine fe; // file functionality
	private PlagiarismEngine pe; // algorithm functionality

	public UI() {
		fe = new FolderEngine();
		pe = new PlagiarismEngine();
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
	public void start(Stage primary) throws Exception {

		primary.setTitle("Copied Code Catcher");

//		renderUploadScreen(primary);
		renderResultsScreen(primary);

	}

	/**
	 * 
	 */
	private void renderUploadScreen(Stage stage) {

		Label label = new Label();
		label.setText("Upload Files");
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox();
		side.setPrefSize(BUTTON_WIDTH, 400);
		side.setSpacing(30);
		renderFileButtons();
		side.getChildren().add(label);
		side.getChildren().addAll(renderFileButtons());

		TableView<File> table = new TableView<File>();
		table.setPrefSize(WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4);
		table.setVisible(true);

		BorderPane pane = new BorderPane();
		BorderPane.setMargin(table, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
		pane.setMaxSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		pane.setRight(side);
		pane.setCenter(table);

		Scene uploadScreen = new Scene(pane);

		stage.setScene(uploadScreen);
		stage.show();
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
			}
		});
	}

	/**
	 * 
	 */
	private void renderResultsScreen(Stage stage) {

		Label label = new Label();
		label.setText("Students' Results");
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox();
		side.setPrefSize(BUTTON_WIDTH, 400);
		side.setSpacing(30);
		renderFileButtons();
		side.getChildren().add(label);
		side.getChildren().addAll(renderResultsButtons());

		TableView<File> table = new TableView<File>();
		table.setPrefSize(WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4);
		table.setVisible(true);

		BorderPane pane = new BorderPane();
		BorderPane.setMargin(table, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 3, WINDOW_HEIGHT / 3);
		pane.setMaxSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		pane.setRight(side);
		pane.setCenter(table);

		Scene uploadScreen = new Scene(pane);

		stage.setScene(uploadScreen);
		stage.show();
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

	private void addResultsButtonListeners(Button help, Button saveToThisPC, Button uploadToOneDrive, Button newProject) {
		
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
		return null;

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