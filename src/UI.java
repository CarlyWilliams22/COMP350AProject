
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
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UI extends Application implements EventHandler<KeyEvent> {

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
	private ObservableList<File> errorFiles = FXCollections.observableArrayList();

	public UI() {
		primary = new Stage();
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

		primary.setOnCloseRequest(event -> {
			clear();
		});

		primary.setTitle("Copied Code Catcher");
		renderUploadScreen();
	}

	/**
	 * Creates the upload screen
	 */
	private void renderUploadScreen() {

		TextFlow title = new TextFlow();
		title.setLayoutX(WINDOW_WIDTH / 1.5);
		title.setLayoutY(50);
		title.setTextAlignment(TextAlignment.CENTER);

		title.getChildren().addAll(renderTitleText());

		Label label = new Label(); // screen information
		label.setText("Upload Files");
		label.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 20));
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox(); // holds the buttons on the right side
		side.setPrefSize(BUTTON_WIDTH, WINDOW_HEIGHT / 2);
		side.setSpacing(60);
		side.getChildren().add(label);
		side.getChildren().addAll(renderFileButtons());

		TableView<File> table = new TableView<File>(); // upload directory

		TableColumn<File, String> column = new TableColumn<File, String>("Files");
		column.setMinWidth(400);
		column.setCellValueFactory(new PropertyValueFactory<File, String>("Name"));
		table.getColumns().add(column);
		table.setItems(files);

		BorderPane pane = new BorderPane(); // UI layout
		BorderPane.setMargin(table, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		pane.setRight(side);
		pane.setCenter(table);

		TableView<File> unprocessedTable = new TableView<File>();

		TableColumn<File, String> unprocessedColumn = new TableColumn<File, String>("Could not read files:");
		unprocessedColumn.setMinWidth(WINDOW_WIDTH * .5);
		unprocessedColumn.setCellValueFactory(new PropertyValueFactory<File, String>("Path"));
		unprocessedTable.getColumns().add(unprocessedColumn);
		unprocessedTable.setMaxHeight(100);
		unprocessedTable.setItems(errorFiles);

		VBox frame = new VBox();
		frame.setPrefSize(WINDOW_WIDTH * .5, WINDOW_HEIGHT * .75);
		frame.getChildren().addAll(title, pane, unprocessedTable);

		uploadScreen = new Scene(frame);
		uploadScreen.setOnKeyPressed(this); // adds hot keys listener

		primary.setScene(uploadScreen);
		primary.show();
	}

	/**
	 * Creates the welcome title textS
	 * 
	 * @return text
	 */
	private ObservableList<Text> renderTitleText() {

		ObservableList<Text> title = FXCollections.observableArrayList();

		Text welcome = new Text("  Welcome To ");
		welcome.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 35));
		Text c1 = new Text("C");
		c1.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 35));
		c1.setFill(Color.GREEN);
		c1.setStrokeWidth(.5);
		c1.setStroke(Color.BLACK);
		Text c2 = new Text("C");
		c2.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 35));
		c2.setFill(Color.YELLOW);
		c2.setStrokeWidth(.5);
		c2.setStroke(Color.BLACK);
		Text c3 = new Text("C");
		c3.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 35));
		c3.setFill(Color.RED);
		c3.setStrokeWidth(.5);
		c3.setStroke(Color.BLACK);

		title.addAll(welcome, c1, c2, c3);

		return title;
	}

	/**
	 * Creates buttons for the upload screen
	 * 
	 * @return list of buttons
	 */
	private ObservableList<Button> renderFileButtons() {

		ObservableList<Button> buttons = FXCollections.observableArrayList();

		Button help = new Button();
		help.setText("Help");
		help.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button uploadZips = new Button();
		uploadZips.setText("Upload Zip Folders");
		uploadZips.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button uploadJavaFiles = new Button();
		uploadJavaFiles.setText("Upload Java Files");
		uploadJavaFiles.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button process = new Button();
		process.setText("Process Files");
		process.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addFileButtonListeners(help, uploadZips, uploadJavaFiles, process);

		buttons.addAll(help, uploadZips, uploadJavaFiles, process);

		return buttons;
	}

	/**
	 * 
	 * @param help
	 * @param browseThisPC
	 * @param browseOneDrive
	 * @param done
	 */
	private void addFileButtonListeners(Button help, Button uploadZips, Button uploadJavaFiles, Button process) {

		// Opens Help Popup
		help.setOnAction((event) -> {
			renderHelpPopup();
		});

		// Opens File Explorer
		uploadZips.setOnAction((event) -> {
			explorer.getExtensionFilters().clear();
			explorer.getExtensionFilters().add(new FileChooser.ExtensionFilter("Zip file (*.zip)", "*.zip"));
			uploadZip();
		});

		// Opens File Explorer
		uploadJavaFiles.setOnAction((event) -> {
			explorer.getExtensionFilters().clear();
			explorer.getExtensionFilters().add(new FileChooser.ExtensionFilter("Java file(*.java)", "*.java"));
			uploadJavaFile();
		});

		// Processes Files
		process.setOnAction((event) -> {
			process();
		});
	}

	/**
	 * Uploads selected zip folders for processing
	 */
	private void uploadZip() {

		List<File> folders = explorer.showOpenMultipleDialog(primary);

		if (folders != null) { // if upload directory contains folders
			for (File file : folders) {
				if (file != null) { // if file exists
					try {
						files.add(file); // display selected folder
						String PATH = file.getCanonicalPath(); // get location
						fe.unzipRecursive(PATH, "Storage\\"); // unzip
						pe.receiveFiles(fe.transferFiles()); // send data to plagiarism engine for processing
						ArrayList<File> unprocessedFiles = fe.getUnprocessedFiles();

						for (int i = 0; i < unprocessedFiles.size(); i++) {
							if (!errorFiles.contains(unprocessedFiles.get(i))) {
								errorFiles.add(unprocessedFiles.get(i));
							}
						}
						fe.cleanUpStorageFolder();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				} // if
			} // for
		} // if
	}

	/**
	 * Uploads selected Java files for processing
	 */
	private void uploadJavaFile() {

		List<File> javaFiles = explorer.showOpenMultipleDialog(primary);

		if (javaFiles != null) { // if upload contains Java files
			for (File file : javaFiles) {
				if (file != null) { // if file exists
					files.add(file); // display selected file
//						String PATH = file.getCanonicalPath(); // get location
					fe.uploadJavaFile(file, "Storage\\");
					pe.receiveFiles(fe.transferFiles());
					ArrayList<File> unprocessedFiles = fe.getUnprocessedFiles();

					for (int i = 0; i < unprocessedFiles.size(); i++) {
						if (!errorFiles.contains(unprocessedFiles.get(i))) {
							errorFiles.add(unprocessedFiles.get(i));
						}
					}
				} // if
			} // for
		} // if
	}

	/**
	 * 
	 */
	private void process() {
		System.out.println("Done!");
		pe.createStudents(); // each student receives a file
		pe.parseAllFiles(); // remove comments and excess whitespace
		pe.countAllKeywords(); // check keywords
		pe.findWordUsage();	//find out how frequently each word is used
		pe.assignWeights();	//Dynamically assign the weights
		pe.createScores();	//make the scores for each student
		pe.compareAll(); // compare each student to each other
		renderResultsScreen(); // switch screens
		primary.setScene(resultsScreen); // display results
	}

	/**
	 * Creates the results screen
	 */
	private void renderResultsScreen() {

		Label label = new Label(); // screen information
		label.setText("Students' Results");
		label.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 15));
		label.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		VBox side = new VBox(); // holds buttons on the right side
		side.setPrefSize(BUTTON_WIDTH, WINDOW_HEIGHT / 2);
		side.setSpacing(60);
		renderFileButtons();
		side.getChildren().add(label);
		side.getChildren().addAll(renderResultsButtons());

		TabPane tabs = renderTabPane(); // for table and graph

		BorderPane pane = new BorderPane(); // UI layout
		BorderPane.setMargin(tabs, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH * .7, WINDOW_HEIGHT * .65);
		tabs.setTabMinWidth(200);
		pane.setRight(side);
		pane.setCenter(tabs);

		resultsScreen = new Scene(pane);
		resultsScreen.setOnKeyPressed(this); // add hot keys listener

		primary.setScene(resultsScreen);
		primary.show();
	}

	/**
	 * Creates buttons for the results screen
	 * 
	 * @return list of buttons
	 */
	private ObservableList<Button> renderResultsButtons() {

		ObservableList<Button> buttons = FXCollections.observableArrayList();

		Button help = new Button();
		help.setText("Help");
		help.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button saveResults = new Button();
		saveResults.setText("Save Results");
		saveResults.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button saveGraph = new Button();
		saveGraph.setText("Save Graph");
		saveGraph.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		Button newProject = new Button();
		newProject.setText("New Project");
		newProject.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addResultsButtonListeners(help, saveResults, saveGraph, newProject);

		buttons.add(help);
		buttons.add(saveResults);
		buttons.add(saveGraph);
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
	private void addResultsButtonListeners(Button help, Button saveResults, Button saveGraph, Button newProject) {

		// Creates popup window
		help.setOnAction((event) -> {
			renderHelpPopup();
		});

		// Opens File Explorer
		saveResults.setOnAction((event) -> {
			saveResults();
		});

		saveGraph.setOnAction((event) -> {
			saveGraph();
		});

		// Opens Upload Screen
		newProject.setOnAction((event) -> {
			newProject();
		});
	}

	/**
	 * Saves results to a CSV file.
	 */
	private void saveResults() {
		System.out.println("Saving results...");

		explorer.getExtensionFilters().clear();
		FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv");
		explorer.getExtensionFilters().add(fileExtension);

		File results = explorer.showSaveDialog(primary);

		if (results != null) {
			saveResults(results);
		}
	}

	/**
	 * Saves an image of the UI to a jpg or png
	 */
	private void saveGraph() {
		System.out.println("Saving graph...");

		explorer.getExtensionFilters().clear();
		explorer.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
		explorer.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG file (*.jpg)", "*.jpg"));

		File graph = explorer.showSaveDialog(primary);

		if (graph != null) {
			saveGraph(graph);
		}
	}

	/**
	 * Setups UI for a new project and erases the old project
	 */
	private void newProject() {
		primary.close();
		errorFiles.clear();
		System.out.println("Creating new project...");
		clear();
		primary.setScene(uploadScreen); // switch to upload screen
		primary.show(); // display
	}

	/**
	 * Deletes all data created at runtime.
	 */
	private void clear() {
		files.clear(); // clear uploads
		fe.clearFiles(); // clear files in the folder engine
		fe.cleanUpFoldersCreated(); // erase stored files
		pe.clearFiles(); // clear files in plagiarism engine
		pe.clearStudents(); // clear student data
	}

	/**
	 * Renders the tab pane
	 */
	private TabPane renderTabPane() {
		TabPane tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE); // ensure tabs cannot be lost
		tabs.setStyle("-fx-padding: 0 -1 -1 -1");
		tabs.getTabs().addAll(renderResultsTab(), renderGraphTab()); // populate with tabs
		return tabs;
	}

	/**
	 * Creates the tab with the results table
	 * 
	 * @return - tab with the results table
	 */
	private Tab renderResultsTab() {
		Tab t = new Tab("Results");
		t.setContent(renderResultsTable()); // add table to tab
		return t;
	}

	/**
	 * Creates the results table
	 * 
	 * @return the results table
	 */
	private TableView<Student> renderResultsTable() {
		TableView<Student> table = new TableView<Student>();

		// Create Results Columns
		TableColumn<Student, String> nameCol = new TableColumn<Student, String>("Name");
		nameCol.setMinWidth(257);
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

		table.setItems(getClassResults()); // populate table if data exists

		// notify user of suspect students by sorting by red counts
		redCol.setSortable(true);
		redCol.setSortType(TableColumn.SortType.DESCENDING);
		table.getSortOrder().add(redCol);
		table.sort();

		addTableSelection(table); // enables student popup

		return table;
	}

	/**
	 * Enables table values to be clicked and triggers a pop up window
	 * 
	 * @param table
	 */
	private void addTableSelection(TableView<Student> table) {
		table.setRowFactory(tv -> {
			TableRow<Student> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (!row.isEmpty()) {
					Student rowData = row.getItem(); // get the student's information
					renderStudentPopup(rowData.getName()); // display individual student data
				}
			});
			return row;
		});
	}

	/**
	 * Creates the tab with the graph
	 * 
	 * @return
	 */
	private Tab renderGraphTab() {
		Tab t = new Tab("Graph");
		ScrollPane scroll = new ScrollPane();
//		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setContent(renderGraph()); // add graph to tab
		t.setContent(scroll);
		return t;
	}

	/**
	 * Creates a graph view of the results
	 * 
	 * @return
	 */
	private BarChart<Number, String> renderGraph() {
		NumberAxis x = new NumberAxis();
		CategoryAxis y = new CategoryAxis();

		BarChart<Number, String> graph = new BarChart<Number, String>(x, y);
		graph.setTitle("Class Results");
		x.setLabel("Number of Files");
		y.setLabel("Students");

		XYChart.Series<Number, String> red = new Series<Number, String>();
		red.setName("Red");

		XYChart.Series<Number, String> yellow = new Series<Number, String>();
		yellow.setName("Yellow");

		XYChart.Series<Number, String> green = new Series<Number, String>();
		green.setName("Green");

		ArrayList<Student> students = pe.getStudents(); // gets data

		for (Student s : students) {
			red.getData().add(new Data<Number, String>(s.getRedNum(), s.getName()));
			yellow.getData().add(new Data<Number, String>(s.getYellowNum(), s.getName()));
			green.getData().add(new Data<Number, String>(s.getGreenNum(), s.getName()));
		}

		graph.getData().add(red);
		graph.getData().add(yellow);
		graph.getData().add(green);
		graph.setHorizontalGridLinesVisible(false); // removes unncessary lines
		graph.setMinHeight(students.size() * 60); // adapts height to the content of the graph

		return graph;
	}

	/**
	 * Creates a pop up window of the student's similarity scores
	 */
	private void renderStudentPopup(String name) {
		Stage popup = new Stage();
		popup.setTitle(name);
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primary);

		BackgroundFill bg_fill = new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10), null);
		Background bg = new Background(bg_fill);

		Label studentName = new Label("  " + name + " ");
		studentName.setMinSize(325, 50);
		studentName.setBackground(bg);
		studentName.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 25));

		// Set up the text for the red students
		Text redTitle = new Text("Red");
		redTitle.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 15));
		redTitle.setUnderline(true);
		redTitle.setFill(Color.RED);
		String veryRedStudentsTxt = getVeryRedStudents(name);
		Text veryRedStudents = null;
		if (veryRedStudentsTxt != null) {
			veryRedStudents = new Text();
			veryRedStudents.setFill(Color.RED);
		}
		Text redStudents = new Text(getRedStudents(name));

		// Set up the text for the yellow students
		Text yellowTitle = new Text("\nYellow");
		yellowTitle.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 15));
		yellowTitle.setUnderline(true);
		yellowTitle.setFill(Color.YELLOW);
		yellowTitle.setStrokeWidth(0.5);
		yellowTitle.setStroke(Color.BLACK);
		Text yellowStudents = new Text(getYellowStudents(name));

		// Set up the text for the green students
		Text greenTitle = new Text("\nGreen");
		greenTitle.setFont(Font.font("Bookman Old Style", FontWeight.BOLD, FontPosture.REGULAR, 15));
		greenTitle.setUnderline(true);
		greenTitle.setFill(Color.GREEN);
		Text greenStudents = new Text(getGreenStudents(name));

		VBox textBoxes = new VBox();
		if (veryRedStudentsTxt == null) {
			textBoxes.getChildren().addAll(studentName, redTitle, redStudents, yellowTitle, yellowStudents, greenTitle,
					greenStudents);
		} else {
			textBoxes.getChildren().addAll(studentName, redTitle, veryRedStudents, redStudents, yellowTitle,
					yellowStudents, greenTitle, greenStudents);

		}
		textBoxes.setSpacing(10);
		textBoxes.setPadding(new Insets(10));
		ScrollPane sp = new ScrollPane(textBoxes);
		Scene dialogScene = new Scene(sp, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 3);

		popup.setScene(dialogScene);
		popup.show();
	}

	/**
	 * Creates a pop up window with instructions
	 */
	private void renderHelpPopup() {
		Stage popup = new Stage();
		popup.setTitle("Help");
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primary);

		Text message = new Text("Upload Zip Folders\n" + "Select one or more zip folders for unzipping.\n\n"
				+ "Upload Java Files\n" + "Select one or more Java files to upload.\n\n" + "Process Files\n"
				+ "Analyze students’ work for plagiarism.\n\n" + "Save Results\n"
				+ "Saves results table to an Excel spreadsheet.\n\n" + "Save Graph\n"
				+ "Select the Graph tab before clicking the button.\n" + "\n" + "New Project\n"
				+ "Upload a new batch of student projects.\n" + "\n" + "Hot Keys\n" + "Ctrl + H – help\n"
				+ "Ctrl + N – start a new project\n" + "Ctrl + G – save graph\n" + "Ctrl + S – save results\n"
				+ "Ctrl + W – exit\n" + "Ctrl + R – restart");

		ScrollPane scroll = new ScrollPane();
		scroll.setPadding(new Insets(20, 20, 20, 20));
		scroll.setContent(message);

		Scene dialogScene = new Scene(scroll, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4);

		popup.setScene(dialogScene);
		popup.show();
	}

	/**
	 * Retreives data from each student
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
	private String getVeryRedStudents(String name) {
		Student s = pe.getStudent(name);
		Map<String, Double> comparisons = s.getCompScores();
		String results = "";

		for (Student i : s.getRedStudents()) {
			if (Math.abs(100 - comparisons.get(i.getName())) < .00001) {
				results += String.format("%.2f%% - %s\n", comparisons.get(i.getName()) * 100, i.getName());
			}
		}

		if (results.equals("")) {
			return null;
		}

		return results;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private String getRedStudents(String name) {
		Student s = pe.getStudent(name);
		Map<String, Double> comparisons = s.getCompScores();
		String results = "";

		for (Student i : s.getRedStudents()) {
			if (Math.abs(100 - comparisons.get(i.getName())) > .00001) {
				results += String.format("%.2f%% - %s\n", comparisons.get(i.getName()) * 100, i.getName());
			}
		}

		return results;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private String getYellowStudents(String name) {
		Student s = pe.getStudent(name);
		Map<String, Double> comparisons = s.getCompScores();
		String results = "";

		for (Student i : s.getYellowStudents()) {
			results += String.format("%.2f%% - %s\n", comparisons.get(i.getName()) * 100, i.getName());
		}

		return results;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	private String getGreenStudents(String name) {
		Student s = pe.getStudent(name);
		Map<String, Double> comparisons = s.getCompScores();
		String results = "";

		for (Student i : s.getGreenStudents()) {
			results += String.format("%.2f%% - %s\n", comparisons.get(i.getName()) * 100, i.getName());
		}

		return results;
	}

	/**
	 * Saves an excel spreadsheet of the results table. TODO add IO errors,
	 * processing errors
	 */
	private void saveResults(File file) {
		try {
			FileWriter writer = new FileWriter(file);
			Map<String, Double> studentResults;

			// Write student information header
			for (Student s : pe.getStudents()) {
				writer.append("Student:," + s.getName().toUpperCase() + "\n");
				studentResults = s.getCompScores();
				//Print green info header
				writer.append("GREEN:," + s.getGreenNum() + "\n");
				//print out the green students and their scores
				for (Student s2 : s.getGreenStudents()) {
					writer.append("," + s2.getName() + "," + studentResults.get(s2.getName()) + "\n");
				}
				//Print yellow info header
				writer.append("YELLOW:," + s.getYellowNum() + "\n");
				//print out the yellow students and their scores
				for (Student s2 : s.getYellowStudents()) {
					writer.append("," + s2.getName() + "," + studentResults.get(s2.getName()) + "\n");
				}
				//Print red info header
				writer.append("RED:," + s.getRedNum() + "\n");
				//print out the red students and their scores
				for (Student s2 : s.getRedStudents()) {
					writer.append("," + s2.getName() + "," + studentResults.get(s2.getName()) + "\n");
				}
				writer.append("\n");

			}
			
			//print out any unprocessed files
			if (!errorFiles.isEmpty()) {
				writer.append("FAILED TO PROCESS THESE FILES:\n");
				for (File f : errorFiles) {
					writer.append(f.getPath() + "\n");
				}
			}

			// finish
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Saves screenshot of graph tab
	 */
	private File saveGraph(File file) {
		WritableImage image = resultsScreen.snapshot(null);

		try {
			System.out.println("Saving image...");
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Hot Keys WIP
	 */
	@Override
	public void handle(KeyEvent e) {
		if (e.getCode() == e.getCode().W && e.isControlDown()) {
			clear();
			System.out.println("Terminating...");
			System.out.println("\n<<< STANDARD TERMINATION >>>");
			System.exit(0);
		}

		else if (e.getCode() == e.getCode().H && e.isControlDown()) {
			renderHelpPopup();
		}

		else if (e.getCode() == e.getCode().N && e.isControlDown()) {
			if (primary.getScene().equals(resultsScreen)) {
				newProject();
			}
		}

		else if (e.getCode() == e.getCode().R && e.isControlDown()) {
			System.out.println("Restarting application...");
			newProject();
		}

		else if (e.getCode() == e.getCode().G && e.isControlDown()) {
			if (primary.getScene().equals(resultsScreen)) {
				saveGraph();
			}
		}

		else if (e.getCode() == e.getCode().S && e.isControlDown()) {
			if (primary.getScene().equals(resultsScreen)) {
				saveResults();
			}
		}
	} // handle
}