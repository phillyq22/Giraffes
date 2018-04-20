package application.signInView;

import application.view.InitialViewController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/*
 * Controller for the sign in view of the Symbol Table Reader. 
 * Welcomes users and prompts the start of a new session or loading of an old session.
 * 
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class SignInViewController {
	/*Fields*/

	@FXML private Button startNewButton;
	@FXML private Button loadOldButton;

	/*
	 * Searches in the current working directory(cwd) for the file name entered by the user.
	 * If the File is found in cwd, checks if file has already been imported. If not, add
	 *  
	 * @param e   Event of clicking the import button
	 */
	public void startNew(ActionEvent e) throws IOException
	{
		Main.exitSignInView();
	}
	
	public void loadOld() throws IOException
	{
		FileChooser fc = new FileChooser();
		
		ExtensionFilter ef1 = new ExtensionFilter("Symbol Table Reader File", "*.ser");		
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String cwd = currentRelativePath.toAbsolutePath().toString();//current working directory as a string
		
		fc.setInitialDirectory(new File(cwd));
		fc.getExtensionFilters().add(ef1);
		File file = fc.showOpenDialog(null);
		if(file != null)
		{
			Main.loadOldView(file);
			//InitialViewController.readEmIn(file);
			
		}
	}
}
	
