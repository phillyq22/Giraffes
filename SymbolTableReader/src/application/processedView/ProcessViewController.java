package application.processedView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.Main;
import application.view.InitialViewController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;

/**
 * Controller for the processing windows. Handles any actions within the TreeView
 * @author Matt Moore and Philip Quinn
 * @version 3/1/18
 */


public class ProcessViewController implements Initializable
{
	/**This annotation allows eclipse to recognize the fx:id created in SceneBuilder and utilize it as a variable accordingly*/
	@FXML TreeView<String> treeView = new TreeView<String>();
	@FXML Button back;
	@FXML private TextField exportFileName = new TextField();
	@FXML private Label saveFileError;
	private static ArrayList<Structure> structs = new ArrayList<Structure>();

	/**
	 * Initializations/Declarations for testing. After the structures have been populated, they are added to the TreeView as TreeItems. This
	 * is where nodes are added to other nodes as children.
	 * @params location
	 * @params resources
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		TreeItem<String> root = new TreeItem<String>("");
		root.setExpanded(true);
		parseMany(structs, root);
		treeView.setRoot(root);
		treeView.refresh();
	}
		
	public static void parseMany(ArrayList<Structure> structs, TreeItem<String> parent)
	{
		for (Structure s : structs)
		{
			TreeItem<String> node = new TreeItem<String>(s.toString());
			parseSingle(node, s);
			parent.getChildren().add(node);
		}
	}
	
	public static void parseSingle(TreeItem<String> parent, Structure s)
	{
		for (Field f : s.getFields())
		{
		parent.getChildren().add(new TreeItem<String>(f.toString()));
		}
		if (!s.getChildren().isEmpty())
		{
			parseMany(s.getChildren(), parent);
		}
	}
	
	/**
	 * Recognizes mouse click as selection for a certain node/item. Multiple items can be selected with control.
	 * @param mouseEvent
	 */
	public void mouseClick(MouseEvent mouseEvent)
	{
		if (treeView.getSelectionModel().isSelected(0))
		{
			treeView.getSelectionModel().clearSelection(0);
		}
			treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void exportSelected() throws IOException
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(ReadableFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}
	}
	
	public void exportAll() throws IOException
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(ReadableFormatParser.parseStructure(structs,0,filePath)){
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}
	}
		
	public void back(ActionEvent e) throws IOException
	{
		Main.showMainView();
	}
	
	public ArrayList<Structure> getStructs()
	{
		return structs;
	}
	
	public static void setStructs(ArrayList<Structure> structures)
	{
		structs = structures;
	}
	
}
