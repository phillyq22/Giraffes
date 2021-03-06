package application;
	
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import application.view.InitialViewController;
import java.io.File;

/*
 * Main class that sets up the GUI 
 * 
 * @author Philip Quinn and Matt Moore
 * @version 3.8.2018
 */
public class Main extends Application {

	private static Stage signInStage;
	private static BorderPane singInLayout;
	
	private static Stage primaryStage;
	private static BorderPane mainLayout;
	
	private static Stage processStage;
	private static BorderPane processLayout;
	
	private static Stage exportStage;
	private static AnchorPane exportLayout;
	
	private static Stage helpStage;
	private static AnchorPane helpLayout;
	
	private static Stage helpStage2;
	private static AnchorPane helpLayout2;
	/*
	* Starts the JavaFX program
	* @see javafx.application.Application#start(javafx.stage.Stage)
	*/
	public void start(Stage signInStage) throws IOException
	{
		this.signInStage = signInStage;
		this.processStage = new Stage();
		this.primaryStage = new Stage();
		this.exportStage = new Stage();
		this.helpStage = new Stage();
		this.helpStage2 = new Stage();

		this.primaryStage.setResizable(false);
		this.processStage.setResizable(false);
		this.signInStage.setResizable(false);
		this.exportStage.setResizable(false);
		this.helpStage.setResizable(false);
		this.helpStage2.setResizable(false);

		buildSignInStage();
		signInStage.show();	
	}
	
	public static void exitSignInView() throws IOException
	{
		signInStage.hide();
		buildPrimaryStage();
		showMainView();
	}
	
	public static void loadOldView(File file) throws IOException
	{
		signInStage.hide();
		InitialViewController.setFile(file);
		buildPrimaryStage();
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
	 * Builds and shows the help view window.
	 */
	public static void showHelpView() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/helpView.fxml"));
		helpLayout = loader.load();
		Scene scene = new Scene(helpLayout);
		helpStage.setTitle("Directions");
		helpStage.setScene(scene);
		helpStage.show();
	}
	
	/*
	 * Builds and shows the help view window.
	 */
	public static void showHelpView2() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("processedView/helpView.fxml"));
		helpLayout = loader.load();
		Scene scene = new Scene(helpLayout);
		helpStage.setTitle("Directions");
		helpStage.setScene(scene);
		helpStage.show();
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
	* Makes the exportView display.
	*/
	public static void showExportView() throws IOException
	{
		exportStage.show();
	}
	
	/*
	* Builds the sign in stage for signInView
	*/
	public static void buildSignInStage() throws IOException
	{	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("signInView/signInView.fxml"));
		singInLayout = loader.load();
		Scene scene = new Scene(singInLayout);
		signInStage.setTitle("Symbol Table Reader");
		signInStage.setScene(scene);
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
	* Builds the process stage for exportView
	*/
	public static void buildExportStage() throws IOException
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("exportView/exportView.fxml"));
		exportLayout = loader.load();
		Scene scene = new Scene(exportLayout);
		exportStage.setTitle("Export Types");
		exportStage.setScene(scene);
	}
	
	
	/*
	* launches the program
	*/
	public static void main(String[] args) {
		launch(args);
	}
}
