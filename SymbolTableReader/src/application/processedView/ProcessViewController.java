package application.processedView;

import java.io.File; 
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MultipleSelectionModel;
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
	@FXML private TextField filterField = new TextField();
	@FXML private Label saveFileError;
	@FXML private RadioButton exportText;
	@FXML private RadioButton exportXML;
	@FXML private RadioButton exportMatlab;
	@FXML private ToggleGroup exportFormat;
	@FXML private Button preview;
	@FXML private Button helpButton;
	@FXML private Label helpError;
	@FXML private Label exportError;
	@FXML private CheckBox deselect;
	@FXML private ImageView humanReadable = new ImageView();
	@FXML private ImageView matlab = new ImageView();
	@FXML private ImageView xml = new ImageView();


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
		parseMany(structs, root);
		
		root.setExpanded(true);
		treeView.setRoot(root);
		treeView.refresh();
		deselect.setSelected(false);
	}
	
	public void selectSearch() {
		if (!deselect.isSelected()) {
		selectMany(treeView.getRoot().getChildren(), filterField.getText());
		}
		else {
			deselectMany(treeView.getRoot().getChildren(), filterField.getText());
		}
	}
	
	public void selectMany(ObservableList<TreeItem<Structure>> list, String filter) {
		MultipleSelectionModel<TreeItem<Structure>> model = treeView.getSelectionModel();
		for (TreeItem<Structure> item : list) {
			if (item.getValue().getName().contains(filter)) {
				model.select(treeView.getRow(item));
			}
			else {
					selectMany(item.getChildren(), filter);
				}
			}
	}
	
	public void deselectMany(ObservableList<TreeItem<Structure>> list, String filter) {
		MultipleSelectionModel<TreeItem<Structure>> model = treeView.getSelectionModel();
		for (TreeItem<Structure> item : list) {
			if (item.getValue().getName().contains(filter)) {
				model.clearSelection(treeView.getRow(item));
			}
			else {
					deselectMany(item.getChildren(), filter);
				}
			}
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

	/*
	 * Detects which format was chosen by the user to export selected structures in.
	 */
	public void detectExportSelectedFormat()
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

	/*
	 * Exports the structures selected by the user to a Matlab file in their current working directory.
	 */
	private void exportSelectedToMatlab() 
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".mat");//creating the full file path
			try 
			{
				if(MATLABFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}
		}
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}
	
	/*
	 * Exports the structures selected by the user to an XML file in their current working directory.
	 */
	private void exportSelectedToXML()
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".xml");//creating the full file path
			try {
				if(XMLFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}	
		}
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}

	/*
	 * Exports the structures selected by the user to a file in their current working directory.
	 */
	public void exportSelectedToText()
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".txt");//creating the full file path
			try 
			{
				if(ReadableFormatParser.parseSelected(treeView.getSelectionModel().getSelectedItems(),filePath)){
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}
		}	
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}

	/*
	 * Detects which format was chosen by the user to export structures in.
	 */
	public void detectExportAllFormat()
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
	public void exportAllToText()
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".txt");//creating the full file path
			try {
				if(ReadableFormatParser.parseStructure(structs,0,filePath)){
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}
		}
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}
	
	/*
	 * Action to export all structures to a Matlab file.
	 */
	public void exportAllToMatlab()
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".mat");//creating the full file path
			try 
			{
				if(MATLABFormatParser.parseStructure(structs,0,filePath))
				{
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}	
		}
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}

	/*
	 * Action to export all structures to an XML file.
	 */
	public void exportAllToXML()
	{
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		if(isValid(fileName))
		{
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
			String filePath = (s + File.separator + fileName + ".xml");//creating the full file path
			try 
			{
				if(XMLFormatParser.parseStructure(structs,filePath))
				{
					saveFileError.setText("Structures successfully saved.");
				}
				else
				{
					saveFileError.setText("File with this name already exists!");
				}
			} 
			catch (IOException e) 
			{
				saveFileError.setText("Sorry, error saving file. Please try agian.");
			}			
		}
		else
		{
			saveFileError.setText("Invalid file name.");
		}
	}

	/*
	 * Action to display the export format options.
	 */
	public void showExportOptions()
	{
		try 
		{/*
			Path currentRelativePath = Paths.get("");//getting the cwd path as an object
			String cwd = currentRelativePath.toAbsolutePath().toString();//current working directory as a string
			System.out.print(cwd);
			Image hr = new Image("file:" + File.separator + cwd + File.separator + "textexample.PNG");
			Image ml =  new Image("file:" + File.separator + cwd + File.separator + "matlabexample.PNG");
			Image x = new Image("file:" + File.separator + cwd + File.separator + "xmlexample.PNG");
			humanReadable.setImage(hr);
			matlab.setImage(ml);
			xml.setImage(x);*/
			Main.buildExportStage();
			Main.showExportView();
		} 
		catch (IOException e) 
		{
			exportError.setText("Unable to show the export options.");
		}
	}

	/*
	 * Returns the GUI back to the mainView.
	 */
	public void back(ActionEvent e)
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

	/*
	 * Confirms that a fileName is valid
	 * 
	 * @param	fileName	The fileName that is to be checked.
	 * @return	boolean		True if the fileName is valid, false if it is not.
	 */
	private static boolean isValid(String fileName) 
	{
		Pattern p = Pattern.compile("[a-zA-Z_0-9]+");
		Matcher m = p.matcher(fileName);
		boolean isValid = false;
		if(!fileName.replaceAll("\\s+", "").isEmpty() && m.matches())
		{
			isValid = true;
		}
		return isValid;
	}

	/*
	 * Action to display the help window.
	 */
	public void help()
	{
		try 
		{
			Main.showHelpView2();
		} 
		catch (IOException e) 
		{
			helpError.setText("Sorry, we are unable to provide help information at this time.");
		}
	}
	
}
