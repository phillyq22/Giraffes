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
public class InitialViewController implements Initializable, Serializable {
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
    @FXML private Label fileError;
    @FXML private Label processError;
    private ArrayList<Structure> structs;

	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
        String fileName = "ObjectSavefile.ser";
        String filePath = (s + File.separator + fileName);//creating the full file path
	File serFile = new File(filePath);

@SuppressWarnings("unchecked")
@Override
public void initialize(URL location, ResourceBundle resources) {
	if (serFile.exists() && !serFile.isDirectory()) {
	ObservableList<LoadedFile> list = loadedFilesView.getItems();
	ObservableList<LoadedFile> listFromFile = read(serFile);
	System.out.println(listFromFile.size());
	int listSize = listFromFile.size();
	System.out.println(listFromFile.get(0).getFile().getName());
	for (int i = 0; i < listSize; i++) {
	//list.add(i, listFromFile.get(i));
	File file = listFromFile.get(i).getFile();
	CheckBox checkBox = new CheckBox(file.getName());
        LoadedFile lf =  new LoadedFile(file, checkBox);
        //Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
        list.add(lf);
	}// end of for loop
	loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
	loadedFilesView.setItems(list);
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
            if(!fileExist(file) && (Pattern.matches(".*[.o]", fileName) || Pattern.matches(".*[.a]", fileName) || !Pattern.matches(".*[.]", fileName)))
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
    public void process(ActionEvent e) throws IOException, InterruptedException, FileNotFoundException, TransformException
    {
	ObservableList<LoadedFile> list = loadedFilesView.getItems();

	LoadedFile lf = getSelectedFile();

	if (lf!=null)
	{
	String fileName = new String("pahole.txt");
	String filePath = new String(lf.getFile().getAbsoluteFile().getParentFile().getAbsolutePath().toString());
	String fullFilePath = new String(filePath + File.separator + fileName);

	File file = new File(fullFilePath);

	String pahole = new String("cd " + filePath + " ; pahole tester.a > " + fullFilePath);

	String exeLinked = new String("cd " + filePath + " ; /usr/bin/g++ tester tester.a -lpthread");
	
			
	    if (Pattern.matches(".*[.a]", lf.getFile().getName()))
	    {
	        //call runtime for filename with flags to process .a into .exe
	        //then process executable
	        try {
	            String[] exeCmd = new String[] {"/bin/bash", "-c", exeLinked};
	            Process p = Runtime.getRuntime().exec(exeCmd);
	            p.destroy();
	            String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
	            Process pp = Runtime.getRuntime().exec(paholeCmd);
	            pp.destroy();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	       
	    }
	    else if (!Pattern.matches(".*[.]", lf.getFile().getName()))
	    {
	        //already .exe, process executable
	        try {
	            String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
	            Process pp = Runtime.getRuntime().exec(paholeCmd);
	            pp.destroy();
	        } catch (IOException e1) {
	            // TODO Auto-generated catch block
	            e1.printStackTrace();
	        }
	    }

	    processError.setText("");
	    try {
	         structs = Parsing.parse(file);
		 ProcessViewController.setStructs(structs);
                 write(list, serFile);
		    Main.buildProcessStage();
		    Main.showProcessView();
		 } 
            catch (TransformException e1)
	    {
		processError.setText("File was not in proper format for parsing, please try again.");
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
		
		ExtensionFilter ef1 = new ExtensionFilter("Archive file", "*.a");
		ExtensionFilter ef2 = new ExtensionFilter("Executable file", "*.");
		
		Path currentRelativePath = Paths.get("");//getting the cwd path as an object
		String cwd = currentRelativePath.toAbsolutePath().toString();//current working directory as a string
		
		fc.setInitialDirectory(new File(cwd));
		fc.getExtensionFilters().add(ef1);
		fc.getExtensionFilters().add(ef2);
		File file = fc.showOpenDialog(null);
		
		if(file != null)
		{
			if(!fileExist(file))
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
   
   
    public File getFile()
    {
        return loadedFilesView.getItems().get(0).getFile();
    }

	public void write(ObservableList<LoadedFile> loadedFiles, File file)
    {
    	try {
    		FileOutputStream fos = new FileOutputStream(file);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    		oos.writeObject(new ArrayList<LoadedFile>(loadedFiles));
		/*int listSize = loadedFiles.size();
        	for(int i = 0; i < listSize; i++)
        	{
            	LoadedFile curr = list.get(i);
            		if(curr.getCheckBox().isSelected())
				{
					oos.writeObject(new String(list.get(i).getFile().getName()));
				}
		}*/
    		oos.close();
		System.out.printf("Files written to : " + file);
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

	public ObservableList<LoadedFile> read(File file) {
		try {
		FileInputStream in = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(in);
		List<LoadedFile> list = (List<LoadedFile>) ois.readObject();
		System.out.printf("Files read from : " + file);

	return FXCollections.observableList(list);
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	return FXCollections.emptyObservableList();
	}

}
