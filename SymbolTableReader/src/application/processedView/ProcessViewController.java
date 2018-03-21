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
	@FXML private TreeView<Structure> treeView;
	@FXML private Button back;
	@FXML private TextField exportFileName;
	private static ArrayList<Structure> structs;
	private static File file;

	/**
	 * Initializations/Declarations for testing. After the structures have been populated, they are added to the TreeView as TreeItems. This
	 * is where nodes are added to other nodes as children.
	 * @params location
	 * @params resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
		Field field1 = new Field("int", "var1", "100", "50");
		Field field2 = new Field("int", "var2", "100", "50");
		Field field3 = new Field("int", "var3", "100", "50");
		Field field4 = new Field("int", "var4", "100", "50");
		Field field5 = new Field("int", "var5", "100", "50");
		Field field6 = new Field("int", "var6", "100", "50");

//		Structure structure1 = new Structure("NAME");
//		Structure structure2 = new Structure("NAME2");
//		Structure structure3 = new Structure("NAME3");
//
//		structure1.addField(field1);
//		structure1.addField(field2);
//		structure1.addField(field3);
//		
//		structure2.addField(field4);
//		structure2.addField(field5);
//		structure2.addField(field6);
//		
//		structure3.addField(field4);
//		structure3.addField(field5);
//		structure3.addField(field6);
//		
//		structure2.addStructure(structure1);
//		structure3.addStructure(structure1);
		
//		TreeItem<Structure> structure = new TreeItem<Structure>(structure1);
//		structure.setExpanded(true);
//		
//		TreeItem<Structure> childs = new TreeItem<Structure>(structure2);
//		
//		TreeItem<Structure> newNode = new TreeItem<Structure>(structure3);
//		newNode.setExpanded(false);
//
//		structure.getChildren().addAll(childs);
//		childs.setExpanded(false);
//		
//		childs.getChildren().addAll(newNode);
		
		
//		treeView.setRoot(structure);
	}
	
	/**
	 * Recognizes mouse click as selection for a certain node/item. Multiple items can be selected with shift.
	 * @param mouseEvent
	 */
	public void mouseClick(MouseEvent mouseEvent)
	{
		TreeItem<Structure> item = treeView.getSelectionModel().getSelectedItem();	
		if (mouseEvent.isControlDown())
		{
			treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			ObservableList<TreeItem<Structure>> items = treeView.getSelectionModel().getSelectedItems();
			System.out.println("shift down" + items);
		}
		if (item!=null)
		System.out.println(item);
	}
	
	public void exportAll() throws IOException
	{
		structs = InitialViewController.getStructs(); 
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = exportFileName.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName + ".txt");//creating the full file path
		File file = new File(filePath);
		ReadableFormatParser.parseStructure(structs,5,file);
	}
	
	public void back(ActionEvent e) throws IOException
	{
		Main.showMainView();
	}
	
	public static ArrayList<Structure> getStructs()
	{
		return structs;
	}
	
	public static File getFile()
	{
		return file;
	}
}
