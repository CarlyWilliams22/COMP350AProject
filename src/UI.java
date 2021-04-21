
/**
 * @author Nathan Beam
 * @author Tirzah Lloyd
 * @author Matthew Moody
 * @author Carly Williams
 * 
 * Copied Code Catcher Sprint 1 Working Increment
 * 
 */

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

//import com.sun.security.sasl.ServerFactoryImpl;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

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

		primary.setTitle("Copied Code Catcher");

		renderUploadScreen();
//		renderScreen(primary);

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
		BorderPane.setMargin(table, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		pane.setRight(side);
		pane.setCenter(table);

		uploadScreen = new Scene(pane);
		uploadScreen.setOnKeyPressed(this);

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

		Button process = new Button();
		process.setText("Process");
		process.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addFileButtonListeners(help, browseThisPC, browseOneDrive, process);

		buttons.add(help);
		buttons.add(browseThisPC);
		buttons.add(browseOneDrive);
		buttons.add(process);
    
		return buttons;
	}

	/**
	 * 
	 * @param help
	 * @param browseThisPC
	 * @param browseOneDrive
	 * @param done
	 */
	private void addFileButtonListeners(Button help, Button browseThisPC, Button browseOneDrive, Button process) {

		// Opens Help Popup
		help.setOnAction((event) -> {
			renderHelpPopup();
		});

		// Opens File Explorer
		browseThisPC.setOnAction((event) -> {
			upload();
		});

		// Opens OneDrive
		browseOneDrive.setOnAction((event) -> {
			upload();
		});

		process.setOnAction((event) -> {
			process();
		});
	}

	/**
	 * 
	 */
	private void upload() {
		List<File> folders = explorer.showOpenMultipleDialog(primary);

		if (folders != null) {
			for (File file : folders) {
				if (file != null) {
					try {
						files.add(file);
						String PATH = file.getCanonicalPath();
						fe.unzipRecursive(PATH, "Storage\\");
						pe.receiveFiles(fe.transferFiles());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 
	 */
	private void process() {
		System.out.println("Done!");
		pe.createStudents();
		pe.parseAllFiles();
		pe.countAllKeywords();
		pe.compareAll();
		renderResultsScreen();
		primary.setScene(resultsScreen);
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

		TabPane tabs = renderTabPane();

		BorderPane pane = new BorderPane();
		BorderPane.setMargin(tabs, new Insets(30, 30, 30, 30));
		BorderPane.setMargin(side, new Insets(30, 30, 30, 0));
		pane.setPrefSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT / 2);
		tabs.setTabMinWidth(200);
		pane.setRight(side);
		pane.setCenter(tabs);

		resultsScreen = new Scene(pane);
		resultsScreen.setOnKeyPressed(this);

		primary.setScene(resultsScreen);
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
		
		Button saveGraph = new Button();
		saveGraph.setText("Save Graph");
		saveGraph.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		addResultsButtonListeners(help, saveToThisPC, uploadToOneDrive, newProject, saveGraph);

		buttons.add(help);
		buttons.add(saveToThisPC);
		buttons.add(uploadToOneDrive);
		buttons.add(newProject);
		buttons.add(saveGraph);

		return buttons;
	}

	/**
	 * 
	 * @param help
	 * @param saveToThisPC
	 * @param uploadToOneDrive
	 * @param newProject
	 * @param saveGraph 
	 */
	private void addResultsButtonListeners(Button help, Button saveToThisPC, Button uploadToOneDrive,
			Button newProject, Button saveGraph) {

		// Creates popup window
		help.setOnAction((event) -> {
			renderHelpPopup();
		});

		// Opens File Explorer
		
		saveGraph.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(final ActionEvent event) {
		//		saveTest(resultsScreen);
			}
      
		saveToThisPC.setOnAction((event) -> {
			System.out.println("Save to ThisPC.");
    	FileChooser.ExtensionFilter exFiller = new FileChooser.ExtensionFilter("CSV file (*.csv)", "*.csv");
			explorer.getExtensionFilters().add(exFiller);
			File savedResults = explorer.showSaveDialog(primary);
				
			if(savedResults != null) {
					saveResults(savedResults);
			}
		});

		// Opens OneDrive
		uploadToOneDrive.setOnAction((event) -> {
			System.out.println("Upload to OneDrive");
		});

		// Opens Upload Screen
		newProject.setOnAction((event) -> {
			newProject();
		});
	}

	/**
	 * 
	 */
	private void newProject() {
		primary.close();
		System.out.println("Creating new project...");
		files.clear();
		fe.clearFiles();
		pe.clearFiles();
		pe.clearStudents();
		primary.setScene(uploadScreen);
		primary.show();
	}

	/**
	 * 
	 */
	private TabPane renderTabPane() {
		TabPane tabs = new TabPane();
		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		tabs.setStyle("-fx-padding: 0 -1 -1 -1");
		tabs.getTabs().addAll(renderResultsTab(), renderGraphTab());
		return tabs;
	}

	/**
	 * 
	 * @return
	 */
	private Tab renderResultsTab() {
		Tab t = new Tab("Results");
		t.setContent(renderResultsTable());
		return t;
	}

	/**
	 * 
	 * @return
	 */
	private TableView renderResultsTable() {
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

		addTableSelection(table);

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
					Student rowData = row.getItem();
					renderStudentPopup(rowData.getName());
				}
			});
			return row;
		});
	}

	/**
	 * 
	 * @return
	 */
	private Tab renderGraphTab() {
		Tab t = new Tab("Graph");
		ScrollPane scroll = new ScrollPane();
		scroll.setFitToHeight(true);
		scroll.setFitToWidth(true);
		scroll.setContent(renderGraph());
		t.setContent(scroll);
		return t;
	}

	/**
	 * 
	 * @return
	 */
	private BarChart renderGraph() {
		NumberAxis x = new NumberAxis();
		CategoryAxis y = new CategoryAxis();

		BarChart<Number, String> chart = new BarChart<Number, String>(x, y);
//		chart.setTitle("Students");
		x.setLabel("Files");
		y.setLabel("Students");

		XYChart.Series<Number, String> red = new Series<Number, String>();
		red.setName("Red");

		XYChart.Series<Number, String> yellow = new Series<Number, String>();
		yellow.setName("Yellow");

		XYChart.Series<Number, String> green = new Series<Number, String>();
		green.setName("Green");

		for (Student s : pe.getStudents()) {
			red.getData().add(new Data<Number, String>(s.getRedNum(), s.getName()));
			yellow.getData().add(new Data<Number, String>(s.getYellowNum(), s.getName()));
			green.getData().add(new Data<Number, String>(s.getGreenNum(), s.getName()));
		}

		chart.getData().add(red);
		chart.getData().add(yellow);
		chart.getData().add(green);
		chart.setHorizontalGridLinesVisible(false);

		
		return chart;
	}

	/**
	 * Creates a pop up window of the student's similarity scores
	 */
	private void renderStudentPopup(String name) {
		Stage popup = new Stage();
		popup.setTitle(name);
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initOwner(primary);

		Text message = new Text(getStudentResults(name));

		ScrollPane scroll = new ScrollPane();
		scroll.setPadding(new Insets(20, 20, 20, 20));
		scroll.setContent(message);

		Scene dialogScene = new Scene(scroll, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4);

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

		Text message = new Text("Hello, World!");

		ScrollPane scroll = new ScrollPane();
		scroll.setPadding(new Insets(20, 20, 20, 20));
		scroll.setContent(message);

		Scene dialogScene = new Scene(scroll, WINDOW_WIDTH / 4, WINDOW_HEIGHT / 4);

		popup.setScene(dialogScene);
		popup.show();
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
	private String getStudentResults(String name) {
		Student s = pe.getStudent(name);
		String results = "";

		results += "\nRed\n";
		for (Student i : s.getRedStudents()) {
			results += i.getName() + ": " + i.getScore() + "\n";
		}

		results += "\nYellow\n";
		for (Student i : s.getYellowStudents()) {
			results += i.getName() + ": " + i.getScore() + "\n";
		}

		results += "\nGreen\n";
		for (Student i : s.getGreenStudents()) {
			results += i.getName() + ": " + i.getScore() + "\n";

		}

		return results;
	}

	/**
	 * 
	 */
	private void saveResults(File file) {
		// may want to change return value to a boolean for status check
		try {
			FileWriter writer = new FileWriter(file);
			
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
			
			//finish
			writer.flush();
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}


	/*public BufferedImage saveGraph()
	{
	 WritableImage snapshot = resultsScreen.snapshot(null);
	 BufferedImage fromFXImage = SwingFXUtils.fromFXImage(snapshot, null);
	 BufferedImage pngImage = null;
	 byte[] imageInByte;
	 try
	 {
	   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	   ImageIO.write(fromFXImage, "jpg", byteArrayOutputStream);
	   byteArrayOutputStream.flush();
	   imageInByte = byteArrayOutputStream.toByteArray();
	   byteArrayOutputStream.close();
	   InputStream in = new ByteArrayInputStream(imageInByte);
	   pngImage = ImageIO.read(in);
	 }
	 catch (IOException e)
	 {
	   e.printStackTrace();
	 }
	 return pngImage;
	}
	
	public void saveTest(Scene scene) {
		WritableImage image = scene.snapshot(null);
	    File file = new File("Chart.png");
	    try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println("Image Saved");
	}*/
	/**
	 * Hot Keys WIP
	 */
	@Override
	public void handle(KeyEvent e) {
		if (e.getCode() == e.getCode().W && e.isControlDown()) {
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

		else if (e.getCode() == e.getCode().S && e.isControlDown()) {
			if (primary.getScene().equals(resultsScreen)) {
				System.out.println("Saving results...");
			}
		}

	}

}