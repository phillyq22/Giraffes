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
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;

/**
 * Controller for the processing windows. Handles any actions within the TreeView
 * @author Matt Moore and Philip Quinn
 * @version 3/1/18
 */


public class ProcessViewController implements Initializable
{
	/**This annotation allows eclipse to recognize the fx:id created in SceneBuilder and utilize it as a variable accordingly*/
	@FXML TreeView<Structure> treeView = new TreeView<Structure>();
	@FXML Button back;
	@FXML private TextField exportFileName = new TextField();
	@FXML private Label saveFileError;
	@FXML private RadioButton exportText;
	@FXML private RadioButton exportXML;
	@FXML private RadioButton exportMatlab;
	@FXML private ToggleGroup exportFormat;

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
		TreeItem<Structure> root = new TreeItem<Structure>(null);
		root.setExpanded(true);
		parseMany(structs, root);
		treeView.setRoot(root);
		treeView.refresh();
	}
	
	/*
	 * Parses several structures.
	 * 
	 * @param structs	the list of structures to parse.
	 * @param parent	the parent treeitem.
	 */
	public static void parseMany(ArrayList<Structure> structs, TreeItem<Structure> parent)
	{
		for (Structure s : structs)
		{
			TreeItem<Structure> node = new TreeItem<Structure>(s);
			parseSingle(node, s);
			parent.getChildren().add(node);
		}
	}
	
	/*
	 * Parses a single treeItem for a single structure.
	 * 
	 * @param parent	the parent treeitem.
	 * @param s 		the structure that's being parsed.
	 */
	public static void parseSingle(TreeItem<Structure> parent, Structure s)
	{
		for (Field f : s.getFields())
		{
		parent.getChildren().add(new TreeItem<Structure>(f));
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
	
	public void detectExportSelectedFormat() throws IOException
	{
		if(exportText.isSelected())
		{
			exportSelectedToText();
		}
		else if(exportXML.isSelected())
		{
			exportSelectedToXML();
		}
		else if(exportMatlab.isSelected())
		{
			exportSelectedToMatlab();
		}
	}
	
	private void exportSelectedToMatlab() throws IOException {
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(MATLABFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}		
	}

	private void exportSelectedToXML() throws IOException 
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(XMLFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}		
	}

	/*
	 * Exports the structures selected by the user to a file in their current working directory.
	 */
	public void exportSelectedToText() throws IOException
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
	
	public void detectExportAllFormat() throws IOException
	{
		if(exportText.isSelected())
		{
			exportAllToText();
		}
		else if(exportXML.isSelected())
		{
			exportAllToXML();
		}
		else if(exportMatlab.isSelected())
		{
			exportAllToMatlab();
		}
	}
	
	/*
	 * Exports all the loaded structures to a file in their current working directory.
	 */
	public void exportAllToText() throws IOException
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
	
	public void exportAllToMatlab() throws IOException 
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(MATLABFormatParser.parseStructure(structs,0,filePath))
		{
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}		
	}

	public void exportAllToXML() throws IOException 
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		if(XMLFormatParser.parseStructure(structs,0,filePath))
		{
			saveFileError.setText("");
		}
		else
		{
			saveFileError.setText("File with this name already exists!");
		}			
	}

	/*
	 * Returns the GUI back to the mainView.
	 */
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
