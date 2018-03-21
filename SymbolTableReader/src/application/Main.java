package application;
	
import java.io.IOException; 
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/*
 * Main class that sets up the GUI 
 * 
 * @author Philip Quinn and Matt Moore
 * @version 3.8.2018
 */
public class Main extends Application {

	private static Stage primaryStage;
	private static BorderPane mainLayout;
	private static Stage processStage;
	private static BorderPane processLayout;
	/*
	 * Starts the JavaFX program
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	public void start(Stage primaryStage) throws IOException
	{
		this.primaryStage = primaryStage;
		this.processStage = new Stage();
		buildPrimaryStage();
		buildProcessStage();
		showMainView();
		
	}
	
	/*
	 * Loads in the InitialView to display it on the primary stage
	 */
	public static void showMainView()
	{
		processStage.hide();
		primaryStage.show();
	}
	
	/*
	 * Builds the primary stage for InitialView
	 */
	public static void buildPrimaryStage() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/InitialView.fxml"));
		mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setTitle("Symbol Table Reader");
		primaryStage.setScene(scene);
	}
	
	/*
	 * Loads in the ProvessView to display it on the primary stage
	 */
	public static void showProcessView() throws IOException
	{
		primaryStage.hide();
		processStage.show();
	}
	
	/*
	 * Builds the process stage for ProcessView
	 */
	public static void buildProcessStage() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("processedView/ProcessView.fxml"));
		processLayout = loader.load();
		Scene scene = new Scene(processLayout);
		processStage.setTitle("Processed");
		processStage.setScene(scene);
	}
	
	/*
	 * launches the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
