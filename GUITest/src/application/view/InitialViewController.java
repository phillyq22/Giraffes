package application.view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import java.io.File;
import java.io.IOException; 
import java.nio.file.Path;
import java.nio.file.Paths;

import application.Main;

/*
 * Controller for the initial view of the Symbol Table Reader. 
 * Handles any actions done within the InitialView. 
 * 
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class InitialViewController {
	/*Fields*/
	@FXML private TextField filename;
	@FXML private TableView<LoadedFile> loadedFilesView;
	@FXML private TableColumn<LoadedFile, CheckBox> loadedFiles; 
	@FXML private Button removeFileButton;
	@FXML private Button processButton;
	@FXML private RadioButton dwarf2;
	@FXML private RadioButton dwarf3;
	@FXML private RadioButton dwarf4;
	@FXML private RadioButton dwarf5;

	/**
	 * Searches in the current working directory(cwd) for the file name entered by the user.
	 * If the File is found in cwd, checks if file has already been imported. If not, add
	 *  
	 * @param e   Event of clicking the import button
	 */
	public void searchFile(ActionEvent e)
	{
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
		String fileName = filename.getText();//getting the file name entered into the filename textfield
		String filePath = (s + "\\" + fileName);//creating the full file path
		File file = new File(filePath);
		if(file.exists() && !fileName.equals(""))
		{
			ObservableList<LoadedFile> list = loadedFilesView.getItems();
			int size = list.size();
			boolean found = false;
			for(int i = 0; i < size && !found; i++)
			{
				if(list.get(i).getPath().equals(filePath))
				{
					found = true;//file was already imported
				}
			}
			if(!found)
			{
				CheckBox checkBox = new CheckBox(filename.getText());
				LoadedFile lf =  new LoadedFile(filePath, checkBox);
				//Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
				loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
				list.add(lf);
				loadedFilesView.setItems(list);
			}
		}		
		else
		{
			System.err.println(false);
		}
	}
	
	/*
	 * Removes all of the files that are selected.
	 * 
	 * @param e Clicking of the remove files button
	 */
	public void removeFile(ActionEvent e)
	{
		ObservableList<LoadedFile> list = loadedFilesView.getItems();
		int listSize = list.size();
		for(int i = 0; i < listSize; i++)
		{
			LoadedFile curr = list.get(i);
			if(curr.getCheckBox().isSelected())
			{
				list.remove(i);
				i--;
				listSize--;
			}
		}
		loadedFilesView.setItems(list);
	}
	
	/*
	 * Action listener for the dwarf radio buttons
	 * 
	 * @param e Selecting a dwarf version
	 */
	public void dwarfSelect(ActionEvent e)
	{
		if(dwarf2.isSelected())
		{
			System.out.println(dwarf2.getText());
		}
		else if(dwarf3.isSelected())
		{
			System.out.println(dwarf3.getText());
		}
		else if(dwarf4.isSelected())
		{
			System.out.println(dwarf4.getText());
		}
		else if(dwarf5.isSelected())
		{
			System.out.println(dwarf5.getText());
		}
	}
	
	public void process(ActionEvent e) throws IOException
	{
		Main.showProcessView();
	}
	
}
