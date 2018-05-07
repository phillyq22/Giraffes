package application.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox; 
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
 
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.dsig.TransformException;
 
import application.Main;
import application.processedView.Parsing;
import application.processedView.ProcessViewController;
import application.processedView.Structure;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
 
/*
 * Controller for the initial view of the Symbol Table Reader.
 * Handles any actions done within the InitialView.
 *
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class InitialViewController implements Serializable, Initializable 
{
	/*Fields*/
    @FXML private TextField filename;
    @FXML private TableView<LoadedFile> loadedFilesView = new TableView<LoadedFile>();
    @FXML private TableColumn<LoadedFile, CheckBox> loadedFiles = new TableColumn<LoadedFile, CheckBox>();
    @FXML private Button removeFileButton;
    @FXML private Button processButton;
    @FXML private Button helpButton;
    @FXML private RadioButton dwarf2;
    @FXML private RadioButton dwarf3;
    @FXML private RadioButton dwarf4;
    @FXML private RadioButton dwarf5;
    @FXML private Label helpError;
    @FXML private Label fileError;
    @FXML private Label processError;
    @FXML private Label saveConfirmation;
    @FXML private TextField saveName;
    private static File file = null;
    private ArrayList<Structure> structs;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    	if(file != null)
    	{
    		readEmIn(file);
    	}
    }
    
	public void save(ActionEvent e)
	{
		String input = saveName.getText();
		Pattern p = Pattern.compile("[a-zA-Z_0-9]+");
		Matcher m = p.matcher(input);
		if(!input.replaceAll("\\s+", "").isEmpty() && m.matches())
		{
			ObservableList<LoadedFile> list = loadedFilesView.getItems();
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
	    	String filePath = (s + File.separator + input + ".ser");//creating the full file path
			File serFile = new File(filePath);
			if(!serFile.exists()){
				write(list, serFile);
				saveConfirmation.setText("Save Successful!");
			}
			else
			{
				saveConfirmation.setText("File with this name already exists.");
			}
		}
		else
		{
			saveConfirmation.setText("File name is invalid.");
		}
	}	


    /*
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
        String filePath = (s + File.separator + fileName);//creating the full file path
        File file = new File(filePath);
        if(file.exists() && !fileName.equals("") && file.isFile())
        {
            ObservableList<LoadedFile> list = loadedFilesView.getItems();
            if(!fileExist(file) && (Pattern.matches(".*[.ser]", fileName) || Pattern.matches(".*[.a]", fileName) || Pattern.matches("^([^.]+)$", fileName)))
            {
                fileError.setText("");
                CheckBox checkBox = new CheckBox(fileName);
                LoadedFile lf =  new LoadedFile(file, checkBox);
                //Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
                loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
                list.add(lf);
                loadedFilesView.setItems(list);
            }
            else
            {
                fileError.setText("File already loaded in, or is of the wrong extension.");
            }
        }      
        else
        {
            fileError.setText("Error loading file.");
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
     * Processes selected files
     *
     * @param   e   The pressing of the process button
     */
    public void process(ActionEvent e)
    {
	LoadedFile lf = getSelectedFile();

	if (lf!=null)
	{
		String fileName = new String("pahole.txt");
		String filePath = new String(lf.getFile().getAbsoluteFile().getParentFile().getAbsolutePath().toString());
		String fullFilePath = new String(filePath + File.separator + fileName);
	
		File file = new File(fullFilePath);
	
		String pahole = new String("cd " + filePath + " ; pahole tester.a > " + fullFilePath);
	
		String exeLinked = new String("cd " + filePath + " ; /usr/bin/g++ tester tester.a -lpthread");
		String lfName = lf.getFile().getName();
			
	    if (Pattern.matches(".*[.a]", lfName))
	    {
	        try {
	            String[] exeCmd = new String[] {"/bin/bash", "-c", exeLinked};
	            Process createExe = Runtime.getRuntime().exec(exeCmd);
	            createExe.destroy();
	            String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
	            Process paholeExe = Runtime.getRuntime().exec(paholeCmd);
	            paholeExe.destroy();
	        } catch (IOException e1) {
	    		processError.setText("File was not in proper format for parsing.");
	        }
	       
	    }
	    else if (!Pattern.matches("^([^.]+)$", lfName))
	    {
	        try {
	            String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
	            Process paholeExe = Runtime.getRuntime().exec(paholeCmd);
	            paholeExe.destroy();
	        } catch (IOException e1) {
	    		processError.setText("File was not in proper format for parsing.");
	        }
	    }

	    processError.setText("");
	    try 
	    {
	         structs = Parsing.parse(file);
	         ProcessViewController.setStructs(structs);
		     try 
		     {
				Main.buildProcessStage();
				Main.showProcessView();
			 } 
		     catch (IOException e1) 
		     {
		        processError.setText("Sorry, something went wrong with your processing!");
			 }
		} 
        catch (TransformException e1)
	    {
        	processError.setText("File was not in proper format for parsing!");
        }
	}
	else
	{
	    processError.setText("A single file must be selected to process!");
	}        
       
 }

	/*
	* Returns the selected file from the list of imported files.
	*
	* @return	File if only one file is selected, null otherwise.
	*/
	public LoadedFile getSelectedFile()
	{
		ObservableList<LoadedFile> list = loadedFilesView.getItems();
		int selectedFiles = 0;
		int index = -1;
		LoadedFile file = null;
		int size = list.size();
		for (int i = 0; i < size && selectedFiles < 2; i++)
		{
			if(list.get(i).getCheckBox().isSelected())
			{
				selectedFiles++;
				if(index == -1)
				{
					index = i;
				}
			}
		}
		if (selectedFiles == 1)
		{
			file = list.get(index);
		}
		return file;
	}
       
    /*
     * Allows file selection from your operating system's file browser.
     *
     * @param   e   The clicking of the selectFiles button
     */
    public void importMultiFiles(ActionEvent e)
	{
		FileChooser fc = new FileChooser();
		
		ExtensionFilter ef1 = new ExtensionFilter("All files", "*");
		ExtensionFilter ef2 = new ExtensionFilter("Archive file", "*.a");
		ExtensionFilter ef3 = new ExtensionFilter("Symbol Table Reader File", "*.ser");

		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String cwd = currentRelativePath.toAbsolutePath().toString();//current working directory as a string
		
		fc.setInitialDirectory(new File(cwd));
		fc.getExtensionFilters().add(ef1);
		fc.getExtensionFilters().add(ef2);
		fc.getExtensionFilters().add(ef3);
		File file = fc.showOpenDialog(null);
		if(file != null)
		{
			if(!fileExist(file))
			{
				String fileName = file.getName();
				if((Pattern.matches(".*[.a]", fileName) || Pattern.matches("^([^.]+)$", fileName)))
				{
					fileError.setText("");
					ObservableList<LoadedFile> list = loadedFilesView.getItems();
					CheckBox checkBox = new CheckBox(file.getName());
					LoadedFile lf =  new LoadedFile(file, checkBox);
					//Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
					loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
					list.add(lf);
					loadedFilesView.setItems(list);
					
				}
				else if(Pattern.matches(".*[.ser]", file.getName()))
				{
					readEmIn(file);
				}
				else
				{
					fileError.setText("Invalid file type.");
				}
			}
			else
			{
				fileError.setText("File was already loaded in.");
			}
		}
	}

   
    /*
     * Checks whether or not the selected file already has been selected.
     *
     * @param   file    The file to be checked
     */
    public boolean fileExist(File file)
    {
        boolean found = false;
        ObservableList<LoadedFile> list = null;
        if(loadedFilesView != null)
        {
            list = loadedFilesView.getItems();
        }
        if(list != null)
        {
            int size = list.size();
            for(int i = 0; i < size; i++)
            {
                if(list.get(i).getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                {
                    //found = true;//file was already imported
		return true;
                }
            }
        }
        return found;
    }
   
	/*
     * Writes/saves the list of loaded files to the specifed serialized file as a new Array List. 
     * (Preserves the program's loaded in files state for future use)
     *
     * @param   loadedFiles 	An ObservableList of loaded files
     * @param   file    The file to be written to
     */
	public void write(ObservableList<LoadedFile> loadedFiles, File file)
    {
    	try 
    	{
    		FileOutputStream fos = new FileOutputStream(file);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    		oos.writeObject(new ArrayList<LoadedFile>(loadedFiles));
    		oos.close();
    	} 
    	catch (Exception e) 
    	{
    		saveConfirmation.setText("Error saving, please try again.");
    	} 
    }

	/*
     * Reads/Deserializes the serialized file.
     *
     * @param   file    The file to be written to
     */
	public ObservableList<LoadedFile> read(File file) 
	{
		try 
		{
			FileInputStream in = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(in);
			ArrayList<LoadedFile> list = (ArrayList<LoadedFile>)(ois.readObject());
			return FXCollections.observableList(list);
		}
		catch (Exception e) 
		{
			return FXCollections.emptyObservableList();
		} 
	}

	/*
     * Reads the list of files serialized to the specified file into the new Table View of files.
     * (Loads old preserved file lists in)
     * @param   file    The file to be written to
     */
	public void readEmIn(File file) 
	{
		if (file.exists() && !file.isDirectory()) 
		{			
			ObservableList<LoadedFile> list = loadedFilesView.getItems();
			ObservableList<LoadedFile> listFromFile = read(file);
			int listSize = listFromFile.size();
			loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
			for (int i = 0; i < listSize; i++) 
			{
				File fileInSer = listFromFile.get(i).getFile();
				if(!fileExist(fileInSer))
				{
					CheckBox checkBox = new CheckBox(fileInSer.getName());
					LoadedFile lf =  new LoadedFile(fileInSer, checkBox);
					//Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
					list.add(lf);
				}
			}// end for
			loadedFilesView.setItems(list);
		}
	}
	
	public static void setFile(File newFile)
	{
		file  = newFile;
	}
	
	public void help()
	{
		try 
		{
			Main.showHelpView();
		} 
		catch (IOException e) 
		{
			helpError.setText("Sorry, we are unable to provide help information at this time.");
		}
	}

}
